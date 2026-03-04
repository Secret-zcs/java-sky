package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /***
     * 今日数据查询
     * @param begin
     * @param end
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        //新增用户
        Integer newUsers = userMapper.countByMap(map);
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);
        //营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }
        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .unitPrice(unitPrice)
                .validOrderCount(validOrderCount)
                .build();
    }

    @Override
    public OrderOverViewVO getOrderOverView() {
        //全部订单
        Integer allOrders = orderMapper.countStatus(null);
        //已取消数量
        Integer cancelledOrders = orderMapper.countStatus(Orders.CANCELLED);
        //已完成数量
        Integer completedOrders = orderMapper.countStatus(Orders.COMPLETED);
        //待派送数量
        Integer deliveredOrders = orderMapper.countStatus(Orders.CONFIRMED);
        //待接单数量
        Integer waitingOrders = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    @Override
    public DishOverViewVO getDishesOverView() {
        Integer discontinued = dishMapper.countStatus(StatusConstant.DISABLE);
        Integer sold = dishMapper.countStatus(StatusConstant.ENABLE);
        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    @Override
    public SetmealOverViewVO getSetmealsOverView() {
        Integer discontinued = setmealMapper.countStatus(StatusConstant.DISABLE);
        Integer sold = setmealMapper.countStatus(StatusConstant.ENABLE);
        return SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }
}
