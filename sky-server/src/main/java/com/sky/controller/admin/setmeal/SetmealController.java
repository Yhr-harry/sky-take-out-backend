package com.sky.controller.admin.setmeal;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api("管理端套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "setmealDTO.categoryId")
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id){
        System.out.println(id);
        SetmealVO setmealVO = setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("编辑套餐")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteSetmeal(@RequestParam(name ="ids") String ids){
        if(ids == null || ids.length() == 0){
            return Result.error("请选择要删除的套餐");
        }
        List<Long> setmealIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        setmealService.deleteSetmeal(setmealIds);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateStatus(@PathVariable Integer status, @RequestParam(name ="id") Long id){
        setmealService.updateStatus(status, id);
        return Result.success();
    }

}
