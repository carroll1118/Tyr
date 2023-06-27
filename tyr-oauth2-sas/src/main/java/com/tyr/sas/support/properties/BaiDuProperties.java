package com.tyr.sas.support.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("baidu")
@Component
public class BaiDuProperties {

	/**
	 * OAuth2 客户ID
	 */
	private String clientId;

	/**
	 * OAuth2 客户秘钥
	 */
	private String clientSecret;

	/**
	 * 重定向的网址前缀
	 */
	private String redirectUri;

}