package com.atguigu.gulimall.product.entity;

import com.atguigu.common.valid.ListValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 品牌
 * 
 * @author qyc
 * @email 565184090@qq.com
 * @date 2023-02-12 09:36:19
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotEmpty
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty
	@URL
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(values = {0,1})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */

	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(0)
	private Integer sort;

}
