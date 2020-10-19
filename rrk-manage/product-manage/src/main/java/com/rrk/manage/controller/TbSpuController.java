package com.rrk.manage.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.dto.SpuDto;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.manage.service.ITbSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
@Slf4j
public class TbSpuController {

	@Autowired
	private ITbSpuService spuService;

	/**
	 * 获取商品spu的列表数据
	 */
	@GetMapping(value = "/getSpuList")
	public R<Object> getSpuList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "keyword", required = false) String keyword) {
		IPage<SpuDto> page = spuService.getSpuList(pageNo, pageSize, keyword);
		return R.ok(200, "操作成功", page);
	}

	/**
	 * 添加商品spu
	 */
	@PostMapping(value = "/addSpu")
	public R<Object> addSpu(@RequestBody TbSpu spu) {
		spu.setCreateTime(new Date());
		Boolean flag = spuService.save(spu);
		if (flag) {
			return R.ok(200, "添加成功");
		}
		return R.fail(437, "添加失败");

	}
	/**
	 * 根据spu的id获取商品spu的数据信息
	 */
	@GetMapping(value="/getSpu")
	public R<Object> getSpu(@RequestParam(value="id") Long id){
		TbSpu spu = spuService.getOne(new QueryWrapper<TbSpu>().eq("id",id));
		return R.ok(200,"操作成功",spu);
	}
	
	/**
	 * 根据商品spu的id来修改商品spu
	 */
	@PostMapping(value= "/updateSpu")
	public R<Object> updateSpu(@RequestBody TbSpu spu){
		TbSpu tbSpu = spuService.getOne(new QueryWrapper<TbSpu>().eq("id",spu.getId()));
		if(ObjectUtil.isNotEmpty(tbSpu)) {
			spu.setUpdateTime(new Date());
			Boolean flag = spuService.updateById(spu);
			if (flag) {
				return R.ok(200,"修改成功");
			}
			return R.fail(437,"修改成功");
		} else {
			return R.fail(437,"要修改的商品不存在");
		}
		
	}

	//获取全部的spu数据
	@GetMapping(value = "/getSpuss")
	public R<Object> getSpuss(){
		List<TbSpu> spuList =  spuService.list(null);
		return R.ok(200, "操作成功", spuList);
	}

}
