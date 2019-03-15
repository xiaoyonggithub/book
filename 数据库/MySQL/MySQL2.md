# 一、变量

## 1.1.定义变量

```mysql
-- 局部变量
declare variable_name variable_type default default_value;
```

```sql
declare var_name varchar(50);
declare var_name varchar(50),
	    var_age int default 10;
```

```sql
-- 用户变量,可以在一个会话的任意地方声明，作用域是整个会话
-- set时可以用“=”或“：=”
set @v_error = '1';
set @v_error := '1';
-- select时必须用“：=赋值”
select @v_error := '1';
select @v_error := error from log where logid = '1';
select @name:=password from user limit 0,1;
-- 从数据表中获取一条记录password字段的值给@name变量。在执行后输出到查询结果集上面。
```

### 1.1.1.局部变量和用户变量的区别

局部变量（`declare声明的变量`）都不被初始化为`null`，只在`begin/end`块中有效。

用户变量（`@申明的变量`）在一个会话只会初始化一次，相当于该会话内的全局变量。

### 1.1.2.系统变量

系统变量又分为全局变量与会话变量 ：

* 全局变量在`MYSQL`启动的时候由服务器自动将它们初始化为默认值，这些默认值可以通过更改`my.ini`这个文件来更改 
* 会话变量在每次建立一个新的连接的时候，由`MYSQL`来初始化。`MYSQL`会将当前所有全局变量的值复制一份。来做为会话变量
* 全局变量与会话变量的区别就在于，对全局变量的修改会影响到整个服务器，但是对会话变量的修改，只会影响到当前的会话 

```mysql
-- 查看会话变量
show session variables like "%var%";
show session variables;
show variables;
select @@variables;
-- 查看全局变量
show global variables;
```

```mysql
-- 有些系统变量的值可以利用语句进行动态修改，但有的系统变量的值为只读
-- 更改会话变量
set session varname = value;   
set @@session.varname = value;
-- 更改全局变量
set global varname = value;
set @@global.varname = value;
```

```mysql
set session sort_buffer_size = 40000;
set @@session.sort_buffer_size = 40000;
-- 修改全局变量的值，需要拥有super权限
set global sort_buffer_size = 40000;
set @@global.sort_buffer_size = 40000;
```

> 注意：`local`是 `session`的近义词，可以使用`local`替代`session`；没有指定`session`或`global`时，默认当做`session`处理

## 1.2.变量赋值

```mysql
set parameter_name  = value,[parameter_name  = value...]
```

```mysql
set var_username = 'username';
```

```sql
SELECT col_name[,...] INTO var_name[,...] table_name [WHERE...];
```

```mysql
declare v_menuid varchar(30);
declare v_menuname varchar(30);
select menuid,menuname into v_menuid,v_menuname from tamenu where menuid = '1';
```

# 二、条件和循环语句

## 2.1.条件语句

```mysql
IF expression THEN commands  
    [ELSEIF expression THEN commands]  
    [ELSE commands]  
END IF; 
```

```mysql
IF (sale_value > 200) THEN  
    CALL free_shipping(sale_id);    /*Free shipping*/  
    IF (customer_status='PLATINUM') THEN  
        CALL apply_discount(sale_id,20); /* 20% discount */  
    ELSEIF (customer_status='GOLD') THEN  
        CALL apply_discount(sale_id,15); /* 15% discount */  
    ELSEIF (customer_status='SILVER') THEN  
        CALL apply_discount(sale_id,10); /* 10% discount */  
    ELSEIF (customer_status='BRONZE') THEN  
        CALL apply_discount(sale_id,5); /* 5% discount*/  
    END IF;  
END IF;  
```

```sql
CASE  
    WHEN condition THEN  
        statements  
    [WHEN condition THEN  
        statements...]  
    [ELSE  
        statements]  
END CASE;  
```

```mysql
CASE  
    WHEN (sale_value>200) THEN  
        CALL free_shipping(sale_id);  
        CASE customer_status  
            WHEN 'PLATINUM' THEN  
                CALL apply_discount(sale_id,20);  
            WHEN 'GOLD' THEN  
                CALL apply_discount(sale_id,15);  
            WHEN 'SILVER' THEN  
                CALL apply_discount(sale_id,10);  
            WHEN 'BRONZE' THEN  
                CALL apply_discount(sale_id,5);  
        END CASE;  
END CASE;  
```

```sql
select
	( case 
     	when ( t.a = 1 and t.b = 0 ) then 
     		t.c else 'n/a' 
      end ) as result 
from test t 
order by result asc
```

## 2.2.循环语句

```mysql
[label:] LOOP  
    statements  
END LOOP [label]; 
```

```mysql
[label:] REPEAT  
    statements  
UNTIL expression  
END REPEAT [label]  
```

```mysql
[label:] WHILE expression DO  
    statements  
END WHILE [label] 	
```

```mysql
SET i=1;  
myloop: LOOP  
    SET i=i+1;  
    IF i=10 then  
        LEAVE myloop;  
    END IF:  
END LOOP myloop;  
SELECT 'I can count to 10';  
```

