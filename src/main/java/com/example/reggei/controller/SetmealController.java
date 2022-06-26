package com.example.reggei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggei.common.R;
import com.example.reggei.dto.SetmealDTO;
import com.example.reggei.entity.Category;
import com.example.reggei.entity.Setmeal;
import com.example.reggei.service.CategoryService;
import com.example.reggei.service.SetmealDishService;
import com.example.reggei.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 * @author Hao zeng
 * @create 2022-06-25 18:49
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDTO setmealDTO){
        log.info("套餐信息：{}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDTO> setmealDTOPage = new Page<>();

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, lambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage, setmealDTOPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDTO> list = records.stream().map((item) -> {
            SetmealDTO setmealDTO = new SetmealDTO();
            // 分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String name1 = category.getName();
                setmealDTO.setCategoryName(name1);
            }
            BeanUtils.copyProperties(item, setmealDTO);
            return setmealDTO;
        }).collect(Collectors.toList());

        setmealDTOPage.setRecords(list);
        return R.success(setmealDTOPage);
    }

    /**
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }
}
