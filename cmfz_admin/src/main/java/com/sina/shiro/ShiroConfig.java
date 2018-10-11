package com.sina.shiro;

import com.sina.realm.MyRealm;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.tools.ant.filters.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
     @Bean
     //创建安全过滤器工厂
    public ShiroFilterFactoryBean createShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
         ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
         //在springmvc中需要注入 <property>
         //在于springboot的整合中  需要通过创建对象的方式 来映射关系 注入就相当于是set方法
         //需注入安全管理器 注入的东西以方法参数的形式传递
         shiroFilterFactoryBean.setSecurityManager(securityManager);
            //定制拦截器链
         LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
         map.put("/main/main.jsp","user");
         shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
         return shiroFilterFactoryBean;
     }
     @Bean
     //创建安全管理器对象
    public DefaultWebSecurityManager createSecurityManager(Realm createRealm,CookieRememberMeManager cookieRememberMeManager){
         DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
         defaultWebSecurityManager.setRealm(createRealm);
         defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager);
         return defaultWebSecurityManager;
     }
     //创建自定义reamle数据源信息
    @Bean
    public Realm createRealm(CredentialsMatcher credentialsMatcher){
        MyRealm realm = new MyRealm();
        //注意 ：设置复杂的凭证匹配器 在自定义的realm里注入
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }
    //创建复杂的凭证认证器
    @Bean
    public CredentialsMatcher createHashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        return hashedCredentialsMatcher;
    }
    //创建"记住我"的管理器
    @Bean
    public CookieRememberMeManager createCookieRememberMeManager(Cookie cookie){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }
    //创建cookie
    @Bean
    public Cookie createCookie(){
        SimpleCookie cookie = new SimpleCookie();
        cookie.setMaxAge(604800);
        cookie.setPath("/");
        cookie.setName("oo");
        return cookie;
    }
    //配置shiro的注解式配置
   /* @Bean
    public AuthorizationAttributeSourceAdvisor createAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }*/
   //缓存管理器
   @Bean
    public CacheManager createCacheManager(){
       EhCacheManager manager = new EhCacheManager();
       return manager;
   }
}
