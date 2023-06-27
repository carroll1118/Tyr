package com.tyr.core.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.tyr.core.conf.TyrSsoConf;
import com.tyr.core.support.entity.TyrSsoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author：carroll
 */
public abstract class TyrSsoFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TyrSsoFilter.class);

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    protected String ssoServer;
    protected String logoutPath;
    protected String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) {
        ssoServer = filterConfig.getInitParameter(TyrSsoConf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(TyrSsoConf.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(TyrSsoConf.SSO_EXCLUDED_PATHS);
        logger.info("TyrSsoFilter init.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // make url
        String servletPath = req.getServletPath();

        // excluded path check
        if (excludedPathCheck(servletPath)) {
            // excluded path, allow
            chain.doFilter(req, res);
            return;
        }

        // logout filter
        if (logoutPath!=null && logoutPath.trim().length()>0 && logoutPath.equals(servletPath)) {
            // logout
            logout(req,res);
            return;
        }

        // login filter
        TyrSsoUser tyrUser = loginCheck(req,res);
        if (tyrUser == null) {
            return;
        }
        // ser sso user
        request.setAttribute(TyrSsoConf.SSO_USER, tyrUser);

        // already login, allow
        chain.doFilter(request, response);
    }

    private boolean excludedPathCheck(String servletPath) {
        if (excludedPaths!=null && excludedPaths.trim().length()>0) {
            for (String excludedPath:excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();
                // 支持ANT表达式
                if (antPathMatcher.match(uriPattern, servletPath)) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     *
     * @param req
     * @param res
     */
    protected abstract void logout(HttpServletRequest req, HttpServletResponse res) throws IOException;

    /**
     * @param req
     * @param res
     * @return
     */
    protected abstract TyrSsoUser loginCheck(HttpServletRequest req, HttpServletResponse res) throws IOException;
}
