DROP TABLE IF EXISTS user1;

CREATE TABLE user1 (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  temp_name varchar(64) comment '中间状态的用户名',
  PRIMARY KEY (id)
);

insert into user1(name) values('user1');