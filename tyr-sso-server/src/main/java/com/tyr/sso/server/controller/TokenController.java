package com.tyr.sso.server.controller;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.support.helper.TyrTokenLoginHelper;
import com.tyr.core.support.helper.TyrSessionIdHelper;
import com.tyr.core.support.helper.TyrSsoLoginStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;
import java.util.UUID;

/**
 * sso server (for app)
 */
@Controller
@RequestMapping
public class TokenController {

    /**
     * Login
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(TyrSsoConf.SSO_APP_LOGIN)
    public String login(String username, String password) {

        // valid login
        if (!Objects.equals(username,"admin") || !Objects.equals(password,"123456")) {
            // 登录失败
            return "redirect:/login";
        }


        // 1、make ssoUser
        TyrSsoUser ssoUser = new TyrSsoUser();
        ssoUser.setUserid("123456789");
        ssoUser.setUsername(username);
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinute(TyrSsoLoginStore.getRedisExpireMinute());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());


        String sessionId = TyrSessionIdHelper.makeSessionId(ssoUser);

        TyrTokenLoginHelper.login(sessionId, ssoUser);

        return "redirect:/";
    }


    /**
     * Logout
     *
     * @param sessionId
     * @return
     */
    @RequestMapping(TyrSsoConf.SSO_APP_LOGOUT)
    public String logout(String sessionId) {
        TyrTokenLoginHelper.logout(sessionId);
        return "redirect:/login";
    }

    /**
     * logincheck
     *
     * @param sessionId
     * @return
     */
    @RequestMapping("/logincheck")
    @ResponseBody
    public TyrSsoUser logincheck(String sessionId) {
        // logout
        TyrSsoUser ssoUser = TyrTokenLoginHelper.loginCheck(sessionId);
        if (ssoUser == null) {
            return new TyrSsoUser();
        }
        return ssoUser;
    }

}
