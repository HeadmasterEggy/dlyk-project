package com.example.service;

import com.example.model.TUser;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(Integer current);

    TUser getUserById(Integer id);
}
