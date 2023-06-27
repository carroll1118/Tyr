package com.tyr.sas.support.authorication.wechat.response;

import lombok.Data;

import java.io.Serializable;


/**
 * 响应的用户信息
 */
@Data
public class WeChatUserResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 普通用户的标识，对当前开发者帐号唯一
	 */
	private String openid;

	/**
	 * 普通用户昵称
	 */
	private String nickname;

	/**
	 * 1:男,2:女
	 */
	private String sex;

	private String province;
	private String city;
	private String country;

	/**
	 * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
	 * 开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。
	 */
	private String unionid;

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	 */
	private String headimgurl;
	/**
	 * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
	 */
	private String privilege;


}
