package com.example.reggei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggei.dto.DishDTO;
import com.example.reggei.entity.Dish;
import com.example.reggei.entity.DishFlavor;
import com.example.reggei.mapper.DishMapper;
import com.example.reggei.service.DishFlavorService;
import com.example.reggei.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hao zeng
 * @create 2022-06-25 11:28
 */
@Service
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存口味数据, 涉及多张表，需要事务控制，加 @Transactional
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        // 保存菜品的基本信息到菜品表 dish
        this.save(dishDTO);
        Long dishId = dishDTO.getId(); // 菜品 id

        // 保存菜品口味数据到菜品口味表 dish_flavor
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().map((item) -> {
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据 id 查询菜品信息和对应口味信息
     * @param id
     * @return
     */
    @Override
    public DishDTO getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);

        // 查询当前菜品对应口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDTO.setFlavors(dishFlavors);
        return dishDTO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        // 更新 dish 表基本信息
        this.updateById(dishDTO);

        // 清理当前菜品对应口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        // 添加当前提交过来的口味
        // 保存菜品口味数据到菜品口味表 dish_flavor

        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(dishDTO.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
