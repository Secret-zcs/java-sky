package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkSpaceController {
    @Autowired
    private WorkspaceService workspaceService;

    /***
     * 工作台今日数据
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("今日数据统计")
    public Result<BusinessDataVO> businessData(){
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return Result.success(workspaceService.getBusinessData(begin, end));
    }

    /**
     * 订单管理数据统计
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("订单管理数据统计")
    public Result<OrderOverViewVO> overviewOrders(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /***
     * 菜品总览数据统计
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("菜品总览数据统计")
    public Result<DishOverViewVO> overviewDishes(){
        return Result.success(workspaceService.getDishesOverView());
    }

    /***
     * 套餐总览数据统计
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐总览数据统计")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        return Result.success(workspaceService.getSetmealsOverView());
    }
}
