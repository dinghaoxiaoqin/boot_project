package com.rrk.manage.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.dto.DelSkuDto;
import com.rrk.common.modules.product.dto.DelSkuDtos;
import com.rrk.common.modules.product.dto.SkuDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.manage.service.ITbSkuService;
import com.rrk.manage.service.ITbSpuService;
import com.rrk.manage.service.ITbStockLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone12 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
@Slf4j
public class TbSkuController {

    @Autowired
    private ITbSkuService skuService;
    @Autowired
    private ITbSpuService spuService;

    @Autowired
    private ITbStockLogService stockLogService;

    /**
     * 获取商品sku列表
     */
    @GetMapping(value = "/getSkuList")
    public R<Object> getSkuList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        IPage<SkuDto> page = skuService.getSkuList(pageNo, pageSize, keyword);
        return R.ok(200, "操作成功", page);

    }

    /**
     * 添加商品sku
     */
    @PostMapping(value = "/addSku")
    public R<Object> addSku(HttpServletRequest request, @RequestBody TbSku sku) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (sku.getSpuId() == null) {
            return R.fail(422, "要添加的sku不存在");
        }
       boolean save =  spuService.addSku(sku,userId);
        if (save ) {
            return R.ok(200, "添加成功");
        }
        return R.fail(437, "添加失败");
    }

    /**
     * 根据sku 的id 获取sku的数据
     */
    @GetMapping(value = "/getSku")
    public R<Object> getSku(@RequestParam(value = "id") Long id) {
        TbSku tbSku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", id));
        return R.ok(200, "操作成功", tbSku);
    }

    /**
     * 修改sku数据
     */
    @PostMapping(value = "/updateSkus")
    public R<Object> updateSkus(@RequestBody TbSku sku) {
        TbSku tbSku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", sku.getId()));
        if (ObjectUtil.isNull(tbSku)) {
            return R.fail(422, "要修改的sku不存在");
        }
        sku.setUpdateTime(new Date());
        boolean b = skuService.updateById(sku);
        if (b) {
            return R.ok(200, "修改成功");
        }
        return R.fail(437, "修改失败");
    }

    /**
     * 根据sku的id 删除sku
     */
    @PostMapping(value = "/delSku")
    public R<Object> delSku(@RequestBody DelSkuDto delSkuDto) {
        TbSku tbSku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", delSkuDto.getId()));
        if (ObjectUtil.isNull(tbSku)) {
            return R.fail(422, "要删除的sku不存在");
        }
        boolean b = skuService.removeById(delSkuDto.getId());
        if (b) {
            return R.ok(200, "删除成功");
        }
        return R.fail(437, "删除失败");
    }

    /**
     * 根据ids来删除sku
     */
    @PostMapping(value = "/delSkus")
    public R<Object> delSkus(@RequestBody DelSkuDtos delSkuDtos){
         String[] strs = delSkuDtos.getIds().split(",");
         List<String> list = Arrays.asList(strs);
         List<Long> longList = list.stream().map(s -> Convert.toLong(s)).collect(Collectors.toList());
         boolean b = skuService.removeByIds(longList);
        if (b) {
            return R.ok(200,"删除成功");
        }
        return  R.fail(437,"删除失败");
    }
}
