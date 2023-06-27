package com.tyr.sas.support.authorication.baidu.response;

import lombok.Data;

import java.io.Serializable;


/**
 * 响应的用户信息
 */
@Data
public class BaiDuUserResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 百度用户的唯一标识，对当前开发者帐号、当前应用唯一
	 */
	private String openid;

	/**
	 * 百度用户统一标识，对当前开发者帐号唯一
	 */
	private String unionid;

	/**
	 * 当前用户绑定手机号（需要向开放平台申请权限）
	 */
	private String securemobile;

	/**
	 * 当前登录用户的展示用户名，包含打码"*"号
	 */
	private String username;

	/**
	 * 当前登录用户的头像，头像地址拼接使用方法：https://himg.bdimg.com/sys/portrait/item/{$portrait}
	 */
	private String portrait;
	/**
	 * 自我简介，可能为空。
	 */
	private String userdetail;
	/**
	 * 生日，以yyyy-mm-dd格式显示。
	 */
	private String birthday;
	/**
	 * 婚姻状况 0:未知,1:单身,2:已婚3:恋爱4:离异
	 */
	private String marriage;
	/**
	 * 0:未知,1:男,2:女
	 */
	private String sex;
	/**
	 * 0:未知,1:A,2:B,3:O,4:AB,5:其他
	 */
	private String blood;
	/**
	 * 是否绑定手机号   0:未绑定,1:已绑定
	 */
	private String isBindMobile;
	/**
	 * 是否实名制  0:未实名制,1:已实名制
	 */
	private String isRealname;

}
