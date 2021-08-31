package com.tao.back_project.dao.interf;

import com.tao.back_project.pojo.User;
import org.springframework.stereotype.Repository;

public interface IUserDao {
    User queryByName(String username);

    int add(User user);
}
