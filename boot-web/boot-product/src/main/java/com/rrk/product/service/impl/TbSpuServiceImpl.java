package com.rrk.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.constant.ElasticsearchContants;
import com.rrk.common.modules.product.dao.TbSkuMapper;
import com.rrk.common.modules.product.dao.TbSpuMapper;
import com.rrk.common.modules.product.dto.webdto.LunboDto;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import com.rrk.common.modules.product.dto.webdto.RecommendDto;
import com.rrk.common.modules.product.dto.webdto.SuggesterDto;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.product.entity.EsEntity;
import com.rrk.product.entity.EsPageEntiry;
import com.rrk.product.entity.HotWordEntity;
import com.rrk.product.service.ITbSkuService;
import com.rrk.product.service.ITbSpuService;
import com.rrk.product.utils.ElasticsearchUtil;
import com.rrk.product.utils.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.GaussDecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbSpuServiceImpl extends ServiceImpl<TbSpuMapper, TbSpu> implements ITbSpuService {

    @Autowired
    private ITbSkuService skuService;
    @Autowired
    private TbSkuMapper skuMapper;

    @Autowired
    private RabbitMqUtil rabbitMqUtil;

    /**
     * 获取轮播的相关数据
     *
     * @param defaultLunboCount
     * @return
     */
    @Override
    public List<LunboDto> getLunBos(Integer defaultLunboCount,Long userId) {
        log.info("轮播传入参数：defaultLunboCount->{},userId->{}", defaultLunboCount,userId);
        List<LunboDto> list = new ArrayList<>();
        if (userId == null) {
            //MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            list =  getLuns(null,defaultLunboCount);
        } else {
            //  2020/9/28 当用户登录时，就将用户最近浏览 下单的 加入购物车
            //  （进行加权重：加入购物车的 45 浏览的 45 下单10 ）
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders =    new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("operatetype", 2), ScoreFunctionBuilders.weightFactorFunction(45)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("operatetype", 1), ScoreFunctionBuilders.weightFactorFunction(10)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("operatetype", 0), ScoreFunctionBuilders.weightFactorFunction(45))
            };
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.termQuery("userid", userId));
            FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(boolQuery, filterFunctionBuilders);
            List<String> strings = ElasticsearchUtil.searchDataLun(ElasticsearchContants.USER_ACTION_INDEX, 1, 3, query, null, "_score,createtime", null);
            if (CollUtil.isNotEmpty(strings)) {
                list =  getLuns(strings,defaultLunboCount);
            } else {
                list =  getLuns(null,defaultLunboCount);
            }

        }
        return list;
    }

    private List<LunboDto> getLuns(List<String> strings,int defaultLunboCount) {
        List<LunboDto> list = new ArrayList<>();
        EsPageEntiry pageEntiry = null;
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        if (CollUtil.isNotEmpty(strings)) {
            List<QueryBuilder> must = bool.must();
            TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
            TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
            must.add(valid);
            must.add(saleable);
            List<QueryBuilder> should = bool.should();
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("spuname", strings.get(0));
            MatchQueryBuilder qb = QueryBuilders.matchQuery("spuname", strings.get(1));
            MatchQueryBuilder query = QueryBuilders.matchQuery("spuname", strings.get(2));
            should.add(queryBuilder);
            should.add(qb);
            should.add(query);
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
            //1按照销量分数统计
            FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("salecount")
                    .modifier(FieldValueFactorFunction.Modifier.LOG2P).factor(0.8f);
            FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
            filterFunctionBuilders[0] = ffb;
            //2计算时间的分数
            GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("createtime", "now", "30d", "2d");
            FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
            filterFunctionBuilders[1] = gauB;
            //3确定计算分数规则
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(bool, filterFunctionBuilders).boostMode(CombineFunction.SUM);
            pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, 1, defaultLunboCount, functionScoreQueryBuilder, null, null, null);
        } else {
            List<QueryBuilder> must = bool.must();
            TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
            TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
            must.add(valid);
            must.add(saleable);
             pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, 1, defaultLunboCount, bool, null, "createtime,salecount", null);
        }
        if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
            for (EsEntity record : pageEntiry.getRecordList()) {
                LunboDto lunboDto = new LunboDto();
                lunboDto.setSkuId(record.getId());
                lunboDto.setSkuName(record.getSpuName());
                String[] split = record.getSkuImage().split(",");
                lunboDto.setSkuImage(split[0]);
                list.add(lunboDto);
            }
        }
        return list;
    }

    /**
     * 竖向轮播商品数据
     *
     * @param defaultLunboCount
     * @return
     */
    @Override
    public List<LunboDto> getNewProductList(Integer defaultLunboCount) {
        log.info("精品推荐传入参数：defaultLunboCount->{}", defaultLunboCount);
        List<LunboDto> list = new ArrayList<>();
        // IPage<TbSku> page = new Page<TbSku>(0, defaultLunboCount);
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<QueryBuilder> must = bool.must();
        TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
        TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
        must.add(valid);
        must.add(saleable);
        EsPageEntiry pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, 1, defaultLunboCount, bool, null, "salecount", null);
        //  IPage<TbSku> skuIPage = skuService.page(page, new QueryWrapper<TbSku>().orderByDesc("create_time"));
        if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
            for (EsEntity record : pageEntiry.getRecordList()) {
                LunboDto lunboDto = new LunboDto();
                lunboDto.setSkuId(record.getId());
                lunboDto.setSkuName(record.getSpuName());
                String[] split = record.getSkuImage().split(",");
                lunboDto.setSkuImage(split[0]);
                list.add(lunboDto);
            }
        }
        return list;
    }

    /**
     * 精品推荐商品列表
     *
     * @param recommendCount
     * @return
     */
    @Override
    public List<RecommendDto> getBoutiqueRecommendList(Integer recommendCount) {
        log.info("推荐传入的参数：recommendCount->{}", recommendCount);
        //按照时间和销量的计算分数来展示
        List<RecommendDto> list = new ArrayList<>();
        // MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<QueryBuilder> must = bool.must();
        TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
        TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
        must.add(valid);
        must.add(saleable);
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
        //1按照销量分数统计
        FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("salecount")
                .modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.8f);
        FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
        filterFunctionBuilders[0] = ffb;
        //2计算时间的分数
        GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("createtime", "now", "30d", "2d");
        FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
        filterFunctionBuilders[1] = gauB;
        //3确定计算分数规则
        FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(bool, filterFunctionBuilders).boostMode(CombineFunction.SUM);
        EsPageEntiry pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, 1, recommendCount, query, null, null, null);
        if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
            for (EsEntity record : pageEntiry.getRecordList()) {
                RecommendDto recommendDto = new RecommendDto();
                recommendDto.setSkuId(record.getSkuId());
                recommendDto.setSkuImage(record.getSkuImage().split(",")[0]);
                recommendDto.setSkuName(record.getSpuName());
                recommendDto.setSalePrice(record.getSalePrice());
                recommendDto.setSaleCount(record.getSaleCount());
                list.add(recommendDto);
            }
        }
        return list;
    }

    /**
     * 获取商品列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<RecommendDto> getProductList(Integer pageNo, Integer pageSize,Long userId) {
        //这里要分两种情况（用户登录和没登录）
        List<RecommendDto> list = new ArrayList<>();
        if (userId == null) {
            //MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            list =  getProducts(null,pageNo,pageSize);
        } else {
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders =    new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("operatetype", 2), ScoreFunctionBuilders.weightFactorFunction(45)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("operatetype", 1), ScoreFunctionBuilders.weightFactorFunction(10)),
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("operatetype", 0), ScoreFunctionBuilders.weightFactorFunction(45))
            };
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.termQuery("userid", userId));
            FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(boolQuery, filterFunctionBuilders);
            List<String> strings = ElasticsearchUtil.searchDataLun(ElasticsearchContants.USER_ACTION_INDEX, 1, 3, query, null, "_score,createtime", null);
            if (CollUtil.isNotEmpty(strings)) {
                list =  getProducts(strings,pageNo,pageSize);
            } else {
                list =  getProducts(null,pageNo,pageSize);
            }

        }
//        Page<TbSku> page = new Page<>(pageNo, pageSize);
//        IPage<TbSku> skuIPage = skuService.page(page, new QueryWrapper<TbSku>().orderByDesc("create_time", "sale_count"));
//        if (CollUtil.isNotEmpty(skuIPage.getRecords())) {
//            for (TbSku record : skuIPage.getRecords()) {
       //         RecommendDto recommendDto = new RecommendDto(record);
//                list.add(recommendDto);
//            }
//        }
        return list;
    }

    private List<RecommendDto> getProducts(List<String> strings, Integer pageNo, Integer pageSize) {
        List<RecommendDto> list = new ArrayList<>();
        EsPageEntiry pageEntiry = null;
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        if (CollUtil.isNotEmpty(strings)) {
            List<QueryBuilder> must = bool.must();
            TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
            TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
            must.add(valid);
            must.add(saleable);
            List<QueryBuilder> should = bool.should();
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("spuname", strings.get(0));
            MatchQueryBuilder qb = QueryBuilders.matchQuery("spuname", strings.get(1));
            MatchQueryBuilder query = QueryBuilders.matchQuery("spuname", strings.get(2));
            should.add(queryBuilder);
            should.add(qb);
            should.add(query);
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
            //1按照销量分数统计
            FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("salecount")
                    .modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.6f);
            FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
            filterFunctionBuilders[0] = ffb;
            //2计算时间的分数
            GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("createtime", "now", "30d", "2d");
            FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
            filterFunctionBuilders[1] = gauB;
            //3确定计算分数规则
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(bool, filterFunctionBuilders).boostMode(CombineFunction.SUM);
            pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, pageNo, pageSize, functionScoreQueryBuilder, null, null, null);
        } else {
            List<QueryBuilder> must = bool.must();
            TermQueryBuilder valid = QueryBuilders.termQuery("valid", 1);
            TermQueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
            must.add(valid);
            must.add(saleable);
            pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, pageNo, pageSize, bool, null, "createtime,salecount", null);
        }
        if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
            for (EsEntity record : pageEntiry.getRecordList()) {
                RecommendDto recommendDto = new RecommendDto();
                recommendDto.setSkuId(record.getId());
                recommendDto.setSkuName(record.getSpuName());
                String[] split = record.getSkuImage().split(",");
                recommendDto.setSkuImage(split[0]);
                recommendDto.setSaleCount(record.getSaleCount());
                recommendDto.setSalePrice(record.getSalePrice());
                list.add(recommendDto);
            }
        }
        return list;
    }

    /**
     * 获取热搜的列表
     *
     * @param hotWordCount
     * @return
     */
    @Override
    public List<HotWordEntity> getHotList(Integer hotWordCount) {
        log.info("热搜传入的参数：hotWordCount->{}", hotWordCount);
        // 分两种情况，1热搜的内容，必须是搜索多，销量高 3天内的商品信息
        List<HotWordEntity> list = new ArrayList<>();
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        //1将搜索多的词
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("productname").field("productname.keyword").size(hotWordCount);
        FunctionScoreQueryBuilder query = getFunctionScoreQueryBuilder(queryBuilder);
        list = ElasticsearchUtil.getHotWordList(ElasticsearchContants.SEARCH_WORD_INDEX, query, aggregationBuilder);
        //若数据少就按照销量高，3天内的商品数据
//        if (CollUtil.isEmpty(list) || list.size() < hotWordCount) {
//            MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();
//            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
//            //2按照销量计算相关度
//            FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("salecount")
//                    .modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.6f);
//            FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
//            filterFunctionBuilders[0] = ffb;
//            //3.时间按照衰减函数来解决
//            GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("createtime", "now", "3d", "0d");
//            FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
//            filterFunctionBuilders[1] = gauB;
//            FunctionScoreQueryBuilder fsqb = QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders).boostMode(CombineFunction.SUM);
//            EsPageEntiry pageEntiry = ElasticsearchUtil.searchDataPage(ElasticsearchContants.MYSQL_TO_INDEX, 1, hotWordCount - list.size(), fsqb, null, null, null);
//            if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
//                for (EsEntity esEntity : pageEntiry.getRecordList()) {
//                    HotWordEntity hotWordEntity = new HotWordEntity();
//                    hotWordEntity.setProductName(esEntity.getSpuName());
//                    hotWordEntity.setSaleCount(esEntity.getSaleCount());
//                    hotWordEntity.setPrice(esEntity.getPrice());
//                    hotWordEntity.setSalePrice(esEntity.getSalePrice());
//                    hotWordEntity.setSkuId(esEntity.getSkuId());
//                    hotWordEntity.setSkuImage(esEntity.getSkuImage().split(",")[0]);
//                    list.add(hotWordEntity);
//                }
//            }
//        }
        return list;
    }

    private FunctionScoreQueryBuilder getFunctionScoreQueryBuilder(MatchAllQueryBuilder queryBuilder) {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
        //2按照销量计算相关度
        FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("sale_count")
                .modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.6f);
        FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
        filterFunctionBuilders[0] = ffb;
        //3.时间按照衰减函数来解决
        GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("create_time", "now", "3d", "0d");
        FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
        filterFunctionBuilders[1] = gauB;
        FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders).boostMode(CombineFunction.SUM);
        return query;
    }

    /**
     * 商品详情
     *
     * @param skuId
     * @return
     */
    @Override
    public ProductDetailDto getProductDetail(Long skuId, Long userId) {
        log.info("商品详情传入的参数：skuId->{},userId->{}", skuId, userId);
        ProductDetailDto productDetailDto = skuMapper.getProductDetail(skuId);
        //通过mq将用户搜索的商品详情数据存放到es用户行为分析的文档中
        rabbitMqUtil.sendUserMessage(skuId, userId, productDetailDto);
        return productDetailDto;
    }

    /**
     * 建议搜索列表
     *
     * @param searchName
     * @param code
     * @return
     */
    @Override
    public List<SuggesterDto> getSuggestList(String searchName, int code) {
        //建议搜索的基本要求
        log.info("建议搜索传入的参数：searchName->{},code->{}", searchName, code);
        List<SuggesterDto> list = new ArrayList<>();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("prename", searchName);
        list = ElasticsearchUtil.searchSuggester(ElasticsearchContants.PREFIX_INDEX, 1, code, queryBuilder, null, "", searchName);
        return list;
    }

    /**
     * 根据搜索词获取商品列表
     *
     * @param searchName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<RecommendDto> getSkuListByname(String searchName, Integer saleOrder,Integer priceOrder, String brandName, BigDecimal minPrice, BigDecimal maxPrice, Integer pageNo, Integer pageSize) {
        log.info("搜索词传入的参数：searchName->{},pageNo->{},pageSize->{}", searchName, pageNo, pageSize);
        List<RecommendDto> list = new ArrayList<>();
        //1，先按照品牌搜索 ，然后按照商品spu进行搜索,将销量大 时间按照衰减函数查询出来
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<QueryBuilder> must = bool.must();
        QueryBuilder valid = QueryBuilders.termQuery("valid", 1);
        QueryBuilder saleable = QueryBuilders.termQuery("saleable", 1);
        QueryBuilder spuname = QueryBuilders.matchQuery("spuname", searchName);
        must.add(valid);
        must.add(saleable);
        must.add(spuname);
        if (StrUtil.isNotBlank(brandName)) {
            QueryBuilder brand = QueryBuilders.termQuery("brandname.keyword",brandName);
            must.add(brand);
        }
        if (minPrice != null && maxPrice != null) {
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("saleprice").from(minPrice).to(maxPrice)
                    .includeLower(true).includeUpper(true);
            must.add(queryBuilder);
        } else if(minPrice != null && maxPrice == null) {
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("saleprice").gte(minPrice);
            must.add(queryBuilder);
        } else if(minPrice == null && maxPrice != null){
            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("saleprice").lte(maxPrice);
            must.add(queryBuilder);
        }
        QueryBuilder query = null;
        if (saleOrder == -1 && priceOrder == -1) {
            query = getSearchProduct(bool);
        }else {
            query = bool;
        }
        EsPageEntiry pageEntiry = ElasticsearchUtil.searchNameDataPage(ElasticsearchContants.MYSQL_TO_INDEX, pageNo, pageSize, query, saleOrder,priceOrder,null);
        if (CollUtil.isNotEmpty(pageEntiry.getRecordList())) {
            for (EsEntity esEntity : pageEntiry.getRecordList()) {
                RecommendDto recommendDto = new RecommendDto();
                recommendDto.setSkuId(esEntity.getSkuId());
                recommendDto.setSkuImage(esEntity.getSkuImage().split(",")[0]);
                recommendDto.setSkuName(esEntity.getSpuName());
                recommendDto.setSalePrice(esEntity.getSalePrice());
                recommendDto.setSaleCount(esEntity.getSaleCount());
                list.add(recommendDto);
            }
            //2.每次将搜索的第一个商品放到热搜index中
            rabbitMqUtil.sendHotWord(pageEntiry.getRecordList().get(0));
        }
        return list;
    }

    private FunctionScoreQueryBuilder getSearchProduct(BoolQueryBuilder bool) {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[2];
        //2按照销量计算相关度
        FieldValueFactorFunctionBuilder fvfb = ScoreFunctionBuilders.fieldValueFactorFunction("salecount")
                .modifier(FieldValueFactorFunction.Modifier.LOG2P).factor(0.8f);
        FunctionScoreQueryBuilder.FilterFunctionBuilder ffb = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fvfb);
        filterFunctionBuilders[0] = ffb;
        //3.时间按照衰减函数来解决
        GaussDecayFunctionBuilder gdfb = ScoreFunctionBuilders.gaussDecayFunction("createtime", "now", "28d", "3d");
        FunctionScoreQueryBuilder.FilterFunctionBuilder gauB = new FunctionScoreQueryBuilder.FilterFunctionBuilder(gdfb);
        filterFunctionBuilders[1] = gauB;
        FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(bool, filterFunctionBuilders).boostMode(CombineFunction.SUM);
        return query;

    }
}
