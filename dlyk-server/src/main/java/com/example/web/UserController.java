package com.example.web;

import com.example.model.TUser;
import com.example.query.UserQuery;
import com.example.result.R;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 用户列表分页查询
     *
     * @param current
     * @return
     */
    @PreAuthorize(value = "hasAuthority('user:list')")
    @GetMapping(value = "/api/users")
    public R userPage(@RequestParam(value = "current", required = false) Integer current) {
        //required = false 传参数 optional, true 传参数 must
        if (current == null) {
            current = 1;
        }
        PageInfo<TUser> pageInfo = userService.getUserByPage(current);
        return R.OK(pageInfo);
    }

    @PreAuthorize(value = "hasAuthority('user:view')")
    @GetMapping(value = "/api/user/{id}")
    public R userDetail(@PathVariable(value = "id") Integer id) {
        TUser tuser = userService.getUserById(id);
        return R.OK(tuser);
    }

    /**
     * 新增用户
     *
     * @param userQuery
     * @return
     */
    @PreAuthorize(value = "hasAuthority('user:add')")
    @PostMapping(value = "/api/user")
    public R addUser(UserQuery userQuery, @RequestHeader(value = "Authorization") String token) {
        userQuery.setToken(token);
        int save = userService.saveUser(userQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 编辑用户
     *
     * @param userQuery
     * @return
     */
    @PreAuthorize(value = "hasAuthority('user:edit')")
    @PutMapping(value = "/api/user")
    public R editUser(UserQuery userQuery, @RequestHeader(value = "Authorization") String token) {
        userQuery.setToken(token);
        int update = userService.updateUser(userQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @PreAuthorize(value = "hasAuthority('user:delete')")
    @DeleteMapping(value = "/api/user/{id}")
    public R delUser(@PathVariable(value = "id") Integer id) {
        int del = userService.delUserById(id);
        return del >= 1 ? R.OK() : R.FAIL();
    }
}
