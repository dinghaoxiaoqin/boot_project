package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 建议搜索的返回的数据实体
 */
@Data
public class SuggesterDto implements Serializable {

    /**
     *商品名称
     */
    private String prename;
    /**
     * 需要高亮展示分词
     */
    private String highLight;
    /**
     * 品牌图片
     */
    private String brandImage;


}
