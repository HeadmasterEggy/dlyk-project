package com.example.service;

import com.example.model.TUser;

import com.example.query.UserQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserByPage(Integer current);

    TUser getUserById(Integer id);

    int saveUser(UserQuery userQuery);

    int updateUser(UserQuery userQuery);

    int delUserById(Integer id);

    int batchDelUserIds(List<String> idList);

    List<TUser> getOwnerList();
}
