package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.dto.SpuDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbSpu;

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
	 * 获取商品spu列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	IPage<SpuDto> getSpuList(Integer pageNo, Integer pageSize, String keyword);

	/**
	 * 添加sku
	 * @param sku
	 * @param userId
	 * @return
	 */
    boolean addSku(TbSku sku, Long userId);
}
