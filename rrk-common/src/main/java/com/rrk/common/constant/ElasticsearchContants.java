package com.rrk.common.constant;

/**
 * @author dinghao
 * @date 2020-8-21
 * elasticsearch的index信息
 */
public class ElasticsearchContants {

    /**
     * 同步mysql的数据到es
     */
    public static final String MYSQL_TO_INDEX = "sku_index";

    /**
     * 同步用户搜索的商品名称到es
     */
    public static final String SEARCH_WORD_INDEX = "hot_index";
    /**
     * 用户行为分析的索引
     */
    public static final String USER_ACTION_INDEX = "user_action_index";
    /**
     * 联想搜素的索引
     */
    public static final String PREFIX_INDEX = "prefix_index";
}
