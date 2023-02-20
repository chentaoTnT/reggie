package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.MyBaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/**/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,
                             @RequestBody Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        String username = employee.getUsername();
        Employee empByUsername = employeeService.selectEmployeeByUsername(username);

        //3、如果没有查询到则返回登录失败结果
        if (empByUsername == null) {
            return R.error("登录失败，未找到账号");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if (!empByUsername.getPassword().equals(password)) {
            return R.error("登录失败，密码不正确");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (empByUsername.getStatus() != 1) {
            return R.error("登录失败，账号已被禁用");
        }

        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", empByUsername.getId());
        return R.success(empByUsername);
    }

    /**
     * 员工退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        Object id = request.getSession().getAttribute("employee");
        if (id != null) {
            request.getSession().setAttribute("employee", null);
            return R.success(null);
        }
        return R.error("退出失败");
    }

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<Employee> addEmployee(@RequestBody Employee employee,
                                   HttpServletRequest request) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(1);
        Long id = (Long) request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        employeeService.save(employee);
        return R.success(employee);
    }


    /**
     * 员工管理分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> listEmployees(@RequestParam int page,
                                           @RequestParam int pageSize,
                                           @RequestParam(required = false) String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        return employeeService.listEmployees(page, pageSize, name);
    }

    /**
     * 实现修改员工
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateEmployee(@RequestBody Employee employee,
                                    HttpServletRequest request){
        log.info("employee={}",employee.toString());
        Long id = (Long) request.getSession().getAttribute("employee");
        MyBaseContext.setCurrentId(id);
        return employeeService.updateEmployee(employee, id);
    }

    /**
     * 实现编辑员工的数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> listEmployee(@PathVariable("id")Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

}
