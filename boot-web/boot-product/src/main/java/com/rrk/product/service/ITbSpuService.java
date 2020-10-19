package com.rrk.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.dto.webdto.LunboDto;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import com.rrk.common.modules.product.dto.webdto.RecommendDto;
import com.rrk.common.modules.product.dto.webdto.SuggesterDto;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.product.entity.HotWordEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */

public interface ITbSpuService extends IService<TbSpu> {

    /**
     * 首页轮播图数据
     * @param defaultLunboCount
     * @return
     */
    List<LunboDto> getLunBos(Integer defaultLunboCount,Long userId);

    /**
     * 竖向轮播图
     * @param defaultLunboCount
     * @return
     */
    List<LunboDto> getNewProductList(Integer defaultLunboCount);

    /**
     * 精品推荐商品列表
     * @param recommendCount
     * @return
     */
    List<RecommendDto> getBoutiqueRecommendList(Integer recommendCount);

    /**
     * 获取商品列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<RecommendDto> getProductList(Integer pageNo, Integer pageSize,Long userId);

    /**
     * 获取热搜列表
     * @param hotWordCount
     * @return
     */
    List<HotWordEntity> getHotList(Integer hotWordCount);

    /**
     * 获取商品详情
     * @param skuId
     * @return
     */
    ProductDetailDto getProductDetail(Long skuId,Long userId);

    /**
     * 建议搜索
     * @param searchName
     * @param code
     * @return
     */
    List<SuggesterDto> getSuggestList(String searchName, int code);

    /**
     * 通过搜索词获取商品列表
     * @param searchName
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<RecommendDto> getSkuListByname(String searchName, Integer saleOrder, Integer priceOrder,String brandName, BigDecimal minPrice,BigDecimal maxPrice, Integer pageNo, Integer pageSize);
}
