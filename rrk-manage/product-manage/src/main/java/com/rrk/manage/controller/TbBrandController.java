package com.rrk.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.dto.BrandCategoryDto;
import com.rrk.common.modules.product.dto.BrandDto;
import com.rrk.common.modules.product.entity.TbBrand;
import com.rrk.manage.service.ITbBrandService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Api(value = "后台商品管理", tags = "后台商品管理中心")
@RestController
@RequestMapping("/product")
public class TbBrandController {

	@Autowired
	private ITbBrandService brandService;

	/**
	 * 获取品牌列表
	 */
	@GetMapping(value = "/getBrandList")
	public R<Object> getBrandList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "keyword", required = false) String keyword) {
		IPage<BrandCategoryDto> page = brandService.getBrandList(pageNo, pageSize, keyword);
		return R.ok(200, "操作成功", page);
	}

	/**
	 * 添加品牌
	 * 
	 * @return
	 */
	@PostMapping(value = "/addBrand")
	public R addBrand(@RequestBody BrandDto brandDto) {
		Integer insert = brandService.addBrand(brandDto);
		if (insert > 0) {
			return R.ok(200, "添加成功", "");
		}
		return R.fail(437, "添加失败");
	}

	/**
	 * 根据品牌id获取品牌数据
	 */
	@GetMapping(value = "/getBrand")
	public R<Object> getBrand(@RequestParam(value = "id") Long id) {
		TbBrand serviceOne = brandService.getOne(new QueryWrapper<TbBrand>().eq("id", id));
		return R.ok(200, "", serviceOne);
	}

	/**
	 * 修改品牌数据
	 */
	@PostMapping(value = "/editBrand")
	public R<Object> editBrand(@RequestBody BrandDto brandDto) {
		TbBrand tbBrand = new TbBrand(brandDto);
		tbBrand.setId(brandDto.getId());
		boolean b = brandService.updateById(tbBrand);
		if (b) {
			return R.ok(200, "修改成功");
		}
		return R.fail(437, "修改失败");

	}

	/**
	 * 获取全部品牌列表
	 */
	@GetMapping(value = "/getBrands")
	public R<Object> getBrands() {
		List<TbBrand> list = brandService.list(null);
		return R.ok(200, "操作成功", list);
	}
	/**
	 * 删除品牌
	 */
	@PostMapping(value = "/deleteBrand")
	public R<Object> deleteBrand(@RequestParam(value = "id") Long id){
		if (id != null) {
			boolean b = brandService.removeById(id);
			if (b) {
				return R.ok(200,"删除成功");
			} else {
				return R.fail(400,"删除失败");
			}
		}
		return R.fail(400,"要删除的品牌不存在");

	}

}
