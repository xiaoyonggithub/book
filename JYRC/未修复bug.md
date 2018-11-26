1、添加社保卡号的查询
2、修改文号为自动获取
3、统计功能

4、人事代理人员信息未关联人员

```sql
select * from uj43 where aac001 is null;
```

5、uj21.getUj21报错myrownum列名无效（2018/04/16 待确认）

~~6、毕业院校或档案来源 模糊查询~~(2018/04/16)

~~7、大屏数据生成的定时任务~~

8、档案转出协办信息的日志

~~9、档案转出协办的电话号码~~

~~10、发送短信信息 (2018/04/16)~~

~~11、修改大屏的定时任务~~

12、查询条件加注销标志

13、区县权限的建立，协办的验证

~~14、基本信息添加转圈~~

15、是否所有操作都需要设置经办机构

16、档案转移添加双击事件

17、档案室2处理

```sql
--1.修改50柜之后的档案为第二档案室
update uj21 set yuh040 = '2' where to_number(yuh050) >= 50;
--2.设置一档案室50柜之后的不可用
update uh10 set yae159 = '2' where yuh040 = '1' and to_number(yuh050) >= 50 
--3.设置二档案室50柜之前不可用
update uh10 set yae159 = '2' where yuh040 = '2' and to_number(yuh050) < 50 
--4.修改二号档案室已使用的档案位置
update uh10 set yae159 = '1' where yuh040 = '2' and yuh060 in (select yuh060 from uj21 where yuh040 = '2')
--5.将2号档案室的档案与档案位置关联
update uh10 a set yuj210 = (select yuj210 from uj21 where yuh060 = a.yuh060 and yuh040 = '2') where a.yuh040 = '2' and to_number(a.yuh050) >= 50
--6.清除一号档案室中50柜后关联的档案信息
update uh10 set yuj210 = null where yuh040 = '1' and to_number(yuh050) >= 50 and yuj210 is not null
--问题：部分功能为关联档案室
```

```sql
--1.修改一号档案室正在使用的档案位置
update uh10 a 
set  yae159 = (select yae159 from uh10 where yuh040 = '1' and yuh060 = a.yuh060),--使用标志
yuh056 = (yuh056 yuj210 from uh10 where yuh040 = '1' and yuh060 = a.yuh060), --使用时间
yuj210 = (select yuj210 from uh10 where yuh040 = '1' and yuh060 = a.yuh060)--关联档案
where yuh040 = '2';
--2.将前50柜中多余的位置号置为不可用
update uh10
set yae159 = '2'
where yuh040 = '2' and to_number(yuh050) < 50 
and yuh050 = 'E' or yuh052 = '6' ;
--3.删除1号档案室
delete from uh10 where yuh040 = '1';
--4.修改2号档案室为一号档案室
update uh10 
set yuh040 = '1';
--5.修改档案室的列
insert into uh06(yuh040,yuh051) values('1','E');
--6.修改档案室的层
insert into uh07(yuh040,yuh052) values('1','6');
--7.修改档案室的柜号
```

~~17、人事代理人员的导出人员条数限制~~

~~18、档案补充材料登记没有问题~~

~~19、人员分类的作用~~

~~20、档案影像登记查询~~

~~21、转出时文号规则~~

##### ~~22、档案影像查阅--aae017~~

23、人事代理没有代理日期

```sql
select count(*) from uj43 where yuj431 is null;
```

~~24、材料登记查询条件~~

~~25、新建扫描目录(未采用如下方案，修改代码)~~

```sql
insert into uj22(yuj030,yuj210,yue022,yue023,yae167,yuj214,aae017,ye116,aae011,aae036)
select 
   SEQ_YUJ030.Nextval,
   yuj210,
   'R00600', --材料分类
   '六、职称及资格证材料', --材料名称
   19,
   null,
   '410800',
   '1',
   '系统管理员',
   sysdate
from  (select distinct yuj210 from uj22) --126453

--大学材料下目录
select 
  (select yuj031 from uj21 where yuj210 = a.yuj210) yuj031 
from uj22 a where a.yue022 = 'R00200' and a.yuj214 is not null and a.yuj214 != 0

--转移大学目录的材料
--修改大学材料目录为大中专目录
update uf05
set yue022 = 'R00210',  --材料分类
yue023 = '二-1、大专、中专材料',      --材料分类名称
where yue022 = 'R00200';  --大学材料
--修改大中专材料页数
update (select * from uj22 where yuj210 in (select yuj210 from uj22 where yue022 = 'R00200' and yuj214 is not null and yuj214 != 0) and  yue022 = 'R00210')
set yuj214 = yuj214 + (select yuj214 from uj22 where yue022 = 'R00200' and yuj214 is not null and yuj214 != 0 and yuj210 = a.yuj210) --修改材料页数
 
```

~~26、修改扫描目录的排序号~~

```sql
--干部
update uj22 
set yae167 = '36'
where yue022 = 'R09000';
--工人
update uj22 
set yae167 = '36'
where yue022 = 'R19000';
--非普
update uj22 
set yae167 = '36'
where yue022 = 'R29000';
--其他
update uj22 
set yae167 = '36'
where yue022 = 'R39000';
```

 ~~27、修改人事代理”代理人员查询“中”单位类型查询失败“~~

~~28、修改对档案及人员信息的部分更新操作~~

~~29、档案统计--档案转出与转入 统计错误~~

30、扫描上传出错

~~31、单位开户信息修改~~

32、修改档案接收时选择人

33、修改档案扫描的目录名称

```sql
select * from ue02 where yuh040 = '1' order by yuj03c,yue022;
select * from uj22  order by yuj210,yue022;
--修改名称
update uj22 a
set a.yue023 = (select yue023 from ue02 where yue022 = a.yue022)
where  a.yue022 != 'R00200';
```

34、扫描率

```sql
--每个月的扫描率
select 
   mon,
   count(*) num,
   sum(count(*)) over(order by mon) total,
   to_char(count(*)/(select count(*) from uj21 where yuj230 != 50)*100,'990.9999')||'%' rate,
   to_char((sum(count(*)) over(order by mon))/(select count(*) from uj21 where yuj230 != 50)*100,'990.9999')||'%' trate
from (select 
       yuf057,    
       to_char(aae036,'yyyymm') mon,
       count(*)
    from uf05
    group by yuf057,to_char(aae036,'yyyymm')) a
group by a.mon
order by a.mon desc;   
--每个月每个人的扫描率
select 
   mon,
   aae011,
   count(*) num,
   to_char(count(*)/(select count(*) from uj21 where yuj230 != 50)*100,'990.9999')||'%' rate
from (select 
       yuf057, 
       aae011,   
       to_char(aae036,'yyyymm') mon,
       count(*)
    from uf05
    group by yuf057,to_char(aae036,'yyyymm'),aae011) a
group by cube(a.mon,a.aae011)
order by a.mon desc; 

--每个人的扫描率
select 
   aae011,
   count(*),
   to_char(count(*)/(select count(*) from uj21 where yuj230 != 50)*100,'990.9999')||'%' rate
from (
  select
     yuf057,
     aae011, 
     count(*)
  from uf05
  group by yuf057,aae011)
group by aae011;
```

`2018/06/21`线上统计数据

![1529563950466](E:\typora\images\1529563950466.png)

本地统计数据

![1529564003934](E:\typora\images\1529564003934.png)