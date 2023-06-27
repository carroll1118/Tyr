package com.tyr.sas.support.authorication.baidu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应的token信息
 */
@Data
public class BaiDuTokenResponse implements Serializable {

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
	 * 授权范围
	 */
	private String scope;

	/**
	 * 基于http调用Open API时所需要的Session Key，其有效期与Access Token一致
	 */
	private String sessionKey;

	/**
	 * 基于http调用Open  API时计算参数签名用的签名密钥
	 */
	private String sessionSecret;

}