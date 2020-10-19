package com.rrk.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.dto.PageDto;
import com.rrk.common.modules.product.dao.TbSpuMapper;
import com.rrk.common.modules.product.dto.SpuDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.manage.service.ITbSkuService;
import com.rrk.manage.service.ITbSpuService;
import com.rrk.manage.service.ITbStockLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Transactional(rollbackFor= Exception.class)
@Slf4j
public class TbSpuServiceImpl extends ServiceImpl<TbSpuMapper, TbSpu> implements ITbSpuService {

	
	@Autowired
	private TbSpuMapper spuMapper;
	@Autowired
	private ITbSpuService spuService;
	@Autowired
	private ITbSkuService skuService;
	@Autowired
	private ITbStockLogService stockLogService;
	
	/**
	 * 获取spu的商品的列表
	 */
	@Override
	public IPage<SpuDto> getSpuList(Integer pageNo, Integer pageSize, String keyword) {
		log.info("传入的参数：pageNo->{},pageSize->{},keyword->{}",pageNo,pageSize,keyword);
		IPage<SpuDto> iPage = new Page<>(pageNo,pageSize);
		PageDto pageDto = new PageDto(pageNo,pageSize);
		List<SpuDto> list = spuMapper.getSpuList(pageDto.getPage(),pageDto.getPageSize(),keyword);
		//获取数量
		Long total = spuMapper.getSpuCount(keyword);
		iPage.setRecords(list);
		iPage.setTotal(total);
		iPage.setPages(new PageDto(total, pageSize).getTotalPage());
		return iPage;
	}

	/**
	 * 添加sku
	 * @param sku
	 * @param userId
	 * @return
	 */
	@Override
	public boolean addSku(TbSku sku, Long userId) {
		TbSpu tbSpu = spuService.getOne(new QueryWrapper<TbSpu>().eq("id", sku.getSpuId()));
		sku.setTitle(tbSpu.getTitle());
		sku.setCreateTime(new Date());
		boolean save = skuService.save(sku);
		//int i = 1/0;
		boolean saveLog =  stockLogService.addStockLog(sku,userId);
		if (save && saveLog) {
			return true;
		}
		return false;
	}

}
