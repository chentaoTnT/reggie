package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.MyBaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/**/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 套餐添加
     * @param setmealDto
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto,
                                HttpServletRequest request){
        log.info(setmealDto.toString());
        Long id = (Long) request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        return setmealService.addSetmeal(setmealDto);
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> listSetmeals(@RequestParam Integer page,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) String name){
        return setmealService.listSetmeals(page, pageSize, name);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> listSetmeal(Setmeal setmeal){
        log.info(setmeal.toString());
        return setmealService.listSetmeal(setmeal);
    }

}
