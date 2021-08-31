package com.tao.back_project.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private int uid;
    private String username;
    private String password;
}
