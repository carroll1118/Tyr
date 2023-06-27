package com.tyr.core.support.helper;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.support.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TyrWebLoginHelper {

    /**
     * client login
     *
     * @param response
     * @param sessionId
     * @param ifRemember    true: cookie not expire, false: expire when browser close （server cookie）
     * @param tyrUser
     */
    public static void login(HttpServletResponse response,
                             String sessionId,
                             TyrSsoUser tyrUser,
                             boolean ifRemember) {

        String storeKey = TyrSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        TyrSsoLoginStore.put(storeKey, tyrUser);
        CookieUtil.set(response, TyrSsoConf.SSO_SESSIONID, sessionId, ifRemember);
    }

    /**
     * client logout
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request,
                              HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, TyrSsoConf.SSO_SESSIONID);
        if (cookieSessionId==null) {
            return;
        }

        String storeKey = TyrSessionIdHelper.parseStoreKey(cookieSessionId);
        if (storeKey != null) {
            TyrSsoLoginStore.remove(storeKey);
        }

        CookieUtil.remove(request, response, TyrSsoConf.SSO_SESSIONID);
    }



    /**
     * login check
     *
     * @param request
     * @param response
     * @return
     */
    public static TyrSsoUser loginCheck(HttpServletRequest request, HttpServletResponse response){

        String cookieSessionId = CookieUtil.getValue(request, TyrSsoConf.SSO_SESSIONID);

        // cookie user
        TyrSsoUser tyrUser = TyrTokenLoginHelper.loginCheck(cookieSessionId);
        if (tyrUser != null) {
            return tyrUser;
        }

        // redirect user

        // remove old cookie
        TyrWebLoginHelper.removeSessionIdByCookie(request, response);

        // set new cookie
        String paramSessionId = request.getParameter(TyrSsoConf.SSO_SESSIONID);
        tyrUser = TyrTokenLoginHelper.loginCheck(paramSessionId);
        if (tyrUser != null) {
            CookieUtil.set(response, TyrSsoConf.SSO_SESSIONID, paramSessionId, false);    // expire when browser close （client cookie）
            return tyrUser;
        }

        return null;
    }


    /**
     * client logout, cookie only
     *
     * @param request
     * @param response
     */
    public static void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, TyrSsoConf.SSO_SESSIONID);
    }

    /**
     * get sessionid by cookie
     *
     * @param request
     * @return
     */
    public static String getSessionIdByCookie(HttpServletRequest request) {
        String cookieSessionId = CookieUtil.getValue(request, TyrSsoConf.SSO_SESSIONID);
        return cookieSessionId;
    }


}
