### 一、`Exsits`

`exists  `(SQL返回的结果集为true)

```sql
--有值返回true,没值返回false
select id,name from a where exists (select * from b where b.aid=１) ;
```

`not exists  `(SQL返回的结果集为false)

```sql
--有值返回true,没值返回false
select id,name from a where not exists (select * from b where b.aid=１) ;
```

