package com.example.reggei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggei.entity.Employee;
import com.example.reggei.mapper.EmployeeMapper;
import com.example.reggei.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Hao zeng
 * @create 2022-06-22 23:16
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
