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


--CREATE TABLE theme(
--	theme VARCHAR(50) PRIMARY KEY
--)ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE topic(
--	topic VARCHAR(200) PRIMARY KEY
--)ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE lecturer(
--	id int PRIMARY KEY AUTO_INCREMENT,
--	name VARCHAR(20) NOT NULL,
--	introduction VARCHAR(100) NOT NULL UNIQUE,
--	pic VARCHAR(200) NOT NULL
--)ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE conference(
--	id int PRIMARY KEY AUTO_INCREMENT,
--	loc VARCHAR(5) NOT NULL,
--	date DATETIME NOT NULL,
--	theme  VARCHAR(100) NOT NULL,
--	topic VARCHAR(100) NOT NULL,
--	FOREIGN KEY (theme) REFERENCES theme(theme) ON UPDATE CASCADE ON DELETE CASCADE,
--	FOREIGN KEY (topic) REFERENCES topic(topic) ON UPDATE CASCADE ON DELETE CASCADE
--)ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE lecture(
--	id int PRIMARY KEY AUTO_INCREMENT,
--	lid int NOT NULL,
--	cid int NOT NULL,
--	FOREIGN KEY (lid) REFERENCES lecturer(id) ON UPDATE CASCADE ON DELETE CASCADE,
--	FOREIGN KEY (cid) REFERENCES conference(id) ON UPDATE CASCADE ON DELETE CASCADE
--)ENGINE=InnoDB DEFAULT CHARSET=utf8;