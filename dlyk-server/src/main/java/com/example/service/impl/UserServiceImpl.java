package com.example.service.impl;

import com.example.mapper.TUserMapper;
import com.example.model.TUser;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null){
            throw new UsernameNotFoundException("登录账号不存在");
        }
        return tUser;
    }
}
