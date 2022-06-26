package com.example.reggei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggei.entity.Category;

/**
 * @author Hao zeng
 * @create 2022-06-25 10:44
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
