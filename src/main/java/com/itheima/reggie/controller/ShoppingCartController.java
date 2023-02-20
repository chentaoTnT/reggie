package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.MyBaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 展示购物车内容
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> listShoppingCart(){
        return shoppingCartService.listShoppingCart();
    }


    /**
     * 将商品添加到购物车
     * @param shoppingCart
     * @param request
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart,
                                      HttpServletRequest request){
        return shoppingCartService.addShoppingCart(shoppingCart, request);
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }

}
