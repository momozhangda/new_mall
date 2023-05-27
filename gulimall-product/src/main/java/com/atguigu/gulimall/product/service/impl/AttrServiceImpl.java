package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.service.*;
import com.atguigu.gulimall.product.vo.AttrInfoVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttrVo(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        this.save(attrEntity);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }

    @Override
    public PageUtils queryBsePage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if (catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((obj)->obj.like("attr_name",key).or().like("attr_id",key));
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper

        );

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> collect = records.stream().map((attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            attrRespVo.setCatelogName(categoryService.getById(attrEntity.getCatelogId()).getName());
            Long attrGroupId = attrAttrgroupRelationService.getById(attrEntity.getAttrId()).getAttrGroupId();
            attrRespVo.setGroupName(attrGroupService.getById(attrGroupId).getAttrGroupName());
            return attrRespVo;
        })).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);

        pageUtils.setList(collect);

        return pageUtils;
    }

    @Override
    public AttrInfoVo getInfoById(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrInfoVo attrInfoVo = new AttrInfoVo();
        BeanUtils.copyProperties(attrEntity,attrInfoVo);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectById(attrEntity.getAttrId());
        attrInfoVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrInfoVo.setCatelogPath(catelogPath);
        return attrInfoVo;
    }

    @Override
    public PageUtils getSaleList(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId);
        }
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((obj)->obj.like("attr_name",key).or().like("attr_id",key));
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        List<AttrEntity> res = page.getRecords();
        List<AttrRespVo> collect = res.stream().map((attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity,attrRespVo);
            attrRespVo.setCatelogName(categoryService.getById(attrEntity.getCatelogId()).getName());
            attrRespVo.setGroupName(attrGroupService.getById(attrEntity.getAttrId()).getAttrGroupName());
            return attrRespVo;
        })).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(collect);
        return pageUtils;
    }

}