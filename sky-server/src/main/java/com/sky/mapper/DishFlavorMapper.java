package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void addFlavor(List<DishFlavor> flavors);
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{id}")
    List<DishFlavor> getDishFlavors(Long id);

    void deleteFlavor(List<Long> dishIds);

}
