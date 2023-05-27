package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 09:36:19
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    void dropManu(Integer dropingCatId,Integer dropCatId,String type);

    Long[] findCatelogPath(Long catelogId);

    void updateDetail(CategoryEntity category);
}

