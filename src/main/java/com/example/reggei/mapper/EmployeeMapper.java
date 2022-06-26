package com.example.reggei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggei.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hao zeng
 * @create 2022-06-22 23:13
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
