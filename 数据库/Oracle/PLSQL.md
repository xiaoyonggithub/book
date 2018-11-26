### 一、基本结构

#### 1.1.`if`语句

```plsql
IF condition THEN 
   S;
END IF;
```

```plsql
IF condition THEN
   S1; 
ELSE 
   S2;
END IF;
```

```plsql
IF(boolean_expression 1)THEN 
   S1; 
ELSIF( boolean_expression 2) THEN
   S2; 
ELSIF( boolean_expression 3) THEN
   S3;
ELSE 
   S4; 
END IF;
```

#### 1.2.`case`语法

```plsql
CASE selector
    WHEN 'value1' THEN S1;
    WHEN 'value2' THEN S2;
    WHEN 'value3' THEN S3;
    ...
    ELSE Sn;  -- default case
END CASE;
```

```plsql
CASE
    WHEN selector = 'value1' THEN S1;
    WHEN selector = 'value2' THEN S2;
    WHEN selector = 'value3' THEN S3;
    ...
    ELSE Sn;  -- default case
END CASE;
```

#### 1.3.`loop`循环语句

```plsql
LOOP
  Sequence of statements;
  --EXIT/EXIT WHEN  退出循环
END LOOP;
```

```plsql
WHILE condition LOOP
   sequence_of_statements
END LOOP;
```

```plsql
FOR counter IN initial_value .. final_value LOOP
   sequence_of_statements;
END LOOP;
```





### 二、变量



### 三、存储过程

#### 3.1.调用储存过程

```plsql
procedure prc_jsgrrsdlf_dabgf_tmp(prm_yuj210      in uj21.yuj210%type, --人事档案流水号
                                  prm_aae017      in uj37.aae017%type, --经办机构
                                  prm_aae041      in uj37.aae041%type, --缴费开始年月
                                  prm_aae042      in uj37.aae042%type, --缴费结束年月
                                  prm_rsdlf       out number, --人事代理费
                                  prm_dabgf       out number, --档案保管费   
                                  prm_rsdlf_dabgf out number, -- 人事代理费+档案保管费
                                  prm_appcode     out varchar2, --错误代码
                                  prm_errormsg    out varchar2); --错误信息
```

```sql
--在pl/sql中调用
declare


   v_rsdlf  number;
   v_dabgf  number;
   v_aae019 cdt1.aae019%type;
   v_ycbt01 cdt1.ycbt01%type;
   v_ycbs20 cdt1.ycbs20%type;
   appcode  varchar2(50);
   errormsg varchar2(50);
```





### 四、函数

### 五、游标

指向查询结果集的指针，指向哪一行，提取哪一行的数据(PLSQL的游标默认指向结果集的第一行) 

#### 5.1.游标的属性

【%found 】当最近一次读入记录成功时返回true 

【%notfound 】当最近一次读入记录成功时返回false

【%isopen】判断游标是否已经打开 

【%rowcount】返回已从游标中读取的记录数 

#### 5.2.读取游标数据

```plsql
--
declare
--存放一条记录
tmp_emp emp%rowtype;
--定义游标
cursor cur_emps 
is 
select * from emp;
begin 
  --打开游标
  open cur_emps;
  loop
    --取值,读取一行记录
    fetch cur_emps into tmp_emp;
    --循环结束条件
    exit when cur_emps%notfound;
    dbms_output.put_line(tmp_emp.ename);
  end loop;
  --关闭游标
  close cur_emps;
end;
```

```plsql
--for循环读取游标数据
declare
--定义游标
cursor cur_emps 
is 
select * from emp;
begin 
  for tmp_emp in cur_emps loop
    dbms_output.put_line(tmp_emp.ename);
  end loop;
end;
```

```plsql
--不声明游标直接读取数据
declare
begin 
  for tmp_emp in (select * from emp) loop
    dbms_output.put_line(tmp_emp.ename);
  end loop;
end;
```

#### 5.3.带参数的游标

```plsql
--带参数的游标
declare
--行记录
r_emp emp%rowtype;
--带参数的游标
cursor cur_emp(tmp_deptno dept.deptno%type)
is 
select * from emp
where deptno = tmp_deptno;
begin 
  --打开游标
  open cur_emp(10);
  loop
    fetch cur_emp into r_emp;
    exit when cur_emp%notfound;
    dbms_output.put_line(r_emp.ename);
  end loop;
  --关闭游标
  close cur_emp;
end;
```

```plsql
--带参数游标
declare
--带参数的游标
cursor cur_emp(tmp_deptno dept.deptno%type)
is 
select * from emp
where deptno = tmp_deptno;
begin 
   for r_emp in cur_emp(10) loop
     dbms_output.put_line(r_emp.ename);
   end loop;
end;
```

#### 5.4.游标删除记录

```plsql
declare
cursor cur_emp
is 
select * from emp
for update;   --
begin 
   for r_emp in cur_emp loop
     if r_emp.mgr = 7839 then 
        delete from emp where current of cur_emp;  --删除当前游标记录
     end if;
   end loop; 
   --提交删除
   --commit;
end;
```

#### 5.5.游标修改记录

```plsql
declare
cursor cur_emp
is 
select * from emp
for update;
begin 
   for r_emp in cur_emp loop
     if r_emp.sal < 1000 then 
        update emp
        set sal = sal + 1000
        where current of cur_emp;
     end if;
   end loop; 
end;
```

```plsql
declare
cursor cur_emp
is 
select * from emp
for update of sal;
begin 
   for r_emp in cur_emp loop
     if r_emp.sal < 1000 then 
        update emp
        set sal = sal + 1000
        where current of cur_emp;
     end if;
   end loop; 
end;
```

#### 5.6.`for update of `与`for update`的区别

`【for update】`锁定所有表中满足条件的记录

`【for update of】`多表连接锁定 ,可以指定要锁定的是哪几张表，而如果表中的列没有在`for update of `后面出现的话 ,就意味着这张表其实并没有被锁定 

```plsql
--锁定所有的记录
select * from emp for update;
--锁定empno='7499'的记录
select * from emp where empno = '7499' for udpate;
--锁定两个表中所有匹配的记录
select * 
from emp a
left join dept b  on a.deptno = b.deptno;
--锁定两个表中所有满足条件的记录
select * 
from emp a
left join dept b  on a.deptno = b.deptno
where a.deptno = '10' 
for update;
--只锁定emp表中满足条件的记录
select * 
from emp a
left join dept b  on a.deptno = b.deptno
where a.deptno = '10'
for udpate of a.deptno;
```



### 六、触发器