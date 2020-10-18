package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.dto.SpuDto;
import com.rrk.common.modules.product.entity.TbSpu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface TbSpuMapper extends BaseMapper<TbSpu> {
	/**
	 * 获取商品spu列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	List<SpuDto> getSpuList(@Param("pageNo")Integer pageNo, @Param("pageSize")Integer pageSize, @Param("keyword")String keyword);

	/**
	 * 获取数量
	 * @param keyword
	 * @return
	 */
    Long getSpuCount(@Param("keyword") String keyword);
}
