package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface ShoppingCartService extends IService<ShoppingCart> {
    R<List<ShoppingCart>> listShoppingCart();

    R<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart, HttpServletRequest request);
}
