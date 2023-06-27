package com.tyr.sas.support.authorication.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应的token信息
 */
@Data
public class WeChatTokenResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 授权Token
	 */
	@JsonProperty("access_token")
	private String accessToken;

	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	@JsonProperty("expires_in")
	private Integer expiresIn;

	/**
	 * 用户刷新access_token
	 */
	@JsonProperty("refresh_token")
	private String refreshToken;

	/**
	 * 授权用户唯一标识
	 */
	private String openid;

	/**
	 * 授权范围
	 */
	private String scope;
	/**
	 * 当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
	 */
	private String unionid;

}