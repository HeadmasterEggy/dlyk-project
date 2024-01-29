package com.example.config.filter;

import com.example.constant.Constants;
import com.example.model.TUser;
import com.example.result.CodeEnum;
import com.example.result.R;
import com.example.service.RedisService;
import com.example.utils.JSONUtils;
import com.example.utils.JWTUtils;
import com.example.utils.ResponseUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class TokenVerifyFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals(Constants.LOGIN_URI)) {
            //登录的时候还没有生成jwt，此时不需要验证jwt，直接放行即可，可以让filter链继续执行，执行下一个filter
            filterChain.doFilter(request, response);

        } else {
            //拿到前端传过来的jwt（token），这个jwt一般都通过header传过来
            String token = request.getHeader("Authorization");
            if (!StringUtils.hasText(token)) {
                R result = R.FAIL(CodeEnum.TOKEN_IS_EMPTY);

                //把对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把R以json返回前端
                ResponseUtils.write(response, resultJSON);

                return;
            }

            //验证token是否被篡改过
            if (!JWTUtils.verifyJWT(token)) {
                R result = R.FAIL(CodeEnum.TOKEN_IS_ERROR);

                //把对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把R以json返回前端
                ResponseUtils.write(response, resultJSON);

                return;
            }


            //怎么拿到用户的id？反写解析jwt，从jwt中解析出用户的id
            TUser tUser = JWTUtils.parseUserFromJWT(token);
            Integer userId = tUser.getId();
            String redisToken = (String) redisService.getValue(Constants.REDIS_JWT_KEY + tUser.getId());
            if (!StringUtils.hasText(redisToken)) {
                //token验证未通过统一结果
                R result = R.FAIL(CodeEnum.TOKEN_IS_EXPIRED);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            if (!token.equals(redisToken)) {
                //token验证未通过统一结果
                R result = R.FAIL(CodeEnum.TOKEN_IS_NOT_MATCH);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //给token续期
            String rememberMe = request.getHeader(Constants.REMEMBERME_NAME); //true, null
            if (Boolean.parseBoolean(rememberMe)) {
                //续期7天
                redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.EXPIRE_TIME, TimeUnit.MINUTES);
            } else {
                //续期30分钟
                redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
            }

            //都验证通过了，没有问题了，需要告诉spring security框架，这样spring security框架才知道该jwt是已经登录过的
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tUser, tUser.getLoginPwd(), tUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            //下面就是filter链继续执行，执行下一个filter
            filterChain.doFilter(request, response);
        }
    }
}
