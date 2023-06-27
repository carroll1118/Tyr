package com.tyr.sas.support.authorication.baidu;

import cn.hutool.extra.spring.SpringUtil;
import com.tyr.sas.support.authorication.TyrAbstractAuthenticationProvider;
import com.tyr.sas.support.authorication.baidu.response.BaiDuTokenResponse;
import com.tyr.sas.support.authorication.baidu.response.BaiDuUserResponse;
import com.tyr.sas.support.constant.SecurityConstants;
import com.tyr.sas.support.properties.BaiDuProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.tyr.sas.support.constant.SecurityConstants.BAIDU_ACCESS_TOKEN_URL;
import static com.tyr.sas.support.constant.SecurityConstants.BAIDU_USERINFO_URL;

public class TyrBaiDuAuthenticationProvider extends TyrAbstractAuthenticationProvider<TyrBaiDuAuthenticationToken> {

    public TyrBaiDuAuthenticationProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TyrBaiDuAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        if (registeredClient != null && !registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.BAIDU))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public Authentication getAuthentication(Map<String, Object> reqParameters) {
        // 构造token返回
        // 授权码
        String code = (String) reqParameters.get(OAuth2ParameterNames.CODE);
        // 码云应用id
        String clientId = (String) reqParameters.get(OAuth2ParameterNames.CLIENT_ID);


        // 调用baidu 根据授权码获取token
        BaiDuTokenResponse baiduTokenResponse = getAccessTokenResponse(code, BAIDU_ACCESS_TOKEN_URL);

        // 根据token获取用户信息
        BaiDuUserResponse baiduUserInfoResponse = getUserInfo(BAIDU_USERINFO_URL, clientId, baiduTokenResponse);

        // 储存或者更新baidu用户信息
        storebaiduUsers(baiduTokenResponse, baiduUserInfoResponse);

        UserDetails user = User.builder()
                .username(baiduUserInfoResponse.getOpenid())
                .roles("USER").build();

        return UsernamePasswordAuthenticationToken.authenticated(user, null,
                user.getAuthorities());
    }

    private UserDetails storebaiduUsers(BaiDuTokenResponse baiduTokenResponse, BaiDuUserResponse userInfo) {
        // 新增/更新从baidu获取的用户信息
        String accessToken = baiduTokenResponse.getAccessToken();
        String refreshToken = baiduTokenResponse.getRefreshToken();
        String scope = baiduTokenResponse.getScope();
        Integer expiresIn = baiduTokenResponse.getExpiresIn();
        LocalDateTime expires = LocalDateTime.now().plusSeconds(expiresIn);
        String openid = userInfo.getOpenid();
        String loginName = userInfo.getUsername();

        UserDetails userDetails = User.builder()
                .username(openid)
                .password("")
                .authorities(scope)
                .build();
        // 筛选出支持此客户端的UserDetailsService
        JdbcUserDetailsManager userDetailsManager = SpringUtil.getBean(JdbcUserDetailsManager.class);
        userDetailsManager.createUser(userDetails);

        return userDetails;
    }

    private BaiDuUserResponse getUserInfo(String userinfoUrl, String appid, BaiDuTokenResponse baiduTokenResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = baiduTokenResponse.getAccessToken();

        BaiDuUserResponse baiduUserInfoResponse = restTemplate.getForObject(userinfoUrl + "?access_token=" + accessToken,
                BaiDuUserResponse.class);

        return baiduUserInfoResponse;
    }

    private BaiDuTokenResponse getAccessTokenResponse(String code, String accessTokenUrl) {

        // 从配置获取 或者 从数据库获取
        BaiDuProperties baiduProperties = SpringUtil.getBean(BaiDuProperties.class);

        Map<String, String> uriVariables = new HashMap<>(8);

        uriVariables.put(OAuth2ParameterNames.CLIENT_ID, baiduProperties.getClientId());
        uriVariables.put(OAuth2ParameterNames.CODE, code);
        uriVariables.put(OAuth2ParameterNames.CLIENT_SECRET, baiduProperties.getClientSecret());
        uriVariables.put(OAuth2ParameterNames.REDIRECT_URI, baiduProperties.getRedirectUri());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 将请求体放入HttpEntity中
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);

        BaiDuTokenResponse baiduTokenResponse = restTemplate.postForObject(accessTokenUrl, httpEntity, BaiDuTokenResponse.class, uriVariables);

        return baiduTokenResponse;
    }
}
