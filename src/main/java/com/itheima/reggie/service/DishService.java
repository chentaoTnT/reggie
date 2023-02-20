package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    R<String> addDish(DishDTO dishDTO);

    R<Page<DishDTO>> listDishes(Integer page, Integer pageSize, String name);

    R<DishDTO> getDish(Long id);

    R<String> updateDish(DishDTO dishDTO);

    R<List<DishDTO>> listDish(Dish dish);
}
