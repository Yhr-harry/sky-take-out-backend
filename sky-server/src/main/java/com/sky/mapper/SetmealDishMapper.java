package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealIds(List<Long> dishIds);

    void add(List<SetmealDish> setmealDishes);

    void delete(List<Long> id);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmeals(Long id);
}
