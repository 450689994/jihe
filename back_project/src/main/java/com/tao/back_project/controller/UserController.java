package com.tao.back_project.controller;

import com.tao.back_project.annotation.PassToken;
import com.tao.back_project.pojo.User;
import com.tao.back_project.service.impl.UserService;
import com.tao.back_project.util.JwtUtils;
import com.tao.back_project.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
public class UserController {
    @Autowired
    private UserService userService;

    @PassToken
    @PostMapping("login")
    public Result login(@RequestBody User user){
        try {
            userService.login(user);
            //设置token
            String token = JwtUtils.createToken(String.valueOf(user.getUid()));
            //将token作为返回值
            return new Result(200,true,"成功",token);
        }catch (Exception e){
            return new Result(500,false,"失败",null);
        }
    }

    @PassToken
    @PostMapping("register")
    public Result register(@RequestBody User user){
        try {
            userService.register(user);
            return new Result(200,true,"成功",null);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(500,false,"失败",null);
        }
    }

    @PassToken
    @GetMapping("checkName")
    public Result checkName(String username){
        try {
            boolean is = userService.isExist(username);
            if (is){
                return new Result(500,false,"失败",null);
            }
            return new Result(500,false,"成功",null);
        }catch (Exception e){
            return new Result(500,false,"失败",null);
        }
    }

    @GetMapping("other")
    public Result other(){
        return new Result(200,true,"成功",null);
    }
}
