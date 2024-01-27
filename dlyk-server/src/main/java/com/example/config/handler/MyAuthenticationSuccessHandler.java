package com.example.config.handler;


import com.example.model.TUser;
import com.example.result.R;
import com.example.service.UserService;
import com.example.utils.JSONUtils;
import com.example.utils.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //由于禁用了session，我们在登录成功后，需要在服务器保持用户的登录状态，前端下次来访问服务器端的时候，服务器端要知道这个人登录了
        TUser tUser = (TUser) authentication.getPrincipal();

        R result = R.OK(authentication);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
