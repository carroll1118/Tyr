package com.tyr.core.support.helper;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;

import javax.servlet.http.HttpServletRequest;

public class TyrTokenLoginHelper {

    /**
     * client login
     *
     * @param sessionId
     * @param ssoUser
     */
    public static void login(String sessionId, TyrSsoUser ssoUser) {

        String storeKey = TyrSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        TyrSsoLoginStore.put(storeKey, ssoUser);
    }

    /**
     * client logout
     *
     * @param sessionId
     */
    public static void logout(String sessionId) {

        String storeKey = TyrSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }

        TyrSsoLoginStore.remove(storeKey);
    }
    /**
     * client logout
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(TyrSsoConf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * login check
     *
     * @param sessionId
     * @return
     */
    public static TyrSsoUser loginCheck(String  sessionId){

        String storeKey = TyrSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }

        TyrSsoUser ssoUser = TyrSsoLoginStore.get(storeKey);
        if (ssoUser != null) {
            String version = TyrSessionIdHelper.parseVersion(sessionId);
            if (ssoUser.getVersion().equals(version)) {

                // After the expiration time has passed half, Auto refresh
                if ((System.currentTimeMillis() - ssoUser.getExpireFreshTime()) > ssoUser.getExpireMinute()/2) {
                    ssoUser.setExpireFreshTime(System.currentTimeMillis());
                    TyrSsoLoginStore.put(storeKey, ssoUser);
                }

                return ssoUser;
            }
        }
        return null;
    }


    /**
     * login check
     *
     * @param request
     * @return
     */
    public static TyrSsoUser loginCheck(HttpServletRequest request){
        String headerSessionId = request.getHeader(TyrSsoConf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }


}
