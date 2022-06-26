package com.example.reggei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggei.dto.DishDTO;
import com.example.reggei.entity.Dish;


/**
 * @author Hao zeng
 * @create 2022-06-25 11:26
 */
public interface DishService extends IService<Dish>{
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表 dish, dish_flavor
    public void saveWithFlavor(DishDTO dishDTO);

    // 根据id 查询菜品信息 和对应的 口味信息
    public DishDTO getByIdWithFlavor(Long id);

    // 更新菜品，同时更新对应口味信息
    public void updateWithFlavor(DishDTO dishDTO);
}
