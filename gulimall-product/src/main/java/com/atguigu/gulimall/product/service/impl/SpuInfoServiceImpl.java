package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.to.SkuEsModel;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.openFeign.WareFeignService;
import com.atguigu.gulimall.product.service.SkuInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SpuInfoDao;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;
import com.atguigu.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    WareFeignService wareFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void upPorduct(Long supId) {
        List<SkuEsModel> upProducts = new ArrayList<>();

        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkuInfoList(supId);
        Boolean hasStock = null;




        upProducts = skuInfoEntities.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity,skuEsModel);
            // skuPrice,skuImg,hasStock,hotScore
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());


            // brandName,brandImg,cataLogName
            // Attrs(attrId,attrName,attrValue)



            return skuEsModel;


        }).collect(Collectors.toList());


    }

}