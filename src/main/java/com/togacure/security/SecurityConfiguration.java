package com.togacure.security;

import java.util.LinkedHashMap;
import java.util.Objects;
import lombok.val;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vitaly Alekseev
 * @since 10.02.2021
 */
@Configuration
public class SecurityConfiguration {

    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    @Bean
    public ShiroFilterFactoryBean shirFilter(final org.apache.shiro.mgt.SecurityManager securityManager) {
        val shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        val filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/VAADIN/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/robots.txt", "anon");
        filterChainDefinitionMap.put("/manifest.webmanifest", "anon");
        filterChainDefinitionMap.put("/sw.js", "anon");
        filterChainDefinitionMap.put("/offline-page.html", "anon");
        filterChainDefinitionMap.put("/icons/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/frontend/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/h2-console/**", "anon");
        filterChainDefinitionMap.put("/frontend-es5/**", "anon");
        filterChainDefinitionMap.put("/frontend-es6/**", "anon");

        filterChainDefinitionMap.put(LOGOUT_URL, "logout");

        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setLoginUrl(LOGIN_URL);

        shiroFilterFactoryBean.setSuccessUrl("/");

        shiroFilterFactoryBean.setUnauthorizedUrl(LOGIN_FAILURE_URL);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;

    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        val securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(new SimpleRealm());
        return securityManager;
    }

    private static class SimpleRealm implements Realm {

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public boolean supports(final AuthenticationToken authenticationToken) {
            return authenticationToken instanceof UsernamePasswordToken;
        }

        @Override
        public AuthenticationInfo getAuthenticationInfo(final AuthenticationToken authenticationToken) throws AuthenticationException {
            val token = (UsernamePasswordToken) authenticationToken;
            if (token.getUsername() != null
                    && !token.getUsername().isEmpty()
                    && Objects.equals(token.getUsername(), "test")) {
                return new SimpleAuthenticationInfo(
                        token.getUsername(),
                        "test",
                        getName());
            }
            throw new UnknownAccountException("username hasn't been found!");
        }
    }

}
