package com.tyr.core.filter.web;


import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.filter.TyrSsoFilter;
import com.tyr.core.support.helper.TyrWebLoginHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author：carroll
 * web sso filter
 */
public class TyrSsoWebFilter extends TyrSsoFilter {

    @Override
    protected void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // remove cookie
        TyrWebLoginHelper.removeSessionIdByCookie(req, res);
        // redirect logout
        String logoutPageUrl = ssoServer.concat(TyrSsoConf.SSO_LOGOUT);
        res.sendRedirect(logoutPageUrl);
    }

    @Override
    protected TyrSsoUser loginCheck(HttpServletRequest req, HttpServletResponse res) throws IOException {
        TyrSsoUser ssoUser = TyrWebLoginHelper.loginCheck(req, res);

        // valid login fail
        if (ssoUser == null) {

            String header = req.getHeader("content-type");
            boolean isJson=  header!=null && header.contains("json");
            if (isJson) {
                // json msg
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println("{\"code\":"+"500"+", \"msg\":\""+ "sso not login." +"\"}");
            } else {

                // total link
                String link = req.getRequestURL().toString();

                // redirect logout
                String loginPageUrl = ssoServer.concat(TyrSsoConf.SSO_LOGIN)
                        + "?" + TyrSsoConf.REDIRECT_URL + "=" + link;

                res.sendRedirect(loginPageUrl);
            }
            return null;
        }
        return ssoUser;
    }
}
