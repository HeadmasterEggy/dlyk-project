package com.example.web;

import com.example.model.TUser;
import com.example.result.R;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取登录信息
     *
     * @param authentication
     * @return
     */
    @GetMapping(value = "/api/login/info")
    public R loginInfo(Authentication authentication) {
        TUser tUser = (TUser) authentication.getPrincipal();
        return R.OK(tUser);
    }

    /**
     * 免登录
     *
     * @return
     */
    @GetMapping(value = "/api/login/free")
    public R freeLogin() {
        return R.OK();
    }

    @GetMapping(value = "/api/users")
    public R userPage(@RequestParam(value = "current", required = false) Integer current){
        //required = false 传参数 optional, true 传参数 must
        if(current == null){
            current = 1;
        }
        PageInfo<TUser> pageInfo = userService.getUserByPage(current);
        return R.OK(pageInfo);
    }

    @GetMapping(value = "/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id){
        TUser tuser = userService.getUserById(id);
        return R.OK(tuser);
    }
}
