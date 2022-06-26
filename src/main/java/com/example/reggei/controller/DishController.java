package com.example.reggei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggei.common.R;
import com.example.reggei.dto.DishDTO;
import com.example.reggei.entity.Category;
import com.example.reggei.entity.Dish;
import com.example.reggei.service.CategoryService;
import com.example.reggei.service.DishFlavorService;
import com.example.reggei.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hao zeng
 * @create 2022-06-25 12:15
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDTO dishDTO){
        log.info(dishDTO.toString());
        dishService.saveWithFlavor(dishDTO);
        return R.success("新增菜品成功");
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDTO dishDTO){
        log.info(dishDTO.toString());
        dishService.updateWithFlavor(dishDTO);
        return R.success("新增菜品成功");
    }

    /**
     * 分页查询菜品信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // 构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDTO> dishDTOPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        // 执行分页查询
        dishService.page(pageInfo, lambdaQueryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDTOPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDTO> list = records.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();
            Long categoryId = item.getCategoryId(); // 分类ID
            // 根据分类 ID 查分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String name1 = category.getName();
                dishDTO.setCategoryName(name1);
            }
            BeanUtils.copyProperties(item, dishDTO);
            return dishDTO;
        }).collect(Collectors.toList());

        dishDTOPage.setRecords(list);

        return R.success(dishDTOPage);
    }

    /**
     * 根据 id 查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDTO> getById(@PathVariable Long id){
        DishDTO dishDTO = dishService.getByIdWithFlavor(id);
        return R.success(dishDTO);
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 查询状态为1的，即起售状态的
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        // 排序
        lambdaQueryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
