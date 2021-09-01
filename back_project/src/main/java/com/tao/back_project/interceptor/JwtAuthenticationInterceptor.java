package com.tao.back_project.interceptor;

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
