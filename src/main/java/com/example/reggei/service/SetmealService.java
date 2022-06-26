package com.example.reggei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggei.dto.SetmealDTO;
import com.example.reggei.entity.Setmeal;

import java.util.List;

/**
 * @author Hao zeng
 * @create 2022-06-25 11:30
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO);

    public void removeWithDish(List<Long> ids);
}
