package com.gjc.configuration;

import com.gjc.interceptor.LoginRequiredInterceptor;
import com.gjc.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 把拦截器注册到MVC里面去
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    //可以把拦截器加进来
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截全局的页面
        registry.addInterceptor(passportInterceptor);
        //拦截关于/setting*的页面,只访问/setting*页面的时候才调用拦截器，否则不调用
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
