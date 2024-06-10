package com.sky.controller.user.cart;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.CartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/add")
    public Result addCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        cartService.addCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("list")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = cartService.list();
        return Result.success(list);
    }

    @PostMapping("/sub")
    public Result subCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        cartService.subCart(shoppingCartDTO);
        return Result.success();
    }

    @DeleteMapping("/clean")
    public Result cleanCart(){
        cartService.cleanCart();
        return Result.success();
    }
}
