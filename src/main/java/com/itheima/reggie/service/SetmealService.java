package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<String> addSetmeal(SetmealDto setmealDto);

    R<Page<SetmealDto>> listSetmeals(Integer page, Integer pageSize, String name);

    void removeWithDish(List<Long> ids);

    R<List<Setmeal>> listSetmeal(Setmeal setmeal);
}
