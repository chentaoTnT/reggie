package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.MyBaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/**/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品
     * @param dishDTO
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDTO dishDTO,
                             HttpServletRequest request){
        log.info(dishDTO.toString());
        Long id = (Long) request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        return dishService.addDish(dishDTO);
    }

    /**
     * 菜品分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDTO>> listDishes(@RequestParam Integer page,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(required = false) String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        return dishService.listDishes(page, pageSize, name);
    }

    /**
     * 修改数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDTO> listDish(@PathVariable("id") Long id){
        log.info("id=" + id.toString());

        return dishService.getDish(id);
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @param request
     * @return
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDTO dishDTO,
                                HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        return dishService.updateDish(dishDTO);
    }

    /**
     * 实现套餐回显
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDTO>> listDish(Dish dish){
        log.info(dish.toString());
        return dishService.listDish(dish);
    }
}
