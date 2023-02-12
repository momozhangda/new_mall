package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 10:47:38
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
