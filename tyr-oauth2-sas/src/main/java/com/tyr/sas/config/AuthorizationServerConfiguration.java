package com.tyr.sas.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.tyr.sas.support.authorication.baidu.TyrBaiDuAuthenticationConverter;
import com.tyr.sas.support.authorication.baidu.TyrBaiDuAuthenticationProvider;
import com.tyr.sas.support.authorication.gitee.TyrGiteeAuthenticationConverter;
import com.tyr.sas.support.authorication.gitee.TyrGiteeAuthenticationProvider;
import com.tyr.sas.support.authorication.mail.TryMailAuthenticationConverter;
import com.tyr.sas.support.authorication.mail.TryMailAuthenticationProvider;
import com.tyr.sas.support.authorication.pwd.TyrPasswordAuthenticationConverter;
import com.tyr.sas.support.authorication.pwd.TyrPasswordAuthenticationProvider;
import com.tyr.sas.support.authorication.sms.TrySmsCodeAuthenticationConverter;
import com.tyr.sas.support.authorication.sms.TrySmsCodeAuthenticationProvider;

import com.tyr.sas.support.authorication.wechat.TyrWeChatAuthenticationConverter;
import com.tyr.sas.support.authorication.wechat.TyrWeChatAuthenticationProvider;
import com.tyr.sas.support.core.TyrDaoAuthenticationProvider;
import com.tyr.sas.support.core.TyrOAuth2TokenCustomizer;
import com.tyr.sas.support.core.TyrOAuth2TokenGenerator;
import com.tyr.sas.support.handler.TyrAccessDeniedHandler;
import com.tyr.sas.support.utils.OAuth2ConfigurerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Author：carroll
 */
@Configuration
public class AuthorizationServerConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 协议端点的Spring安全过滤器链。
    // 官方地址参考: https://docs.spring.io/spring-authorization-server/docs/0.4.2/reference/html/protocol-endpoints.html
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
                                                    .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                                                    .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0

        // 自定义授权码端点
        // OAuth2AuthorizationEndpointConfigurer提供了自定义OAuth2授权端点的功能。它定义了扩展点，使您可以自定义OAuth2授权请求的预处理、主处理和后处理逻辑。
       /* authorizationServerConfigurer
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint
                                .consentPage("/oauth2/v1/authorize")
                );*/

        // 自定义认证授权端点
        // OAuth2TokenEndpointConfigurer提供了自定义OAuth2令牌端点的功能。它定义了扩展点，使您可以自定义OAuth2访问令牌请求的预处理、主处理和后处理逻辑。
        authorizationServerConfigurer
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(accessTokenRequestConverter())); // 预处理 授权认证 Converter
                           //     .authenticationProvider(new DaoAuthenticationProvider())  // 主处理 授权模式实现提供方
                             //    .accessTokenResponseHandler(new TyrRequestAuthenticationSuccessHandler()) //后处理 登录成功
                             //    .errorResponseHandler(new TyrRequestAuthenticationFailureHandler())); //后处理  登录失败
        // 自定义客户端认证
        authorizationServerConfigurer
                .clientAuthentication(clientAuthentication ->
                       clientAuthentication
                               .errorResponseHandler(new ExceptionMappingAuthenticationFailureHandler()) // 客户端认证异常
                );

        // 自定义授权服务
        HttpSecurity httpSecurity = authorizationServerConfigurer
                // 授权服务设置
                .authorizationServerSettings(authorizationServerSettings())
                // 授权服务
                //.authorizationService(new InMemoryOAuth2AuthorizationService())
                .authorizationService(new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository()))
                .and()
                // 资源服务
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // 异常处理
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(new TyrAccessDeniedHandler())
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                );

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(httpSecurity);

        return httpSecurity.build();
    }

    // UserDetailsService的实例，用于检索要进行身份验证的用户。
    @Bean
    public UserDetailsService userDetailsService() {
        return new JdbcUserDetailsManager(jdbcTemplate.getDataSource());
    }

    // RegisteredClientRepository的实例，用于管理客户端。
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    // 用于签署访问令牌的com. nimbuss .jo .jwk.source. jwksource实例。
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    // 一个java.security.KeyPair的实例，它带有在启动时生成的密钥，用于创建上面的JWKSource。
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    // JwtDecoder的实例，用于解码已签名的访问令牌。
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        // 指定协议终结点的 URI 以及颁发者标识符
        return AuthorizationServerSettings.builder()
                .issuer("http://127.0.0.1:8080")
                .authorizationEndpoint("/oauth2/authorize")
                .tokenEndpoint("/oauth2/token")
                .jwkSetEndpoint("/oauth2/jwks")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .oidcClientRegistrationEndpoint("/connect/register")
                .oidcUserInfoEndpoint("/userinfo")
                .build();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义的授权认证Converter
     * @return
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                // 内置的刷新tokenConverter
                new OAuth2RefreshTokenAuthenticationConverter(),
                // 内置的客户端凭据Converter
                new OAuth2ClientCredentialsAuthenticationConverter(),
                // 内置的授权码Converter
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                // 内置的授权码请求Converter
                new OAuth2AuthorizationCodeRequestAuthenticationConverter(),
                // 自定义的gitee oauth2登录
                new TyrGiteeAuthenticationConverter(),
                // 自定义的验证码模式Converter
                new TrySmsCodeAuthenticationConverter(),
                // 自定义的密码模式Converter
                new TyrPasswordAuthenticationConverter(),
                // 微信登录
                new TyrWeChatAuthenticationConverter(),
                // 百度登录
                new TyrBaiDuAuthenticationConverter(),
                // 邮箱登录
                new TryMailAuthenticationConverter()
        ));
    }

    /**
     * 注入自定义授权模式实现提供方
     *
     * @return
     */
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(http);

        TyrPasswordAuthenticationProvider passwordAuthenticationProvider = new TyrPasswordAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        TrySmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new TrySmsCodeAuthenticationProvider(
                authenticationManager,authorizationService, tokenGenerator);

        TyrGiteeAuthenticationProvider giteeAuthenticationProvider = new TyrGiteeAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        TyrWeChatAuthenticationProvider weChatAuthenticationProvider = new TyrWeChatAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        TyrBaiDuAuthenticationProvider baiDuAuthenticationProvider = new TyrBaiDuAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        TryMailAuthenticationProvider mailAuthenticationProvider = new TryMailAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new TyrDaoAuthenticationProvider());
        // 处理 TyrPasswordAuthenticationToken
        http.authenticationProvider(passwordAuthenticationProvider);
        // 处理 TrySmsCodeAuthenticationToken
        http.authenticationProvider(smsCodeAuthenticationProvider);
        // 处理 TyrGiteeAuthenticationToken
        http.authenticationProvider(giteeAuthenticationProvider);
        //
        http.authenticationProvider(weChatAuthenticationProvider);
        //
        http.authenticationProvider(baiDuAuthenticationProvider);
        //
        http.authenticationProvider(mailAuthenticationProvider);

    }

    /**
     * 令牌生成规则实现
     * @return OAuth2TokenGenerator
     */
    /*@Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        TyrOAuth2TokenGenerator accessTokenGenerator = new TyrOAuth2TokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new TyrOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }*/

}
