package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
    @Transactional
    @Override
    public void add(SetmealDTO setmealDTO) {
        System.out.println(setmealDTO);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.add(setmeal);
        for(SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.add(setmealDishes);


    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        PageResult pr = new PageResult();
        List<Setmeal> setmeals = setmealMapper.pageQuery(setmealPageQueryDTO);
        Page<Setmeal> page = (Page<Setmeal>) setmeals;
        List<SetmealVO> list = new ArrayList<>();
        for (Setmeal setmeal : setmeals){
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal,setmealVO);
            setmealVO.setCategoryName(setmealVO.getCategoryId() == 15? "商务套餐" : "人气套餐");
            list.add(setmealVO);
        }
        pr.setTotal(page.getTotal());
        pr.setRecords(list);
        return pr;
    }

    @Override
    public SetmealVO getSetmealById(Long id) {
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        SetmealVO setmealVO = new SetmealVO();
        List<SetmealDish> list = setmealDishMapper.getSetmeals(id);
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        for(SetmealDish setmealDish : list){
            setmealDish.setSetmealId(setmealDTO.getId());
        }
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<Long> ids = new ArrayList<>();
        ids.add(setmealDTO.getId());
//        setmealMapper.delete(ids);
        setmealDishMapper.delete(ids);
        setmealMapper.update(setmeal);
//        setmealMapper.add(setmeal);
        setmealDishMapper.add(list);
    }

    @Override
    @Transactional
    public void deleteSetmeal(List<Long> setmealIds) {
        setmealMapper.delete(setmealIds);
        setmealDishMapper.delete(setmealIds);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);

    }


}
