package com.rrk.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.product.dao.TbCartMapper;
import com.rrk.common.modules.product.dao.TbCartProductMapper;
import com.rrk.common.modules.product.dto.webdto.*;
import com.rrk.common.modules.product.entity.TbCart;
import com.rrk.common.modules.product.entity.TbCartProduct;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.ToolUtil;
import com.rrk.product.fegin.UserFeginClient;
import com.rrk.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@Service
@Slf4j
@Transactional(rollbackFor = MyException.class)
public class TbCartServiceImpl extends ServiceImpl<TbCartMapper, TbCart> implements ITbCartService {

    @Autowired
    private ITbCartService cartService;

    @Autowired
    private TbCartMapper cartMapper;

    @Autowired
    private ITbCartProductService cartProductService;

    @Autowired
    private ITbActivityProductService activityProductService;

    @Autowired
    private ITbPlatformActivityService platformActivityService;

    @Autowired
    private UserFeginClient userFeginClient;

    @Autowired
    private ITbSkuService skuService;

    @Autowired
    private TbCartProductMapper cartProductMapper;

    /**
     * 添加购物车
     *
     * @param userId
     * @param addCartDtos
     * @return
     */
    @Override
    public Boolean addCart(Long userId, List<AddCartDto> addCartDtos) {
        log.info("添加购物车前端传入参数：userId ->{},addCartDtos->{}", userId, addCartDtos);
        try {
            //1，判断用户是否已经有购物车 是，直接加入购物车，否 先给用户创建购物车 然后加入购物车
            List<TbCartProduct> cartProducts = new ArrayList<>();
            TbCart cart;
            cart = cartService.getOne(new QueryWrapper<TbCart>().eq("user_id", userId));
            if (ObjectUtil.isNotNull(cart)) {
                cartProducts = addProducts(cart, addCartDtos, cartProducts);
            } else {
                cart = new TbCart();
                cart.setCreateTime(new Date());
                cart.setProductNum(TbCart.MAX_COUNT);
                cart.setUserId(userId);
                cartService.save(cart);
                cartProducts = addProducts(cart, addCartDtos, cartProducts);
            }
            cartProductService.saveBatch(cartProducts);
            return true;
        } catch (Exception e) {
            log.error("添加购物车失败：" + e.getMessage());
            throw new MyException("添加购物车失败");
        }
    }

    /**
     * 删除购物车商品
     *
     * @param userId
     * @param addCartDtos
     * @return
     */
    @Override
    public Boolean removeCart(Long userId, List<AddCartDto> addCartDtos) {
        log.info("清除购物车商品传入参数：userId->{},addCartDtos->{}", userId, addCartDtos);
        try {
            if (CollUtil.isNotEmpty(addCartDtos)) {
                for (AddCartDto addCartDto : addCartDtos) {
                    TbCartProduct cartProduct = cartProductService.getOne(new QueryWrapper<TbCartProduct>().eq("user_id", userId)
                            .eq("shop_id", addCartDto.getSkuId())
                            .eq("sku_id", addCartDto.getSkuId()));
                    if (ObjectUtil.isNotNull(cartProduct)) {
                        cartProductService.removeById(cartProduct);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("删除购物车商品失败：userId->{},addCartDtos->{}", userId, addCartDtos);
            throw new MyException("删除购物车商品失败:"+e.getMessage());
        }

    }

    /**
     * 获取购物车列表
     * @param userId
     * @return
     */
    @Override
    public List<CartDto> getCarts(Long userId) {
        log.info("获取购物车列表前端传入参数：userId->{}",userId);
        List<CartDto> list = cartMapper.getCarts(userId);
        return list;
    }

    /**
     * 获取结算列表数据
     * @param userId
     * @param settleProVo
     * @return
     */
    @Override
    public SettleDto getSettleList(Long userId, SettleProVo settleProVo) {
        log.info("结算列表前端传来参数：userId->{},settleProVo->{}",userId,settleProVo);
        SettleDto settleDto = new SettleDto();
        List<ProductDto> productDtos = new ArrayList<>();
        //初始化实收金额
        BigDecimal sub = BigDecimal.ZERO;
        //满减
        BigDecimal manSub = BigDecimal.ZERO;
        //打折
        BigDecimal daSub = BigDecimal.ZERO;
        //1获取用户地址信息
        TbUserAddress userAddress = userFeginClient.getUserAddressById(userId, settleProVo.getAddressId());
        if (ObjectUtil.isNull(userAddress)) {
            throw new MyException("获取收货用户地址信息失败");
        }
        //获取结算商品数据
        for (SettleVo settleVo : settleProVo.getSettleVos()) {
            //TbActivityProduct activityProduct = activityProductService.getOne(new QueryWrapper<TbActivityProduct>().eq("sku_id", settleVo.getSkuId()));
            //TbSku sku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", settleVo.getSkuId()));
            ProductDto dto = cartProductMapper.getCartProduct(settleVo.getShopId(),settleVo.getSkuId());
            productDtos.add(dto);
        }
        double sum = productDtos.stream().mapToDouble(p -> Convert.toDouble(p.getSalesPrice())).sum();
        //double beatSum = productDtos.stream().mapToDouble(p->Convert.toDouble(p.getBeatPrefe())).sum();
        //double prefeSum = productDtos.stream().mapToDouble(p->Convert.toDouble(p.getProPrefe())).sum();
        //double totalAmount = NumberUtil.sub(sum,NumberUtil.sub(beatSum,prefeSum));
        settleDto.setProductDtos(productDtos);
        settleDto.setAddress(userAddress.getUserAddress());
        settleDto.setUserId(userId);
        settleDto.setDeliverName(userAddress.getUserName());
        settleDto.setPhone(userAddress.getUserMobile());
        //计算实际消费(满减，筹单，打折)
        List<ProductDto> dtos = productDtos.stream().filter(p -> ToolUtil.equals(1, p.getIsGift())
        && ToolUtil.equals(0,p.getIsActivity()) && ToolUtil.equals(0,p.getIsSell())
        ).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dtos)) {
            for (ProductDto dto : dtos) {
                BigDecimal subtract = Convert.toBigDecimal(sum).subtract(dto.getManAmount());
                if (Convert.toDouble(subtract) >= 0D) {
                    manSub = manSub.add(dto.getGiftAmount());
                }
            }
        }
        List<ProductDto> dtoList = productDtos.stream().filter(p -> ToolUtil.equals(0, p.getIsGift())
                && ToolUtil.equals(0,p.getIsActivity()) && ToolUtil.equals(0,p.getIsSell())
        ).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(dtoList)) {
            for (ProductDto productDto : dtoList) {
                daSub = daSub.add(productDto.getProPrefe());
            }
        }
        //计算总优惠金额 和 实付
        settleDto.setGiftPrefe(manSub);
        settleDto.setCOrderAmount(daSub);
        settleDto.setSumAmount(Convert.toBigDecimal(sum).subtract(manSub).subtract(daSub));
        return settleDto;
    }

    private List<TbCartProduct> addProducts(TbCart cart, List<AddCartDto> addCartDtos, List<TbCartProduct> cartProducts) {
        TbCartProduct cartProduct;
        for (AddCartDto addCartDto : addCartDtos) {
            if (addCartDto.getNums() > cart.getProductNum()) {
                throw new MyException("存在商品数量超越购物车上限值");
            }
            cartProduct = new TbCartProduct(addCartDto, cart);
            cartProducts.add(cartProduct);
        }
        return cartProducts;
    }
}
