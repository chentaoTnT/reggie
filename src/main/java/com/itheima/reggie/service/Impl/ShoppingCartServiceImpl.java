package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R<List<ShoppingCart>> listShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = this.list(wrapper);
        return R.success(list);
    }

    @Override
    public R<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        // 添加的是菜品
        if(shoppingCart.getDishId() != null){
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            wrapper.eq(ShoppingCart::getUserId, userId);

        }
        // 添加的是套餐
        else{
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            wrapper.eq(ShoppingCart::getUserId, userId);

        }
        ShoppingCart one = this.getOne(wrapper);
        // 已有菜品上+1
        if(one != null){
            one.setNumber(one.getNumber() + 1);
        }
        else {
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            one = shoppingCart;
        }
        this.saveOrUpdate(one);
        return R.success(one);
    }
}
