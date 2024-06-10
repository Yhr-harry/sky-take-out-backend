package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for(DishFlavor flavor : flavors){
            flavor.setDishId(dishId);
        }
        if(flavors.size() > 0){
            dishFlavorMapper.addFlavor(flavors);
        }
    }

    @Override
    public PageResult getDishList(DishPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        List<Dish> dishes = dishMapper.getDishList(queryDTO);

        // Convert the list to a Page object
        Page<Dish> page = (Page<Dish>) dishes;

        List<DishVO> ds = new ArrayList<>();
        for (Dish d : page) {
            List<DishFlavor> dfs = dishFlavorMapper.getDishFlavors(d.getId());
            Category c = categoryMapper.getCategoryById(d.getCategoryId());
            DishVO dvo = new DishVO();
            BeanUtils.copyProperties(d, dvo);
            dvo.setFlavors(dfs);
            dvo.setCategoryId(c.getId());
            dvo.setCategoryName(c.getName());
            ds.add(dvo);
        }

        PageResult pr = new PageResult();
        pr.setTotal(page.getTotal());
        pr.setRecords(ds);

        return pr;
    }

    @Override
    public void deleteDish(List<Long> dishIds) {
        for(Long dishId : dishIds){
            Dish dish = dishMapper.getDishById(dishId);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealIds  = setmealDishMapper.getSetmealIds(dishIds);
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        dishMapper.deleteDish(dishIds);
        dishFlavorMapper.deleteFlavor(dishIds);
    }

    @Override
    public DishVO getDishById(Long id) {
        Dish dish = dishMapper.getDishById(id);
        List<DishFlavor> df = dishFlavorMapper.getDishFlavors(id);
        DishVO vo = new DishVO();
        BeanUtils.copyProperties(dish, vo);
        vo.setFlavors(df);
        return vo;
    }
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for(DishFlavor flavor : flavors){
            flavor.setDishId(dish.getId());
        }
        dishFlavorMapper.deleteFlavor(Arrays.asList(dish.getId()));
        dishFlavorMapper.addFlavor(flavors);
        dishMapper.updateDish(dish);
    }

    @Override
    public void setStatus(Integer status, Long id) {
        dishMapper.setStatus(status, id);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getDishFlavors(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public List<DishVO> list(Long categoryId) {
        List<DishVO> dvos = new ArrayList<>();
        List<Dish> dishList = dishMapper.getDishByCategory(categoryId);
        for(Dish d : dishList){
            DishVO dvo = new DishVO();
            BeanUtils.copyProperties(d, dvo);
            List<DishFlavor> flavors = dishFlavorMapper.getDishFlavors(d.getId());
            dvo.setFlavors(flavors);
            dvos.add(dvo);
        }
        return dvos;
    }


}
