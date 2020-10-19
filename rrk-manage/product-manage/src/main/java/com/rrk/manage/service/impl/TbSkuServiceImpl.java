package com.rrk.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbSkuMapper;
import com.rrk.common.modules.product.dto.SkuDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.manage.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbSkuServiceImpl extends ServiceImpl<TbSkuMapper, TbSku> implements ITbSkuService {
	
	

	@Autowired
	private ITbSkuService skuService;
	/**
	 * 获取商品sku列表
	 */
	@Override
	public IPage<SkuDto> getSkuList(Integer pageNo, Integer pageSize, String keyword) {
		 log.info("传入的参数：pageNo->{},pageSize->{},keyword->{}", pageNo, pageSize, keyword);
		 IPage<TbSku> iPage = new Page<>(pageNo,pageSize);
		 IPage<SkuDto> skuDtoIPage =  new Page<>(pageNo,pageSize);
		if (StrUtil.isBlank(keyword)) {
			iPage  = skuService.page(iPage);
		} else {
			iPage = skuService.page(iPage,new QueryWrapper<TbSku>().like("title",keyword));
		}
		IPage<SkuDto> list  = getList(skuDtoIPage,iPage);
		return list;
	}

	private IPage<SkuDto> getList(IPage<SkuDto> skuDtoIPage,IPage<TbSku> iPage) {
		skuDtoIPage.setPages(iPage.getPages());
		skuDtoIPage.setTotal(iPage.getTotal());
		List<SkuDto> list = new ArrayList<>();
		if (!iPage.getRecords().isEmpty()) {
			for (TbSku record : iPage.getRecords()) {
				 SkuDto skuDto = new SkuDto(record);
				 list.add(skuDto);
			}
		}
		skuDtoIPage.setRecords(list);
		return skuDtoIPage;
	}

}
