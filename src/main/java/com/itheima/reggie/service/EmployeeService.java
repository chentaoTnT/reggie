package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee selectEmployeeByUsername(String username);

    R<Page<Employee>> listEmployees(int page, int pageSize, String name);

    R<String> updateEmployee(Employee employee, Long id);
}
