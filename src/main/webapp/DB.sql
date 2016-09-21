--资费信息表
create table cost_light(
  	cost_id			number(4) primary key,
  	name 			varchar(50)  not null,
  	base_duration 	number(11),
  	base_cost 		number(7,2),
  	unit_cost 		number(7,4),
  	status 			char(1),
  	descr 			varchar2(100),
  	creatime 		date default sysdate ,
  	startime 		date,
	cost_type		char(1)
  );

create sequence cost_seq_light start with 100;
--资费信息表数据
INSERT INTO cost_light VALUES (1,'5.9元套餐',20,5.9,0.4,1,'5.9元20小时/月,超出部分0.4元/时',DEFAULT,DEFAULT,'2');
INSERT INTO cost_light VALUES (2,'6.9元套餐',40,6.9,0.3,1,'6.9元40小时/月,超出部分0.3元/时',DEFAULT,DEFAULT,'2');
INSERT INTO cost_light VALUES (3,'8.5元套餐',100,8.5,0.2,1,'8.5元100小时/月,超出部分0.2元/时',DEFAULT,DEFAULT,'2');
INSERT INTO cost_light VALUES (4,'10.5元套餐',200,10.5,0.1,1,'10.5元200小时/月,超出部分0.1元/时',DEFAULT,DEFAULT,'2');
INSERT INTO cost_light VALUES (5,'计时收费',null,null,0.5,1,'0.5元/时,不使用不收费',DEFAULT,DEFAULT,'3');
INSERT INTO cost_light VALUES (6,'包月',NULL,20,NULL,1,'每月20元,不限制使用时间',DEFAULT,DEFAULT,'1');
COMMIT;

select * from cost_light;

-------------------------------------------------------------------------------
--管理员表
create table admin_info_light(
   	admin_id 	number(8) primary key not null,
   	admin_code 	varchar2(30) not null,
   	password 	varchar2(30) not null,
   	name 		varchar2(30) not null,
   	telephone 	varchar2(15),
   	email 		varchar2(50),
   	enrolldate 	date default sysdate not null
);

create sequence admin_seq_light start with 10000;

--管理员表数据
insert into admin_info_light values(2000,'admin','123','ADMIN','123456789','admin@tarena.com.cn',sysdate);
insert into admin_info_light values(3000,'zhangfei','123','ZhangFei','123456789','zhangfei@tarena.com.cn',sysdate);
insert into admin_info_light values(4000,'liubei','123','LiuBei','123456789','liubei@tarena.com.cn',sysdate);
insert into admin_info_light values(5000,'caocao','123','CaoCao','123456789','caocao@tarena.com.cn',sysdate);
insert into admin_info_light values(6000,'aaa','123','AAA','123456789','aaa@tarena.com.cn',sysdate);
INSERT INTO admin_info_light VALUES(7000,'bbb','123','BBB','123456789','bbb@tarena.com.cn',SYSDATE);
commit;

select * from admin_info_light;
