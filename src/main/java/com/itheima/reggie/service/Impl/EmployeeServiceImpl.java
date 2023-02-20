package com.itheima.reggie.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployMapper;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployMapper, Employee> implements EmployeeService {

    /**
     * 通过username查找Employee
     * @param username
     * @return
     */
    public Employee selectEmployeeByUsername(String username){
//        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
//        wrapper.eq("username",username);
        // 以下代码同上
        //
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public R<Page<Employee>> listEmployees(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> updateEmployee(Employee employee, Long id) {
        updateById(employee);
        return R.success("员工信息修改成功");
    }
}
