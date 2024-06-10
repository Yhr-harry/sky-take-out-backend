package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface CartService {
    void addCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void subCart(ShoppingCartDTO shoppingCartDTO);

    void cleanCart();
}
