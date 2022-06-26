package com.example.reggei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggei.common.CustomException;
import com.example.reggei.entity.Category;
import com.example.reggei.entity.Dish;
import com.example.reggei.entity.Setmeal;
import com.example.reggei.mapper.CategoryMapper;
import com.example.reggei.service.CategoryService;
import com.example.reggei.service.DishService;
import com.example.reggei.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hao zeng
 * @create 2022-06-25 10:44
 */
@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    // 注入菜品Service
    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;
    /**
     * 根据 id 删除分类，删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联了菜品，如果已经关联 抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count = dishService.count(dishLambdaQueryWrapper);
        if(count > 0){
            // 已经关联了菜品
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0){
            // 已经关联了套餐
            throw new CustomException("当前分类项关联了套餐，不能删除");
        }

        // 正常删除分类
        super.removeById(id);
    }
}
