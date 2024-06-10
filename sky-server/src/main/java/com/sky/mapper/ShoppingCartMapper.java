package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> getCart(ShoppingCart shoppingCart);
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);
    @Insert("INSERT INTO shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            "VALUES (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);
    @Select("select * from shopping_cart where user_id = #{id}")
    List<ShoppingCart> list(Long id);
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteByCartId(Long id);
    @Delete("delete from shopping_cart where user_id = #{id}")
    void deleteByUserId(Long id);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}
