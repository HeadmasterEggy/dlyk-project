package com.example.config.handler;

import com.example.model.TUser;
import com.example.result.CodeEnum;
import com.example.result.R;
import com.example.utils.JSONUtils;
import com.example.utils.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //用户退出登录，那么把redis中的jwt删除
        TUser tUser = (TUser) authentication.getPrincipal();


        //执行到这里，说明退出成功，那我们向前端返回json就行了
        R result = R.OK(CodeEnum.OK);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}