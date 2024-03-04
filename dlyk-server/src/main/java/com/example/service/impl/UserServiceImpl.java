package com.example.service.impl;

import com.example.constant.Constants;
import com.example.manager.RedisManager;
import com.example.mapper.TPermissionMapper;
import com.example.mapper.TRoleMapper;
import com.example.mapper.TUserMapper;
import com.example.model.TUser;
import com.example.query.UserQuery;
import com.example.service.UserService;
import com.example.utils.JWTUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private TUserMapper tUserMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TRoleMapper tRoleMapper;
    @Resource
    private RedisManager redisManager;
    @Resource
    private TPermissionMapper tPermissionMapper;

    @Override
    public int saveUser(UserQuery userQuery) {
        TUser tUser = new TUser();

        //把UserQuery对象里面的属性数据复制到TUser对象里面去(复制要求：两个对象的属性名相同，属性类型要相同，这样才能复制)
        BeanUtils.copyProperties(userQuery, tUser);

        tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd())); //密码加密
        tUser.setCreateTime(new Date()); //创建时间

        //登录人的id
        Integer loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        tUser.setCreateBy(loginUserId); //创建人

        return tUserMapper.insertSelective(tUser);
    }

    @Override
    public int updateUser(UserQuery userQuery) {
        return 0;
    }

    @Override
    public int delUserById(Integer id) {
        return 0;
    }

    @Override
    public int batchDelUserIds(List<String> idList) {
        return 0;
    }

    @Override
    public List<TUser> getOwnerList() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登录账号不存在");
        }
        return tUser;
    }

    @Override
    public PageInfo<TUser> getUserByPage(Integer current) {
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


}
