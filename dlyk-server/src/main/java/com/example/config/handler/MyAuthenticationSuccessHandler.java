package com.example.config.handler;


import com.example.constant.Constants;
import com.example.model.TUser;
import com.example.result.R;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.utils.JSONUtils;
import com.example.utils.JWTUtils;
import com.example.utils.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //由于禁用了session，我们在登录成功后，需要在服务器保持用户的登录状态，前端下次来访问服务器端的时候，服务器端要知道这个人登录了
        TUser tUser = (TUser) authentication.getPrincipal();

        String userJSON = JSONUtils.toJSON(tUser);
        String jwt = JWTUtils.createJWT(userJSON);

        //2、jwt字符串写入redis
        Integer userId = tUser.getId();
        redisService.setValue(Constants.REDIS_JWT_KEY + userId, jwt); //要设置jwt不同的过期时间，选择记住我是7天过期，否则是30分钟过期

        //设置jwt过期时间
        String rememberMe = request.getParameter("rememberMe");
        if (Boolean.parseBoolean(rememberMe)) {
            redisService.expire(Constants.REDIS_JWT_KEY + tUser.getId(), Constants.EXPIRE_TIME, TimeUnit.SECONDS);
        } else {
            redisService.expire(Constants.REDIS_JWT_KEY + tUser.getId(), Constants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        R result = R.OK(jwt);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
