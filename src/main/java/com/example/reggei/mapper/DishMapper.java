package com.example.reggei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggei.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Hao zeng
 * @create 2022-06-25 11:25
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
