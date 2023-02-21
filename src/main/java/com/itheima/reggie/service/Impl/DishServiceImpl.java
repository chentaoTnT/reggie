package com.itheima.reggie.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 加上事务
    @Transactional
    @Override
    public R<String> addDish(DishDTO dishDTO) {
        dishDTO.setSort(0);
        dishDTO.setCode(UUID.randomUUID().toString());
        save(dishDTO);
        Long dishId = dishDTO.getId();
        // 保存菜品口味数据到口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().forEach(s->{
            s.setDishId(dishId);
        });
        dishFlavorService.saveBatch(flavors);
        // 清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDTO.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("添加成功");
    }

    @Override
    public R<Page<DishDTO>> listDishes(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDTO> dishDTOPage = new Page<>(page, pageSize);


        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);
        page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, dishDTOPage, "records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDTO> collect = records.stream().map(s -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(s, dishDTO);
            Long id = s.getCategoryId();
            Category category = categoryService.getById(id);
            String categoryName = category.getName();
            dishDTO.setCategoryName(categoryName);
            return dishDTO;
        }).collect(Collectors.toList());
        dishDTOPage.setRecords(collect);
        return R.success(dishDTOPage);
    }

    @Override
    public R<DishDTO> getDish(Long id) {
        Dish dish = getById(id);
        Category category = categoryService.getById(dish.getCategoryId());
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavorList = dishFlavorService.list(wrapper);
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);
        dishDTO.setCategoryName(category.getName());
        dishDTO.setFlavors(flavorList);
        return R.success(dishDTO);
    }

    @Transactional
    @Override
    public R<String> updateDish(DishDTO dishDTO) {


        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        saveOrUpdate(dish);
        // 需要先删除原先的口味
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(wrapper);
        // 再进行设置新的口味
        dishDTO.getFlavors().stream().forEach(s->{
            s.setDishId(dishDTO.getId());
        });
        dishFlavorService.saveOrUpdateBatch(dishDTO.getFlavors());

        // 清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDTO.getCategoryId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改成功");
    }

    @Override
    public R<List<DishDTO>> listDish(Dish dish) {
        List<DishDTO> collect = null;

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        
        // 先从redis中获取缓存数据
        collect = (List<DishDTO>) redisTemplate.opsForValue().get(key);
        // 如果存在，直接返回，无需查询数据库
        if(collect != null){
            return R.success(collect);
        }


        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        wrapper.orderByAsc(Dish::getSort);
        List<Dish> list = this.list(wrapper);

        collect = list.stream().map(item -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO, "flavors");
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDTO.setFlavors(dishFlavors);
            return dishDTO;
        }).collect(Collectors.toList());

        // 如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key, collect, 60, TimeUnit.MINUTES);

        return R.success(collect);
    }
}
