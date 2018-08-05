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

--������Ϣ
-----------------------1.1
INSERT INTO domain
VALUES
('������ϵ��̳');

INSERT INTO topic
VALUES
('������֪��ֵ��������ϵ'),
('�������Ŷӵ���Ϯ��--��ֵ������������ϵ����');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('�ū�','Autodesk Principle SQA Engineer','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/27c042688481e145a0fd84960276eebc.png'),
('������','��������Ĵ��Ͳ�Ʒ�������� ����','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/4bed9b98420ffb6cf9bb6e715bc70416.png'),
('������','��������Ĵ��Ͳ�Ʒ�Զ��������� ����','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/69d6234d0b4e8bc4d97a9064091f7611.png');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('212A',now(),now(),'������ϵ��̳','������֪��ֵ��������ϵ'),
('212A',now(),now(),'������ϵ��̳','�������Ŷӵ���Ϯ��--��ֵ������������ϵ����');

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
('������̳');

INSERT INTO topic
VALUES
('����ŵ��Ϸ�������Ŷӡ����� & ����3.0'),
('��Ч˼��������ͨ��������������ʵսѵ��-������ԭ��');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('�ޱ','δ����Ƽ� ��ѵʦ','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180608/f62ea9eb949bad6b360349533162c8fc.png'),
('����','����ͨѶϵͳ��Ʒ�쵼���ܼࡢ����Ĺ�����','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180615/2af7abcc5af1e33fa6de463444484c8b.jpg');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('212B',now(),now(),'������̳','����ŵ��Ϸ�������Ŷӡ����� & ����3.0'),
('212B',now(),now(),'������̳','��Ч˼��������ͨ��������������ʵսѵ��-������ԭ��');

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
('�˹�������̳');

INSERT INTO topic
VALUES
('û��������ô�죿����ǳ̸Ǩ��ѧϰ'),
('Ԥ�ⴴ�⣬��������-Pretotype it--ģ������ʽ����ģʽ̽��');

INSERT INTO lecturer
(name,introduction,pic)
VALUES
('������','ThoughtWorks�й�����ѯ�ĸ߼��㷨��ѧ��','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180619/89f4f4c88de1e781a46041003cadfcd0.jpg'),
('��Ⱥ��','�й������������������Թ���ʦ','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180619/9419cc4bd65533bd33a32feb69a2d7ad.jpg');

INSERT INTO conference
(loc,begintime,endtime,domain,topic)
VALUES
('206B',now(),now(),'�˹�������̳','û��������ô�죿����ǳ̸Ǩ��ѧϰ'),
('206B',now(),now(),'�˹�������̳','Ԥ�ⴴ�⣬��������-Pretotype it--ģ������ʽ����ģʽ̽��');

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
('��ѶWeTest��������ƽ̨','��ѶWeTest','http://web1806060001.gz01.bdysite.com/static/kindeditor/attached/image/20180611/02d9d499fb29854d9327b01213ff85c7.jpg');

INSERT INTO antistop
(antistop)
VALUES
('�����з�Ч�� '),
('���ϲ�ƷƷ��');

INSERT INTO antistops
(sid,antistop)
VALUES
(4,'���ϲ�ƷƷ��'),
(4,'�����з�Ч��');