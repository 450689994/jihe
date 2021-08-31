package com.tao.back_project.service;

import com.tao.back_project.pojo.User;

public interface IUserService {
    void login(User user);

    void register(User user);

    boolean isExist(String username);
}
