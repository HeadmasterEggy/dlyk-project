package com.example.service.impl;

import com.example.mapper.TUserMapper;
import com.example.model.TUser;
import com.example.query.UserQuery;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.constant.Constants;
import java.util.List;

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

    @Override
    public PageInfo<TUser> getUserByPage(Integer current){
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TUser> list = tUserMapper.selectUserByPage();
        //3.封装分页数据到PageInfo
        return new PageInfo<>(list);
    }

    @Override
    public TUser getUserById(Integer id) {
        return tUserMapper.selectDetailById(id);
    }

    @Override
    public int saveUser(UserQuery userQuery) {

        TUser tUser = new TUser();

        //把UserQuery对象里面的属性复制到TUser对象里面
        BeanUtils.copyProperties(userQuery, tUser);

        return tUserMapper.insertSelective(tUser);
    }
}
