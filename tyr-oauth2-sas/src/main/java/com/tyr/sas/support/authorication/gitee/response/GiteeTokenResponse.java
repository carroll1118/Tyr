package com.tyr.sas.support.authorication.gitee.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * gitee响应的token信息
 */
@Data
public class GiteeTokenResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 授权Token
	 */
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

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

	@JsonProperty("created_at")
	private Long createdAt;

	/**
	 * 错误码
	 */
	private String error;

	/**
	 * 错误信息
	 */
	@JsonProperty("error_description")
	private String errorDescription;

}