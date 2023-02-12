package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 11:07:25
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
