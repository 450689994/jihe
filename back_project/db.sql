create database myproject;

use myproject;

-- 创建user表
create table user(
                     `uid` int primary key auto_increment,
                     `username` varchar(20) not null ,
                     `password` varchar(32) not null
);
-- 插入数据，md5的密码（123）
insert into `user`(username, password) values('chen','202cb962ac59075b964b07152d234b70');
-- 插入数据，md5的密码（abc）
insert into `user`(username, password) values('hou','900150983CD24FB0D6963F7D28E17F72');

select * from user;