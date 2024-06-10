package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
    @AutoFill(OperationType.INSERT)
    void addDish(Dish dish);

    List<Dish> getDishList(DishPageQueryDTO queryDTO);

    void deleteDish(List<Long> dishIds);
    @Select("select * from dish where id = #{dishId}")
    Dish getDishById(Long dishId);
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);
    @Update("UPDATE dish SET status = #{status} WHERE id = #{dishId}")
    void setStatus(Integer status, Long dishId);

    List<Dish> list(Dish dish);
    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = 1")
    List<Dish> getDishByCategory(Long categoryId);

    Integer countByMap(Map map);
}
