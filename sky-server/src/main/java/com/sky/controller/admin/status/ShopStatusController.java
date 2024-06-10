package com.sky.controller.admin.status;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShowController")
@RequestMapping("/admin/shop")
@Api("管理端商店状态相关接口")
public class ShopStatusController {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @ApiOperation("修改商店状态")
    @PutMapping("/{status}")
    public Result changeStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return Result.success();
    }
    @ApiOperation("查询商店状态")
    @GetMapping("/status")
    public Result getStatus() {
        Integer status = redisTemplate.opsForValue().get("SHOP_STATUS");
        return Result.success(status);
    }


}
