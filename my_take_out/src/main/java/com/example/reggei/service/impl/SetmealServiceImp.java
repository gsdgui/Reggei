package com.example.reggei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggei.common.CustomException;
import com.example.reggei.common.R;
import com.example.reggei.dto.SetmealDTO;
import com.example.reggei.entity.Category;
import com.example.reggei.entity.Setmeal;
import com.example.reggei.entity.SetmealDish;
import com.example.reggei.mapper.SetmealMapper;
import com.example.reggei.service.CategoryService;
import com.example.reggei.service.SetmealDishService;
import com.example.reggei.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hao zeng
 * @create 2022-06-25 11:35
 */
@Service
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        // 保存套餐基本信息，操作setmeal表，执行insert
        this.save(setmealDTO);

        // 保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDTO.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        long count = this.count(queryWrapper);
        // 如果不能删除，抛出业务异常
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        // 删除套餐和菜品关联表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1= new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper1);
    }
}
