package com.tyr.sso.client.controller;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    public TyrSsoUser index(HttpServletRequest request) {
        TyrSsoUser ssoUser = (TyrSsoUser) request.getAttribute(TyrSsoConf.SSO_USER);
        return ssoUser;
    }

}