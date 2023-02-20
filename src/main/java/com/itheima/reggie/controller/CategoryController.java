package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.MyBaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/**/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 添加分类
     * @param category
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category,
                                 HttpServletRequest request){
        log.info(category.toString());
        Long id = (Long)request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        if(categoryService.save(category)) {
            return R.success(null);
        }
        else{
            return R.error("添加失败");
        }
    }

    /**
     * 用户分页展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<Category>> listCategories(@RequestParam Integer page,
                                            @RequestParam Integer pageSize){
        return categoryService.listCategories(page, pageSize);
    }

    /**
     * 实现修改分类信息
     * @param category
     * @param request
     * @return
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category,
                                    HttpServletRequest request){
        log.info(category.toString());
        Long id = (Long)request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }


    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long ids){
        log.info(ids.toString());
        return categoryService.deleteCategory(ids);
    }

    /**
     * 实现分类回显
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> listCategory(@RequestParam(required = false) Integer type){
//        log.info(type.toString());
        return categoryService.listCategories(type);
    }
}
