create database myproject;

use myproject;

-- 创建user表
create table user(
                     `uid` int primary key auto_increment,
                     `username` varchar(20) not null ,
                     `password` varchar(20) not null
);
-- 插入数据，md5的密码（123）
insert into `user`(username, password) values('chen','202cb962ac59075b964b07152d234b70');

select * from user;