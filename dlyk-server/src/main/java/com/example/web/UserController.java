package com.example.web;

import com.example.model.TUser;
import com.example.result.R;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping(value = "/api/login/info")
    public R loginInfo(Authentication authentication){
        TUser tUser =  (TUser) authentication.getPrincipal();
        return R.OK(tUser);
    }
}
