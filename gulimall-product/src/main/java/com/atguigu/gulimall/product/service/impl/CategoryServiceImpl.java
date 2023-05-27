package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> data=categoryEntities.stream().filter(x->x.getParentCid()==0)
                .map(x->{x.setChildren(getChilder(x,categoryEntities));
                return x;})
                .sorted((s1,s2)->{return (s1.getSort()==null?0:s1.getSort())-(s1.getSort()==null?0:s1.getSort());})
                .collect(Collectors.toList());
        return data;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public void dropManu(Integer dropingCatId,Integer dropCatId,String type) {
        CategoryEntity dropingcategoryEntity = baseMapper.selectById(dropingCatId);
        CategoryEntity dropcategoryEntity = baseMapper.selectById(dropCatId);
        List<CategoryEntity> entities = baseMapper.selectList(null);
        if (!canDropOrNot(dropingcategoryEntity,dropcategoryEntity,type)){
            return;
        }
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_cid",dropingcategoryEntity.getCatId());
        List<CategoryEntity> categoryEntities = baseMapper.selectList(wrapper);
        if (type.equals("inner")){
            dropingcategoryEntity.setParentCid(dropcategoryEntity.getCatId());
            dropingcategoryEntity.setCatLevel(dropcategoryEntity.getCatLevel()+1);
            baseMapper.updateById(dropingcategoryEntity);
        }
        else if (type.equals("before")){
            if (dropcategoryEntity.getParentCid()==0){
                dropingcategoryEntity.setParentCid(0L);
                dropingcategoryEntity.setCatLevel(1);
                dropingcategoryEntity.setSort(dropcategoryEntity.getSort()+1);
                baseMapper.updateById(dropingcategoryEntity);
                if (categoryEntities.size()>0) {
                    setChilderLevel(dropingcategoryEntity, entities).stream().map(categoryEntity -> baseMapper.updateById(categoryEntity));
                }
            }else {
                dropingcategoryEntity.setParentCid(dropcategoryEntity.getParentCid());
                dropingcategoryEntity.setCatLevel(dropcategoryEntity.getCatLevel());
                dropingcategoryEntity.setSort(dropcategoryEntity.getSort()+1);
                baseMapper.updateById(dropingcategoryEntity);
                if (categoryEntities.size()>0) {
                    setChilderLevel(dropingcategoryEntity, entities).stream().map(categoryEntity -> baseMapper.updateById(categoryEntity));
                }
            }
        }else {
            if (dropingcategoryEntity.getParentCid()==0){
                dropingcategoryEntity.setParentCid(0L);
                dropingcategoryEntity.setCatLevel(1);
                dropingcategoryEntity.setSort(dropcategoryEntity.getSort()-1);
                baseMapper.updateById(dropingcategoryEntity);
                if (categoryEntities.size()>0) {
                    setChilderLevel(dropingcategoryEntity, entities).stream().map(categoryEntity -> baseMapper.updateById(categoryEntity));
                }
            }else {
                dropingcategoryEntity.setParentCid(dropcategoryEntity.getParentCid());
                dropingcategoryEntity.setCatLevel(dropcategoryEntity.getCatLevel());
                dropingcategoryEntity.setSort(dropingcategoryEntity.getSort()-1);
                baseMapper.updateById(dropingcategoryEntity);
                if (categoryEntities.size()>0) {
                    setChilderLevel(dropingcategoryEntity, entities).stream().map(categoryEntity -> baseMapper.updateById(categoryEntity));
                }
            }
        }

    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
//        List<Long> list = new ArrayList<>();
//        setCatelogPath(catelogId,list);
//        Collections.reverse(list);
//        Long[] longs = new Long[3];
//        Long[] arr = (Long[]) list.toArray(longs);

        List<Long> abc = new LinkedList<>();
        abc.add(catelogId);
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        while (true){
            if(categoryEntity.getParentCid()!=0){
                categoryEntity=baseMapper.selectById(categoryEntity.getParentCid());
                abc.add(categoryEntity.getCatId());
            }
            else break;
        }
        return abc.toArray(new Long[3]);
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(category.getName());
        categoryBrandRelationDao.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id",category.getCatId()));
    }

    private List<Long> setCatelogPath(Long catelogId,List<Long> path){
        path.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid()!=0){
            setCatelogPath(categoryEntity.getParentCid(),path);
        }
        return path;
    }

    private List<CategoryEntity> setChilderLevel(CategoryEntity categoryEntity,List<CategoryEntity> categoryEntities){
        List<CategoryEntity> Children = categoryEntities.stream().filter(x->x.getParentCid()==categoryEntity.getCatId())
                .map(x-> {
                    x.setCatLevel(categoryEntity.getCatLevel() + 1);
                    setChilderLevel(x,categoryEntities);
                    return x;})
                .sorted((s1,s2)->{return (s1.getSort()==null?0:s1.getSort())-(s1.getSort()==null?0:s1.getSort());})
                .collect(Collectors.toList());

        return Children;
    }




    private List<CategoryEntity> getChilder(CategoryEntity categoryEntity,List<CategoryEntity> categoryEntities){
        List<CategoryEntity> Children = categoryEntities.stream().filter(x->x.getParentCid()==categoryEntity.getCatId())
                .map(x->{x.setChildren(getChilder(x,categoryEntities));
                    return x;})
                .sorted((s1,s2)->{return (s1.getSort()==null?0:s1.getSort())-(s1.getSort()==null?0:s1.getSort());})
                .collect(Collectors.toList());

        return Children;
    }

    private Boolean canDropOrNot(CategoryEntity dropingCategoryEntity,CategoryEntity dropCategoryEntity,String type){
        if (type.equals("inner")){
            if(dropingCategoryEntity.getCatLevel()-dropCategoryEntity.getCatLevel()>=1){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            System.out.println(type);
            if(dropingCategoryEntity.getCatLevel()-dropCategoryEntity.getCatLevel()>=0){
                return true;
            }else {
                return false;
            }
        }
    }



}