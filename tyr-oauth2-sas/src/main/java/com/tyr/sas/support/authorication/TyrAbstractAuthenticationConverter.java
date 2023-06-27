package com.tyr.sas.support.authorication;

import com.tyr.sas.support.utils.OAuth2EndpointUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author：carroll
 * 自定义认证授权Converter抽象类  所有自定义Converter的都继承此类
 */
@Slf4j
public abstract class TyrAbstractAuthenticationConverter<T extends TyrAbstractAuthenticationToken> implements AuthenticationConverter {

    /**
     * 用于从 HttpServletRequest 转换为特定类型的身份验证。
     * 用于使用适当的身份验证管理器进行身份验证。如果结果为 null，则表示不应进行身份验证尝试。
     * 如果存在无效的身份验证方案，也可以在转换（HttpServletRequest）中抛出 AuthenticationException。
     * @param request
     * @return
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!support(grantType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        validParameters(parameters);

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            /*if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE) &&
                    !key.equals(OAuth2ParameterNames.REDIRECT_URI)) {

            }*/
            additionalParameters.put(key, value.get(0));
        });

        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));

        return getAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验参数
     * @param parameters
     */
    public void validParameters(MultiValueMap<String, String> parameters) {

    }
    /**
     * 获取当前Converter对应的AuthenticationToken
     * @param clientPrincipal
     * @param additionalParameters
     * @param requestedScopes
     * @return
     */
    public abstract T getAuthenticationToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);
    /**
     * 是否支持当前类型的授权认证
     * @param grantType  请求入参的授权模式
     * @return
     */
    public abstract boolean support(String grantType);
}