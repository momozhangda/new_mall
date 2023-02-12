package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 11:31:02
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
