package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult getDishList(DishPageQueryDTO queryDTO);

    void deleteDish(List<Long> dishIds);

    DishVO getDishById(Long id);

    void updateDish(DishDTO dishDTO);


    void setStatus(Integer status, Long id);

    List<DishVO> listWithFlavor(Dish dish);

    List<DishVO> list(Long categoryId);
}
