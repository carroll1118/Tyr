package com.tyr.core.filter.token;


import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.filter.TyrSsoFilter;
import com.tyr.core.support.helper.TyrTokenLoginHelper;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author：carroll
 * app sso filter
 */
public class TyrSsoTokenFilter extends TyrSsoFilter {

    @Override
    protected void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // logout
        TyrTokenLoginHelper.logout(req);
        // redirect logout
        String logoutPageUrl = ssoServer.concat(TyrSsoConf.SSO_APP_LOGOUT);
        res.sendRedirect(logoutPageUrl);
        // response
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().println("{\"code\":"+ "00000" +", \"msg\":\"\"}");
    }

    @Override
    protected TyrSsoUser loginCheck(HttpServletRequest req, HttpServletResponse res) throws IOException {

        TyrSsoUser ssoUser = TyrTokenLoginHelper.loginCheck(req);
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
                String loginPageUrl = ssoServer.concat(TyrSsoConf.SSO_APP_LOGIN)
                        + "?" + TyrSsoConf.REDIRECT_URL + "=" + link;
                res.sendRedirect(loginPageUrl);
            }
            return null;
        }
        return ssoUser;
    }
}
