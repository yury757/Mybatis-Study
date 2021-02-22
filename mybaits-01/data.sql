USE mybatis;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `pwd` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO USER VALUES
(1,'yuyr757','123456'),
(2,'张三y','AAAAAAAAAAAAAAA');

SELECT 2 INTO @I;
-- 执行若干次
INSERT INTO USER VALUES(@I := @I + 1, CONCAT('NAME-', @I), CONCAT('PASSWORD-', @I));

CREATE TABLE TEACHER(
    `ID` INT PRIMARY KEY,
    `NAME` VARCHAR(20) NOT NULL
);

INSERT INTO TEACHER VALUES(1, '李老师'),(2, '黄老师'),(3, '钱老师');

CREATE TABLE STUDENT(
    `ID` INT PRIMARY KEY ,
    `NAME` VARCHAR(20) NOT NULL,
    `TEACHER_ID` INT NOT NULL,
    CONSTRAINT `FKTID` FOREIGN KEY (TEACHER_ID) REFERENCES TEACHER(ID)
);

INSERT INTO STUDENT VALUES(1, '小明', 1),(2, '小五', 1),(3, '小华', 3),
                           (4, '小石', 2),(5, '李笑', 3),(6, '孙武', 2),
                           (7, '黄铭', 2);

CREATE TABLE BLOG(
    `ID` varchar(50) PRIMARY KEY,
    `TITLE` VARCHAR(50) NOT NULL,
    `AUTHOR` VARCHAR(20) NOT NULL,
    `CREATE_TIME` DATETIME NOT NULL,
    `VIEWS` INT NOT NULL DEFAULT 0 COMMENT '浏览量'
);

INSERT INTO BLOG VALUES
(replace(UUID(), '-', ''), '记一个关于std::unordered_map并发访问的BUG', 'KatyuMarisa', '2021-02-22 18:44', 20),
(replace(UUID(), '-', ''), '爬虫入门到放弃系列04：我对钱没有兴趣', 'Seven0007', '2021-02-22 18:40', 10000000),
(replace(UUID(), '-', ''), 'Laravel Queues 队列应用实战', 'bananaplan', '2021-02-22 18:35', 20),
(replace(UUID(), '-', ''), '两年Java，去字节跳动写Python和Go', 'TinyLeon', '2021-02-21 13:48', 72658),
(replace(UUID(), '-', ''), '彻底理解c++的隐式类型转换', 'apocelipes', '2021-02-19 10:52', 594),
(replace(UUID(), '-', ''), '亿级流量架构之服务限流思路与方法', '等不到的口琴', '2021-02-20 22:43', 610);
