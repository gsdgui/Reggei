package com.example.reggei.dto;


import com.example.reggei.entity.Setmeal;
import com.example.reggei.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDTO extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
