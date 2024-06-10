package com.sky.controller.user.shopStatus;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api("用户端商店状态相关接口")
public class ShopStatusController {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @ApiOperation("查询商店状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = redisTemplate.opsForValue().get("SHOP_STATUS");
        return Result.success(status);
    }


}
