package com.example.reggei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggei.common.R;
import com.example.reggei.entity.Employee;
import com.example.reggei.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Hao zeng
 * @create 2022-06-22 23:19
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
//    @Autowired
//    public EmployeeController(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }

    /**
     * 员工登录
     * @param request
     * @param employee （前端返回的Json 数据，在Post里，使用@RequestBody 自动解析为 employee）
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 1. 页面提交的密码进行 md5 加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2. 根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 3. 如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }
        // 4. 密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        // 5. 查看员工状态，如果已经为禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账户已禁用");
        }
        // 6. 登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        log.info("退出成功...");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息: {}", employee.toString());
        // 设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        // 获得当前用户登录的id
        Long empID = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        lambdaQueryWrapper.like(StringUtils.hasText(name), Employee::getName, name);
        // 添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getCreateTime);

        // 执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info("更新员工信息: {}", employee.toString());

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
