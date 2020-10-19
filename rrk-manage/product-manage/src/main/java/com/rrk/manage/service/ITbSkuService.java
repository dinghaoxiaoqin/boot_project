package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.dto.SkuDto;
import com.rrk.common.modules.product.entity.TbSku;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface ITbSkuService extends IService<TbSku> {

	/**
	 *  获取商品sku列表
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	IPage<SkuDto> getSkuList(Integer pageNo, Integer pageSize, String keyword);

}
