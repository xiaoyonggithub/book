```sql
--个人人事代理和单位人事代理财务统计
select 
  to_char(ycbt08,'yyyymm') mon,
  to_char(sum(decode(yccs31,2,decode(ycbs03,0,aae019,0),0)),'L999,999,999,990.99') anum,--个人代理（档案保管费）
  to_char(sum(decode(yccs31,2,decode(ycbs03,1,aae019,0),0)),'L999,999,999,990.99') bnum,--个人代理（综合服务费）
  to_char(sum(decode(yccs31,1,decode(ycbs03,1,aae019,0),0)),'L999,999,999,990.99') cnum,--单位代理（开户费）
  to_char(sum(decode(yccs31,1,decode(ycbs03,0,aae019,0),0)),'L999,999,999,990.99') dnum,--单位代理（档案保管费）
  to_char(sum(aae019),'L999,999,999,990.99') total  --合计金额
from cdt1
where yad006 = '1'
group by to_char(ycbt08,'yyyymm')
order by mon desc;
```

```plsql
select 
  to_char(a.ycbt08,'yyyymm') mon,
  to_char(sum(decode(a.yccs31,'1',decode(a.ycbs03,'2',decode(b.aaa115,'91',a.aae019,0),0),0)),'L999,999,999,990.99') anum, --社保代理服务费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'110',a.aae019,0),0)),'L999,999,999,990.99') bnum, --基本养老保险费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'210',a.aae019,0),0)),'L999,999,999,990.99') cnum, --失业保险费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'310',a.aae019,0),0)),'L999,999,999,990.99') dnum, --基本医疗保险费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'410',a.aae019,0),0)),'L999,999,999,990.99') enum, --工伤保险费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'510',a.aae019,0),0)),'L999,999,999,990.99') fnum, --生育保险费
  to_char(sum(decode(a.ycbs03,'2',decode(b.aae140,'330',a.aae019,0),0)),'L999,999,999,990.99') hnum, --大病补充保险费
  to_char(sum(decode(a.ycbs03,'3',decode(b.aae140,'810',a.aae019,0),0)),'L999,999,999,990.99') inum, --公积金
  to_char(sum(decode(a.ycbs03,'2',decode(a.ycdt12,'1',decode(a.aaa039,'1',a.aae019),0),0)),'L999,999,999,990.99') jnum, --社保暂存金收入
  to_char(sum(decode(a.ycbs03,'2',decode(a.ycdt12,'1',decode(a.aaa039,'2',a.aae019),0),0)),'L999,999,999,990.99') mnum, --社保暂存金支出
  to_char(sum(decode(a.ycbs03,'3',decode(a.ycdt12,'1',decode(a.aaa039,'1',a.aae019),0),0)),'L999,999,999,990.99') nnum, --公积金暂存金收入
  to_char(sum(decode(a.ycbs03,'3',decode(a.ycdt12,'1',decode(a.aaa039,'2',a.aae019),0),0)),'L999,999,999,990.99') pnum, --公积金暂存金支出
  sum(decode(a.yccs31,'1',decode(a.ycbs03,'2',decode(b.aaa115,'91',a.aae019,0),0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'110',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'210',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'310',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'410',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'510',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(b.aae140,'330',a.aae019,0),0))+
  sum(decode(a.ycbs03,'3',decode(b.aae140,'810',a.aae019,0),0))+
  sum(decode(a.ycbs03,'2',decode(a.ycdt12,'1',decode(a.aaa039,'1',a.aae019),0),0))+
  sum(decode(a.ycbs03,'3',decode(a.ycdt12,'1',decode(a.aaa039,'1',a.aae019),0),0))-
  sum(decode(a.ycbs03,'2',decode(a.ycdt12,'1',decode(a.aaa039,'2',a.aae019),0),0))-
  sum(decode(a.ycbs03,'3',decode(a.ycdt12,'1',decode(a.aaa039,'2',a.aae019),0),0)) total
from cdt2 a
join cct2 b on a.ycbt01 = b.ycbt01 
where a.yad006 = '1'
group by to_char(a.ycbt08,'yyyymm')
order by mon desc;
```



1.焦作市人才服务指挥监控系统

2.焦作市人才服务全景视图

3.档案情况

4.流动党员分析

5.高校毕业生分析

6.全市人力资源供求状况分析（季度） 

7.全市人力资源供求状况分析（季度对比）

8.工作人员工作量分析