package com.rrk.product.controller;

import com.rrk.common.R;
import com.rrk.common.constant.ElasticsearchContantEnum;
import com.rrk.common.modules.product.dto.webdto.LunboDto;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import com.rrk.common.modules.product.dto.webdto.RecommendDto;
import com.rrk.common.modules.product.dto.webdto.SuggesterDto;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.product.entity.HotWordEntity;
import com.rrk.product.service.ITbSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 移动端商品前端控制器
 *
 * @author dinghao
 * @date 2020-8-19
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
@Slf4j
public class ProductController {

    @Autowired
    private ITbSpuService spuService;

    /**
     * 获取首页轮播部分的商品相关数据
     */
    @GetMapping(value = "/getLunBos")
    public R<Object> getLunBos(HttpServletRequest request) {
        //后期根据用户是否登录进行不同情况下的数据分析(用户登录就根据下单 和用户最近浏览的商品，加入购物车进行相关度分析)
        Long userId = JwtTokenUtil.getUserId(request);
       // Long userId = 730578624L;
        List<LunboDto> list = spuService.getLunBos(ElasticsearchContantEnum.LUNBO_PRODUCT_COUNT.getCode(),userId);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 获取竖向轮播的商品数据（后期通过es进行数据分析展示）
     */
    @GetMapping(value = "/getNewProductList")
    public R<Object> getNewProductList(HttpServletRequest request) {
        //
        List<LunboDto> list = spuService.getNewProductList(ElasticsearchContantEnum.LUNBO_PRODUCT_COUNT.getCode());
        return R.ok(200, "操作成功", list);
    }

    /**
     * 获取精品推荐的商品数据
     *
     */
    @GetMapping(value = "/getBoutiqueRecommendList")
    public R<Object> getBoutiqueRecommendList(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        List<RecommendDto> list = spuService.getBoutiqueRecommendList(ElasticsearchContantEnum.RECOMMEND_PRODUCT_COUNT.getCode());
        return R.ok(200, "操作成功", list);
    }

    /**
     * 获取首页商品列表
     *
     */
    @GetMapping(value = "/getProductList")
    public R<Object> getProductList(HttpServletRequest request,
                                    @RequestParam(required = false, value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {
        Long userId = JwtTokenUtil.getUserId(request);
        List<RecommendDto> list = spuService.getProductList(pageNo, pageSize,userId);
        return R.ok(200, "操作成功", list);
    }

    /**
     *获取热搜排行榜
     */
    @GetMapping(value = "/getHotList")
    public R<Object> getHotList(HttpServletRequest request) {
        List<HotWordEntity> list = spuService.getHotList(ElasticsearchContantEnum.HOT_PRODUCT_COUNT.getCode());
        return R.ok(200, "操作成功", list);
    }
    /**
     * 获取商品详情
     */
    @GetMapping(value = "/getProductDetail")
    public R<Object> getProductDetail(HttpServletRequest request,
                                      @RequestParam(value = "skuId") Long skuId){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        ProductDetailDto productDetail = spuService.getProductDetail(skuId,userId);
        return R.ok(200,"操作成功",productDetail);
    }

    /**
     * 建议搜索（展示10个商品名称）
     */
    @GetMapping(value = "/getSuggestList")
    public R<Object> getSuggestList(HttpServletRequest request,
                                    @RequestParam(value = "searchName") String searchName){
        List<SuggesterDto> list = spuService.getSuggestList(searchName,ElasticsearchContantEnum.SUGGESTERS_COUNT.getCode());
        return R.ok(200,"操作成功",list);
    }

    /**
     * 根据搜索词进行搜索
     */
    @GetMapping(value = "/getSkuListByname")
    public R<Object> getSkuListByname(HttpServletRequest request,
                                      @RequestParam(value = "searchName") String searchName,
                                      @RequestParam(required = false,value = "saleOrder") Integer saleOrder,
                                      @RequestParam(required = false,value = "priceOrder") Integer priceOrder,
                                      @RequestParam(required = false,value = "brandName") String brandName,
                                      @RequestParam(required = false, value = "minPrice") BigDecimal minPrice,
                                      @RequestParam(required = false, value = "maxPrice") BigDecimal maxPrice,
                                      @RequestParam(required = true, value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(required = true, value = "pageSize", defaultValue = "10") Integer pageSize){
        List<RecommendDto> list = spuService.getSkuListByname(searchName,saleOrder,priceOrder,brandName,minPrice,maxPrice,pageNo,pageSize);
        return R.ok(200,"操作成功",list);
    }

}