```mysql
SET i=0;  
loop1: LOOP  
    SET i=i+1;  
    IF i>=10 THEN                 /*Last number - exit loop*/  
        LEAVE loop1;  
    ELSEIF MOD(i, 2)=0 THEN       /*Even number - try again*/  
        ITERATE loop1;  
    END IF;  
  
    SELECT CONCAT(i, " is an odd number");  
END LOOP loop1;  
```







# 三、自定义函数

`delimiter`就是告诉`mysql`解释器，该命令已经结束，可以执行命令了（在命令行模式下 ）。

```sql
DELIMITER $$   -- 定义命令以$$结束，默认MySQL是以;结束
```

用处： 在`MySQL`命令行编写函数和过程时，不能一遇到`;`就执行，此时就可以修改结束符。

```sql
-- 此处定义//为结束符
mysql> delimiter //   
mysql> CREATE FUNCTION `SHORTEN`(S VARCHAR(255), N INT)   
mysql>     RETURNS varchar(255)   
mysql> BEGIN   
mysql> IF ISNULL(S) THEN   
mysql>     RETURN '';   
mysql> ELSEIF N<15 THEN   
mysql>     RETURN LEFT(S, N);   
mysql> ELSE   
mysql>     IF CHAR_LENGTH(S) <=N THEN   
mysql>    RETURN S;   
mysql>     ELSE   
mysql>    RETURN CONCAT(LEFT(S, N-10), '...', RIGHT(S, 5));   
mysql>     END IF;   
mysql> END IF;   
mysql> END;//  
```

## 2.1.函数的语法

```sql
create function function_name([parameter_name type...])
returns return_type
begin 
  ...
end
```

# 四、树形结构

##  4.1.查询某一节点的下一级节点

```mysql
select 
 a.menuid,
 a.menuname,
 a.pmenuid,
 a.menulevel
from tamenu a   
inner join tamenu b on a.pmenuid = b.menuid  -- b为父节点信息
where b.menuname = '银海软件';
```

## 4.2.查询某一节点的上一个节点

```mysql
select 
 b.menuid,
 b.menuname,
 b.pmenuid,
 b.menulevel
from tamenu a
inner join tamenu b on a.pmenuid = b.menuid  -- b为父节点信息
where a.menuname = '大屏管理';
```

## 4.3.查询某一节点的所有上级节点



## 4.4.查询某一节点的所有下级节点

### 4.4.1.自定义函数

```mysql
create function getChildList(rootId int)
returns varchar(10000)
begin
	declare temp varchar(1000);
	declare tempChild varchar(1000);
	
	set temp = '$';
	set tempChild = cast(rootId as char);
	
	while tempChild is not null do
		set temp = concat(temp,',',tempChild);
		select group_concat(menuid) into tempChild from tamenu where find_in_set(pmenuid,tempChild) > 0;
	end while;	
	return temp;
end
```

```mysql
select getChildList(118455); 
-- $,118455,118532,118533,118534,118535,118536,118537,118538,118539,122447,122448,122449,122450
select 
	menuid,
	menuname,
	pmenuid
from tamenu 
where find_in_set(menuid,getChildList(118455));
```

![1530084038630](E:\typora\images\1530084038630.png)

优点: 简单，方便，没有递归调用层次深度的限制 (`max_sp_recursion_depth`,最大`255`) ；
缺点：长度受限，虽然可以扩大` RETURNS varchar(1000)`，但总是有最大限制的。

### 4.4.2.**利用临时表和过程递归** 

```mysql
drop procedure if exists getChildList;
create procedure getChildList(in rootId int)
begin 
	-- 删除临时表
	-- drop temporary table tmp_menu;
	create temporary table if not exists chat_system.tmp_menu(
		id int primary key auto_increment,
		menuid decimal(10,0),
		menulevel decimal(10,0)
	);
	-- 清空数据
	delete from tmp_menu;
	
	call createChildList(rootId,1);
	
	select 
	  a.menuid,
		a.pmenuid,
		a.menuname
	from tamenu a
	inner join tmp_menu b on a.menuid  = b.menuid
  order by b.id;
	
end;
```

```mysql
drop procedure if exists createChildList;
create procedure createChildList(in rootId int,in depth int)
begin
	declare done int default 0;
	declare tmp int;
	declare cur_menu cursor for select menuid from tamenu where pmenuid = rootId;
	declare continue handler for not found set done = 1;
	
	insert into tmp_menu values(null,rootId,depth);
	
	open cur_menu;
	fetch cur_menu into tmp;
	while done = 0 do 
		call createChildList(tmp,depth+1);
		fetch cur_menu into tmp;
	end while;
	
	close cur_menu;
end;
```

```mysql
call getChildList(1);
```

```mysql
ERROR 1456 (HY000): Recursive limit 0 (as set by the max_sp_recursion_depth variable) was exceeded for routine
```

递归限制为0，说明默认情况下不允许递归，此时需要设置递归层级 

```sql
SET GLOBAL max_sp_recursion_depth=20;
SELECT @@max_sp_recursion_depth;
```

# 五、索引

## 5.1.索引加速检索

> 问题：使用索引为什么可加快数据库的检索速度?

