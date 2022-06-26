package com.example.reggei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggei.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hao zeng
 * @create 2022-06-26 11:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
