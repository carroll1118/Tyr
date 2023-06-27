package com.tyr.sas.support.constant;


/**
 * 权限相关的参数
 */
public interface SecurityConstants {

	/**
	 * 内部
	 */
	String FROM_IN = "Y";

	String Authorization = "Authorization";

	/**
	 * 标志
	 */
	String FROM = "from";

	/**
	 * 请求header
	 */
	String HEADER_INNER = FROM + "=" + FROM_IN;

	/**
	 * 短信验证码key前缀
	 */
	String SMS_CODE_PREFIX = "SMS_CODE:";

	/**
	 * 系统管理 web 客户端ID
	 */
	String ADMIN_CLIENT_ID = "tyr";

	/**
	 * 客户端编号
	 */
	String CLIENT_ID = "client_id";

	/**
	 * 客户端编号
	 */
	String AUTH_TYPE = "auth_type";

	/**
	 * Basic认证前缀
	 */
	String BASIC_PREFIX = "Basic ";

	/**
	 * 图片验证码登录
	 */
	String CAPTCHA = "captcha";

	String CAPTCHA_CODE = "captcha_code";
	/**
	 * 短信验证码
	 */
	String SMS = "sms";
	String MOBILE = "mobile";
	String SMS_CODE = "sms_code";

	/**
	 * 邮箱登录
	 */
	String MAIL = "mail";
	String MAILBOX = "mailbox";
	/**
	 * 邮箱验证码
	 */
	String MAIL_CODE = "mail_code";

	/**
	 * Gitee登录  参考文档：https://gitee.com/api/v5/oauth_doc#/list-item-1
	 */
	String GITEE = "gitee";

	/**
	 * Gitee获取授权码端点url
	 */
	String GITEE_AUTHORIZE_URL = "https://gitee.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s";

	/**
	 * Gitee通过授权码获取token请求地址
	 */
	String GITEE_ACCESS_TOKEN_URL = "https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}";

	/**
	 * Gitee获取用户信息
	 */
	String GITEE_USERINFO_URL = "https://gitee.com/api/v5/user";


	/**
	 * 微信扫码登录  参考文档：https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
	 */
	String WX_CHAT = "wechat";

	/**
	 * 微信获取授权码端点url
	 */
	String WX_AUTHORIZE_URL = " https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect ";

	/**
	 *微信通过授权码获取token请求地址
	 */
	String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";

	/**
	 * 微信获取用户信息
	 */
	String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";


	/**
	 * 百度登录  参考文档：https://openauth.baidu.com/doc/doc.html
	 */
	String BAIDU = "baidu";

	/**
	 * 百度获取授权码端点url
	 */
	String BAIDU_AUTHORIZE_URL = "https://openapi.baidu.com/oauth/2.0/authorize?response_type=CODE&client_id=%s&redirect_uri=%s&scope=%s&state=%s";

	/**
	 *百度通过授权码获取token请求地址
	 */
	String BAIDU_ACCESS_TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code=CODE&client_id={client_id}&client_secret={client_secret}&redirect_uri={redirect_uri}";

	/**
	 * 百度获取用户信息
	 */
	String BAIDU_USERINFO_URL = "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";

	/**
	 * 自己的授权服务器url
	 */
	String TOKEN_URL = "/oauth2/token?grant_type={grant_type}&appid={appid}&code={code}&state={state}&client_id={client_id}&client_secret={client_secret}&binding={binding}";

}