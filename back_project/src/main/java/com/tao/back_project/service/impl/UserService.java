package com.tao.back_project.service.impl;

import com.tao.back_project.dao.UserRepository;
import com.tao.back_project.pojo.User;
import com.tao.back_project.service.IUserService;
import com.tao.back_project.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void login(User user) {
        String username = user.getUsername();
        //先在redis缓存中找
        String password = (String) redisTemplate.opsForValue().get(username);
        //如果缓存中没有就去数据库中找
        User userdb = null;
        if (password == null){
            userdb = userRepository.queryByName(username);
            password = userdb.getPassword();
            if (!password.equals(user.getPassword())){
                throw new RuntimeException();
            }
            //写入缓存
            redisTemplate.opsForValue().set(user.getUsername(),password);
        }else {
            //缓存中有
            if (!password.equals(user.getPassword())){
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void register(User user) {
        //验证合法性
        if (user == null ||
                user.getPassword() == null ||
                "".equals(user.getPassword()) ||
                user.getUsername() == null ||
                "".equals(user.getUsername())){
            throw new RuntimeException();
        }
        int add = userRepository.add(user);
        if (add == 0)
            throw new RuntimeException();
        //新用户加入redis缓存
        redisTemplate.opsForValue().set(user.getUsername(),user.getPassword());
        String pass = (String)redisTemplate.opsForValue().get(user.getUsername());
    }

    @Override
    public boolean isExist(String username) {
        if (username == null || "".equals(username)){
            throw new RuntimeException();
        }
        //先在redis缓存中找
        String password = (String) redisTemplate.opsForValue().get(username);
        //如果redis中有这个人，那就说明存在
        if (password != null){
            throw new RuntimeException();
        }
        User user = userRepository.queryByName(username);
        //如果数据库中有这个人，那就存放到redis中
        if(user != null){
            redisTemplate.opsForValue().set(user.getUsername(),user.getPassword());
        }
        return user != null;
    }
}
