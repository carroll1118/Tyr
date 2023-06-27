package com.tyr.core.support.helper;

import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import com.tyr.core.support.util.JedisUtil;

/**
 * local login store
 *
 */
public class TyrSsoLoginStore {

    private static int redisExpireMinute = 1440;
    public static void setRedisExpireMinute(int redisExpireMinute) {
        if (redisExpireMinute < 30) {
            redisExpireMinute = 30;
        }
        TyrSsoLoginStore.redisExpireMinute = redisExpireMinute;
    }
    public static int getRedisExpireMinute() {
        return redisExpireMinute;
    }

    /**
     * get
     *
     * @param storeKey
     * @return
     */
    public static TyrSsoUser get(String storeKey) {

        String redisKey = redisKey(storeKey);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            TyrSsoUser tyrUser = (TyrSsoUser) objectValue;
            return tyrUser;
        }
        return null;
    }

    /**
     * remove
     *
     * @param storeKey
     */
    public static void remove(String storeKey) {
        String redisKey = redisKey(storeKey);
        JedisUtil.del(redisKey);
    }

    /**
     * put
     *
     * @param storeKey
     * @param tyrUser
     */
    public static void put(String storeKey, TyrSsoUser tyrUser) {
        String redisKey = redisKey(storeKey);
        JedisUtil.setObjectValue(redisKey, tyrUser, redisExpireMinute * 60);  // minute to second
    }

    private static String redisKey(String sessionId){
        return TyrSsoConf.SSO_SESSIONID.concat("#").concat(sessionId);
    }

}
