package com.tyr.sso.server.controller;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.support.helper.TyrWebLoginHelper;
import com.tyr.core.support.helper.TyrSessionIdHelper;
import com.tyr.core.support.helper.TyrSsoLoginStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * sso server (for web)
 */
@Controller
public class WebController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        TyrSsoUser tyrUser = TyrWebLoginHelper.loginCheck(request, response);

        if (tyrUser == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("tyrUser", tyrUser);
            return "index";
        }
    }

    /**
     * Login page
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(TyrSsoConf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        TyrSsoUser ssoUser = TyrWebLoginHelper.loginCheck(request, response);

        if (ssoUser != null) {

            // success redirect
            String redirectUrl = request.getParameter(TyrSsoConf.REDIRECT_URL);
            if (redirectUrl!=null && redirectUrl.trim().length()>0) {

                String sessionId = TyrWebLoginHelper.getSessionIdByCookie(request);
                String redirectUrlFinal = redirectUrl + "?" + TyrSsoConf.SSO_SESSIONID + "=" + sessionId;;

                return "redirect:" + redirectUrlFinal;
            } else {
                return "redirect:/";
            }
        }

        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(TyrSsoConf.REDIRECT_URL, request.getParameter(TyrSsoConf.REDIRECT_URL));
        return "login";
    }

    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes,
                        String username,
                        String password,
                        String ifRemember) {

        boolean ifRem = (ifRemember!=null&&"on".equals(ifRemember))?true:false;

        // valid login
        //ReturnT<UserInfo> result = userService.findUser(username, password);
        if (!Objects.equals(username,"admin") || !Objects.equals(password,"123456")) {
            redirectAttributes.addAttribute("errorMsg", "密码错误");

            redirectAttributes.addAttribute(TyrSsoConf.REDIRECT_URL, request.getParameter(TyrSsoConf.REDIRECT_URL));
            // 登录失败
            return "redirect:/login";
        }

        // 1、make tyr-sso user
        TyrSsoUser tyrUser = new TyrSsoUser();
        tyrUser.setUserid("10000");
        tyrUser.setUsername(username);
        tyrUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        tyrUser.setExpireMinute(TyrSsoLoginStore.getRedisExpireMinute());
        tyrUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、make session id
        String sessionId = TyrSessionIdHelper.makeSessionId(tyrUser);

        // 3、login, store storeKey + cookie sessionId
        TyrWebLoginHelper.login(response, sessionId, tyrUser, ifRem);

        // 4、return, redirect sessionId
        String redirectUrl = request.getParameter(TyrSsoConf.REDIRECT_URL);
        if (redirectUrl!=null && redirectUrl.trim().length()>0) {
            String redirectUrlFinal = redirectUrl + "?" + TyrSsoConf.SSO_SESSIONID + "=" + sessionId;
            return "redirect:" + redirectUrlFinal;
        } else {
            return "redirect:/";
        }

    }

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(TyrSsoConf.SSO_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        // logout
        TyrWebLoginHelper.logout(request, response);

        redirectAttributes.addAttribute(TyrSsoConf.REDIRECT_URL, request.getParameter(TyrSsoConf.REDIRECT_URL));
        return "redirect:/login";
    }


}
