package com.tao.back_project.dao;

import com.tao.back_project.dao.interf.IUserDao;
import com.tao.back_project.dao.mybatis.UserMap;
import com.tao.back_project.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserDao {
    @Autowired
    private UserMap userMap;

    @Override
    public User queryByName(String username) {
        return userMap.queryByName(username);
    }

    @Override
    public int add(User user) {
        return userMap.add(user);
    }
}
