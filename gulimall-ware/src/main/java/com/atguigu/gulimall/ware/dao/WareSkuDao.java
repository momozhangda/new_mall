package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 11:34:39
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Integer getHasStockNum(Long skuId);
}
