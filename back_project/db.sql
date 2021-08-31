use myproject;
create table user(
                     `uid` int primary key auto_increment,
                     `username` varchar(20) not null ,
                     `password` varchar(20) not null
);

insert into `user`(username, password) values('chentao','123');

select * from user;