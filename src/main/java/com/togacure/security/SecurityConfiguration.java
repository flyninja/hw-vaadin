package com.togacure.security;

import com.vaadin.flow.shared.ApplicationConstants;
import java.util.LinkedHashMap;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.val;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
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
    public ShiroFilterFactoryBean shiroFilterFactoryBean(final org.apache.shiro.mgt.SecurityManager securityManager) {
        val shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        val filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/VAADIN/**", DefaultFilter.anon.name());

        filterChainDefinitionMap.put(LOGOUT_URL, DefaultFilter.logout.name());

        filterChainDefinitionMap.put("/**", DefaultFilter.authc.name());

        shiroFilterFactoryBean.setLoginUrl(LOGIN_URL);

        shiroFilterFactoryBean.setSuccessUrl("/");

        shiroFilterFactoryBean.setUnauthorizedUrl(LOGIN_FAILURE_URL);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        val filters = new LinkedHashMap<String, Filter>();
        val authc = new FormAuthenticationFilter() {
            @Override
            protected boolean isAccessAllowed(final ServletRequest request, final ServletResponse response, final Object mappedValue) {
                return request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER) != null ? true : super.isAccessAllowed(request, response, mappedValue);
            }
        };
        authc.setLoginUrl(LOGIN_URL);
        authc.setSuccessUrl("/");
        filters.put(DefaultFilter.authc.name(), authc);
        shiroFilterFactoryBean.setFilters(filters);

        return shiroFilterFactoryBean;

    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(final Realm simpleRealm) {
        return new DefaultWebSecurityManager(simpleRealm);
    }

    @Bean
    public Realm simpleRealm() {
        return new SimpleRealm();
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
