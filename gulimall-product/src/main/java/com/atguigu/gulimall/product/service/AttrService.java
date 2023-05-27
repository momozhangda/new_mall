package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.AttrInfoVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 09:36:19
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrVo(AttrVo attrVo);

    PageUtils queryBsePage(Map<String, Object> params, Long catelogId);

    AttrInfoVo getInfoById(Long attrId);

    PageUtils getSaleList(Map<String, Object> params, Long catelogId);
}

