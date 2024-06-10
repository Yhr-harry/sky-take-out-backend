package com.sky.controller.admin.dish;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api("餐点相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    @ApiOperation("添加菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("开始添加菜品");
        dishService.addDish(dishDTO);
        String key = "dist_" + dishDTO.getCategoryId();
        deleteCache(key);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> getDishList(DishPageQueryDTO queryDTO){
        PageResult pg = dishService.getDishList(queryDTO);
        return Result.success(pg);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteDish(@RequestParam(name ="ids") String ids){
        List<Long> dishIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        dishService.deleteDish(dishIds);
        deleteCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        DishVO dvo = dishService.getDishById(id);
        return Result.success(dvo);
    }

    @PutMapping
    @ApiOperation("编辑菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        deleteCache("dish_*");
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用菜品")
    public Result updateStatus(@PathVariable Integer status, @RequestParam(name ="id") Long id){
        dishService.setStatus(status, id);
        deleteCache("dish_*");
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品列表")
    public Result<List<DishVO>> list(@RequestParam(name ="categoryId") Long categoryId){
        List<DishVO> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void deleteCache (String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
