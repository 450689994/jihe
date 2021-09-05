# 项目要求

* 前端采用 vue-webpack 搭建，UI 采用 elementUI 完成
* 后台使用 springboot、mybatis、mysql、redis、jwt
* 系统开发完成后，部署在 linux 环境下，服务均要部署到 docker 容器中；
  * 前端采用 nginx 部署；
  * Redis、mysql 也均使用 docker 部署

# 运行效果

* 用户打开登录界面，可点击注册，输入用户名、密码完成用户注册。
* 用户注册时，每当用户名发生改变时，就会进行重名校验，重名就会提示“**用户名已存在**”
* 用户登录成功后，跳转到首界面。
* jwt有效期为30s，30s之后的请求会被拦截，提示用户“**重新登录**”

# SQL代码

```sql
create database myproject;

use myproject;

-- 创建user表
create table user(
 `uid` int primary key auto_increment,
 `username` varchar(20) not null ,
 `password` varchar(32) not null
);
-- 插入数据，md5的密码（123）
insert into `user`(username, password) values('chen','202cb962ac59075b964b07152d234b70');
-- 插入数据，md5的密码（abc）
insert into `user`(username, password) values('hou','900150983CD24FB0D6963F7D28E17F72');

select * from user;
```

# 功能实现

* 登录
* 注册
* jwt

## jwt逻辑

* 判断`header`中有没有`token`
* `token`是加密规则
* `token`是否在有效期之内

满足上述要求通过校验，反之错误响应。

## 登录逻辑

* 先在redis中找
* 再在mysql中找，进行密码校验
* 之后再存放在redis中

## 注册逻辑

* 验证合法性，避免用户关闭浏览器`javascript`功能
* 每当用户名输入框发生改变时，就会进行重名校验：
  * 先在redis中找，有就直接false，无再去mysql
  * mysql中找找到了，直接false，找不到就显示“用户名可用”
* 注册成功写入数据库
* 之后再存放在redis中



# 问题汇总

* jwt解决方案
* 数据库连接池问题
* 跨域问题

## jwt解决方案

由后端`springboot`拦截器，拦截所有请求。当接口被自定义`@PassToken`注解所标记，就不通过`jwt`校验，相反没有被注解标记的接口就必须经过`jwt`校验，**校验规则**：

* 判断header中有没有token
* token是加密规则
* token是否在有效期之内

#### 自定义`@PassToken`注解：

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
    boolean required() default true;
}
```

#### 配置`JwtInterceptorConfig`拦截器：

```java
import com.tao.back_project.interceptor.JwtAuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //默认拦截所有路径
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public JwtAuthenticationInterceptor authenticationInterceptor() {
        return new JwtAuthenticationInterceptor();
    }
}
```

#### 自定义`JwtAuthenticationInterceptor`拦截器

```java
import com.google.gson.Gson;
import com.tao.back_project.annotation.PassToken;
import com.tao.back_project.dao.UserRepository;
import com.tao.back_project.util.JwtUtils;
import com.tao.back_project.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查是否有passtoken注释，有则不用验证，直接跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
            return false;
        }
        //默认全部检查
        else {
            //压根没token
            if (token == null ||
                    //token验证失败了
                    !JwtUtils.verifyToken(token, JwtUtils.getAudience(token)) ||
                    //登录超时
                    !JwtUtils.isEffective(token)) {
                //返回一个JSON给前端
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                Result result = new Result(200, false, "token验证失败", token);
                out.append(new Gson().toJson(result));
                return false;
            }
            //放行
            return true;
        }
    }
}
```



## 连接池问题

一开始使用的是`springboot`默认的`HikariPool`数据库连接池，有超时连接问题需要设置，由于对`HikariPool`不太了解，还是换用`c3p0`，在`application.yaml`进行如下配置：

```yaml
  maxIdleTime: 0   #连接的最大空闲时间，0表示无限长
  MaxConnectionAge: 0
```

## 跨域问题

一开始在部署之前是用前端`vue-cli`自带的`vue.config.js`中配置`proxy`解决问题，但是后来部署到服务器之后发现不管用，然后又在后端通过配置`CorsFilter`过滤器解决了问题。但是要**注意跨域问题只能存在一个解决方式，不能有两个以上**。配置如下：

```java
package com.tao.back_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCORS {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 可以自行筛选
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
```

