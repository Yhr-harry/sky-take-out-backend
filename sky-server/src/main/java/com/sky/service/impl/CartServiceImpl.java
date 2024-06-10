package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long id = BaseContext.getCurrentId();
        shoppingCart.setUserId(id);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getCart(shoppingCart);
        if(shoppingCartList != null && shoppingCartList.size() > 0){
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId != null){
                Dish dish = dishMapper.getDishById(dishId);
                ShoppingCart cart = new ShoppingCart();
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                Setmeal setmeal = setmealMapper.getSetmealById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    @Override
    public List<ShoppingCart> list() {
        Long id = BaseContext.getCurrentId();

        return shoppingCartMapper.list(id);
    }

    @Override
    public void subCart(ShoppingCartDTO shoppingCartDTO) {
        Long uId = BaseContext.getCurrentId();
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,cart);
        cart.setUserId(uId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getCart(cart);
        if(shoppingCartList!= null && shoppingCartList.size() > 0){
            ShoppingCart cart1 = shoppingCartList.get(0);
            cart1.setNumber(cart1.getNumber() - 1);
            if(cart1.getNumber() > 0){
                shoppingCartMapper.updateNumberById(cart1);
            }else{
                shoppingCartMapper.deleteByCartId(cart1.getId());
            }

        }
    }

    @Override
    public void cleanCart() {
        Long id = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(id);
    }
}
