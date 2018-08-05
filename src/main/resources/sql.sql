CREATE DATABASE IF NOT EXISTS iqalliance DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE iqalliance;


CREATE TABLE meeting(
	id int primary key auto_increment,
	lecturerName varchar(20),
	introduction varchar(50) NOT NULL,
	meetingTime datetime,
	meetingTheme varchar(100),
	pptUrl varchar(100) DEFAULT 'http://localhost:8080/smallProject/message.do',
	imageUrl varchar(100),
	videoUrl varchar(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE admin(
	account varchar(20) primary key,
	password varchar(20) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE news(
	id int PRIMARY KEY AUTO_INCREMENT,
	theme VARCHAR(100) NOT NULL UNIQUE,
	time DATETIME NOT NULL,
	cover VARCHAR(200) NOT NULL,
	detailUrl VARCHAR(300) NOT NULL,
	digest VARCHAR(200) NOT NULL,
	watch int DEFAULT 0,
	praise int DEFAULT 0,
	kind tinyint DEFAULT 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE feedback(
	id int PRIMARY KEY AUTO_INCREMENT,
	contact VARCHAR(30),
	feedback VARCHAR(200)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE domain(
	domain VARCHAR(50) PRIMARY KEY
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE topic(
	topic VARCHAR(200) PRIMARY KEY
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE lecturer(
	id int PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	introduction VARCHAR(100) NOT NULL,
	pic VARCHAR(200) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE conference(
	id int PRIMARY KEY AUTO_INCREMENT,
	loc VARCHAR(5) NOT NULL,
	domain VARCHAR(100) NOT NULL,
	topic VARCHAR(200) NOT NULL,
	begintime datetime NOT NULL,
	endtime datetime NOT NULL,
	FOREIGN KEY (topic) REFERENCES topic(topic) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (domain) REFERENCES domain(domain) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE lecture(
	id int PRIMARY KEY AUTO_INCREMENT,
	lid int NOT NULL,
	cid int NOT NULL,
	ppt VARCHAR(200),
	FOREIGN KEY (lid) REFERENCES lecturer(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (cid) REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sponsor(
	id int PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(20),
	company VARCHAR(20) NOT NULL,
	pic VARCHAR(200) NOT NULL,
	detail VARCHAR(200) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE antistop(
	antistop VARCHAR(30) PRIMARY KEY
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE antistops(
	id int PRIMARY KEY AUTO_INCREMENT,
	sid int NOT NULL,
	antistop VARCHAR(30) NOT NULL,
	FOREIGN KEY(sid) REFERENCES sponsor(id) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE forum(
	forum VARCHAR(50) PRIMARY KEY
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE ticket(
	id int PRIMARY KEY AUTO_INCREMENT,
	forum VARCHAR(50) NOT NULL,
	name VARCHAR(20) NOT NULL,
	company VARCHAR(30),
	position VARCHAR(30),
	phone CHAR(11) NOT NULL UNIQUE,
	password VARCHAR(18) NOT NULL,
	email VARCHAR(50) NOT NULL,
	paid TINYINT DEFAULT 0,
	qrCode VARCHAR(200),
	FOREIGN KEY (forum) REFERENCES forum(forum) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE image(
	id INT PRIMARY KEY AUTO_INCREMENT,
	hashcode VARCHAR(11) NOT NULL,
	path VARCHAR(150) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE download(
	id INT PRIMARY KEY AUTO_INCREMENT,
	hashcode VARCHAR(11) NOT NULL,
	path VARCHAR(150) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--测试信息
-----------------------1.1
INSERT INTO domain
VALUES
('质量体系论坛');

INSERT INTO topic
VALUES
('质量感知价值新质量体系'),
('《测试团队的逆袭》--价值驱动的质量体系建设');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('张珣','Autodesk Principle SQA Engineer','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/27c042688481e145a0fd84960276eebc.png'),
('解玲玲','广联达核心大型产品质量经理 北京','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/4bed9b98420ffb6cf9bb6e715bc70416.png'),
('杨泗豪','广联达核心大型产品自动化负责人 西安','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/69d6234d0b4e8bc4d97a9064091f7611.png');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('212A',now(),now(),'质量体系论坛','质量感知价值新质量体系'),
('212A',now(),now(),'质量体系论坛','《测试团队的逆袭》--价值驱动的质量体系建设');

INSERT INTO lecture
(lid,cid)
VALUES
(1,1),
(2,1),
(3,1);

UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=1;
UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=2;
UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=3;

-------------------------------1.2
INSERT INTO domain
VALUES
('管理论坛');

INSERT INTO topic
VALUES
('多米诺游戏坊——团队、敏捷 & 管理3.0'),
('高效思考力、沟通能力和做事能力实战训练-金字塔原理');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('侯伯薇','未来邦科技 培训师','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/f62ea9eb949bad6b360349533162c8fc.png'),
('刘灿','中兴通讯系统产品领导力总监、资深的管理经理','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180615/2af7abcc5af1e33fa6de463444484c8b.jpg');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('212B',now(),now(),'管理论坛','多米诺游戏坊——团队、敏捷 & 管理3.0'),
('212B',now(),now(),'管理论坛','高效思考力、沟通能力和做事能力实战训练-金字塔原理');

INSERT INTO lecture
(lid,cid)
VALUES
(4,2),
(5,2);

UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=4;
UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=5;

-------------
INSERT INTO domain
VALUES
('人工智能论坛');

INSERT INTO topic
VALUES
('没有数据怎么办？——浅谈迁移学习'),
('预测创意，启发当下-Pretotype it--模拟体验式开发模式探讨');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('王晓雷','ThoughtWorks中国区咨询的高级算法科学家','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180619/89f4f4c88de1e781a46041003cadfcd0.jpg'),
('张群鹤','中国银行软件中心软件测试工程师','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180619/9419cc4bd65533bd33a32feb69a2d7ad.jpg');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('206B',now(),now(),'人工智能论坛','没有数据怎么办？——浅谈迁移学习'),
('206B',now(),now(),'人工智能论坛','预测创意，启发当下-Pretotype it--模拟体验式开发模式探讨');

INSERT INTO lecture
(lid,cid)
VALUES
(6,3),
(7,3);

UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=6;
UPDATE lecture
SET ppt='http://ppt.1ppt.com/uploads/soft/1807/1-1PI1164916.zip'
WHERE lid=7;

-----------------------------2.1

INSERT INTO sponsor
(name,company,pic)
VALUES
('腾讯WeTest质量开放平台','腾讯WeTest','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180611/02d9d499fb29854d9327b01213ff85c7.jpg');

INSERT INTO antistop
(antistop)
VALUES
('助力研发效率 '),
('保障产品品质');

INSERT INTO antistops
(sid,antistop)
VALUES
(4,'保障产品品质'),
(4,'助力研发效率');