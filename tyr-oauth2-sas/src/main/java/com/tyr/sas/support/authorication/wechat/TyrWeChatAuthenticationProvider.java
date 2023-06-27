package com.tyr.sas.support.authorication.wechat;

import cn.hutool.extra.spring.SpringUtil;
import com.tyr.sas.support.authorication.TyrAbstractAuthenticationProvider;
import com.tyr.sas.support.authorication.gitee.TyrGiteeAuthenticationToken;
import com.tyr.sas.support.authorication.wechat.response.WeChatTokenResponse;
import com.tyr.sas.support.authorication.wechat.response.WeChatUserResponse;
import com.tyr.sas.support.properties.GiteeProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.tyr.sas.support.constant.SecurityConstants.*;

public class TyrWeChatAuthenticationProvider extends TyrAbstractAuthenticationProvider<TyrWeChatAuthenticationToken> {

    public TyrWeChatAuthenticationProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TyrGiteeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {

    }

    @Override
    public Authentication getAuthentication(Map<String, Object> reqParameters) {
        // 构造token返回
        // 授权码
        String code = (String) reqParameters.get(OAuth2ParameterNames.CODE);
        // 码云应用id
        String clientId = (String) reqParameters.get(OAuth2ParameterNames.CLIENT_ID);


        // 根据授权码获取token
        WeChatTokenResponse giteeTokenResponse = getAccessTokenResponse(clientId, code, WX_ACCESS_TOKEN_URL);

        // 根据token获取用户信息
        WeChatUserResponse giteeUserInfoResponse = getUserInfo(WX_USERINFO_URL, clientId, giteeTokenResponse);

        // 储存或者更新用户信息
        storeGiteeUsers(giteeTokenResponse, giteeUserInfoResponse);

        UserDetails user = User.builder()
                .username(giteeUserInfoResponse.getUnionid())
                .roles("USER").build();

        return UsernamePasswordAuthenticationToken.authenticated(user, null,
                user.getAuthorities());
    }

    private UserDetails storeGiteeUsers(WeChatTokenResponse giteeTokenResponse, WeChatUserResponse userInfo) {
        // 新增/更新从获取的用户信息
        String accessToken = giteeTokenResponse.getAccessToken();
        String refreshToken = giteeTokenResponse.getRefreshToken();
        String scope = giteeTokenResponse.getScope();
        Integer expiresIn = giteeTokenResponse.getExpiresIn();
        LocalDateTime expires = LocalDateTime.now().plusSeconds(expiresIn);
        String unionid = userInfo.getUnionid();
        String loginName = userInfo.getNickname();

        UserDetails userDetails = User.builder()
                .username(unionid)
                .password("")
                .authorities(scope)
                .build();
        // 筛选出支持此客户端的UserDetailsService
        JdbcUserDetailsManager userDetailsManager = SpringUtil.getBean(JdbcUserDetailsManager.class);
        userDetailsManager.createUser(userDetails);

        return userDetails;
    }

    private WeChatUserResponse getUserInfo(String userinfoUrl, String appid, WeChatTokenResponse giteeTokenResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = giteeTokenResponse.getAccessToken();

        WeChatUserResponse giteeUserInfoResponse = restTemplate.getForObject(userinfoUrl + "?access_token=" + accessToken,
                WeChatUserResponse.class);

        return giteeUserInfoResponse;
    }

    private WeChatTokenResponse getAccessTokenResponse(String appid, String code, String accessTokenUrl) {

        GiteeProperties giteeProperties = SpringUtil.getBean(GiteeProperties.class);

        Map<String, String> uriVariables = new HashMap<>(8);

        uriVariables.put(OAuth2ParameterNames.CLIENT_ID, giteeProperties.getClientId());
        uriVariables.put(OAuth2ParameterNames.CODE, code);
        uriVariables.put(OAuth2ParameterNames.CLIENT_SECRET, giteeProperties.getClientSecret());
        uriVariables.put(OAuth2ParameterNames.REDIRECT_URI, giteeProperties.getRedirectUri());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 将请求体放入HttpEntity中
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);

        WeChatTokenResponse giteeTokenResponse = restTemplate.postForObject(accessTokenUrl, httpEntity, WeChatTokenResponse.class, uriVariables);

        return giteeTokenResponse;
    }
}
