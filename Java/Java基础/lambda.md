# 一、`Lambda`

- lambda表达式是一段可以传递的代码，它的核心思想是将面向对象中的传递数据变成传递行为
- lambda是一个匿名函数，就是将代码像数据一样的传递
- lambda简化了匿名委托的使用

## 1.1.`Lambda`的语法

Java8引入了一个新的操作符`->`(箭头操作符、Lambda操作符)，该操作符将Lambda语句拆分为两部分

- 左侧:Lambda的参数列表
- 右侧:Lambda执行的功能语句，即Lambda体

### 1.1.1.无参数，无返回值

```java
() -> System.out.println("Thread Lambda!")
```

### 1.1.2.有一个参数，无返回值

```java
(x) -> System.out.println(x)
```

```java
Consumer<String> consumer = x -> System.out.println(x);
consumer.accept("一个参数可省略");
```

### 1.1.3.有且只有一个参数，可省略`()`

```java
x -> System.out.println(x)
```

### 1.1.4.有两个参数，且有返回值

```java
Comparator<Integer> comparator = (x,y) -> {
    return Integer.compare(x,y);
};
```

### 1.1.5.Lambda只有一条语句，可省略`return`和大括号

```java
Comparator<Integer> comparator = (x,y) -> Integer.compare(x,y);
```

### 1.1.6.数据类型省略

Lambda 表达式的参数列表的数据类型可以省略不写，因为JVM编译器通过上下文推断出数据类型，即“类型推断”

注意：添加了数据类型，就需要所有的参数都添加数据类型

```java
Comparator<Integer> comparator = (Integer x,Integer y) -> {
    return Integer.compare(x,y);
};
```

## 1.2.类型推断

Lambda表达式中的参数类型都是由编译器推断得出的，因为javac根据程序的上下文，在后台推断出了参数的类型

## 1.3.函数式接口

- 函数时接口就是只包含一个抽象方法的接口
- 可以通过Lambda表达式来创建函数式接口的对象
- 若Lambda表达式抛出一个受检异常，那么该异常需要在目标接口的抽象方法上进行声明
- `@FunctionalInterface`修饰接口是一个函数式接口，可检验接口是否是函数式接口；且在javadoc也会声明该接口是函数式接口

> 对一个数的运算

```java
@FunctionalInterface
public interface Fun {
    Integer cal(Integer num);
}
```

```java
@Test
public void test12() {
    int operation = operation(12, (x) -> x + 2);
    System.out.println(operation);//14
}

public int operation(Integer num, Fun fun) {
    return fun.cal(num);
}
```

> 操作字符串

```java
@FunctionalInterface
public interface StrFun {
    String call(String str);
}
```

```java
public void test13(){
    String res = op("hello", str-> str.toUpperCase());
    System.out.println(res); //HELLO

    String string = op("hello", String::toUpperCase);
    System.out.println(string);//HELLO
}

public String op(String str, StrFun fun){
    return fun.call(str);
}
```

## 1.4.内置的函数式接口

### 1.4.1.`Consumer<T>`

`Consumer<T>`消费型接口，对类型为T的对象应用的操作，方法`void accept(T t)`

```java
@Test
public void test14() {
    happly(1000, m -> System.out.println("你出去玩耍消费了" + m + "元"));
}

public void happly(double money, Consumer<Double> consumer) {
    consumer.accept(money);
}
```

### 1.4.2.`Supplier<T>`

`Supplier<T>`供给型接口，返回类型为T的对象，方法`T get()`

```java
@Test
public void test15() {
    List<Integer> list = getList(5, () -> (int) (Math.random() * 100));
    list.stream().forEach(System.out::println);
}
//产生指定个数的整数，并放入集合中
public List<Integer> getList(int num, Supplier<Integer> supplier) {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < num; i++) {
        list.add(supplier.get());
    }
    return list;
}
```

### 1.4.3.`Function<T, R>`

`Function<T, R>`函数型接口，对类型为T的对象应用操作，并返回结果，且结果是R类型的对象，方法`R apply(T t)`

```java
@Test
public void test16(){
    String lambdaStr = strHandler("Hello Lambda", str -> str.toLowerCase());
    System.out.println(lambdaStr);
}
//处理字符串6
public String strHandler(String str, Function<String,String> fun){
    return fun.apply(str);
}
```

### 1.4.4.`Predicate<T>`

`Predicate<T>`断定型接口，确定类型为T的对象是否满足某约束，返回boolea值，方法`boolean test(T t)`

```java
@Test
public void test17(){
    List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "www", "ok");
    List<String> strs = filterStr(list, x -> x.length() > 3);
    strs.stream().forEach(System.out::println);
}

public List<String> filterStr(List<String> list, Predicate<String> predicate){
    ArrayList<String> strList = new ArrayList<>();
    for (String str: list) {
        if(predicate.test(str)){
            strList.add(str);
        }
    }
    return strList;
}
```

## 1.5.方法引用

若在Lambda体中的功能，已经有方法实现了，可直接使用方法的引用；即Lambda表达式的另一种表现形式

### 1.5.1.引用的形式

- 对象引用::实例方法名
- 类名::静态方法名
- 类名::实例方法名

方法的引用所引用方法的参数列表和返回值类型，需要与函数式接口中抽象方法的参数类表和返回值类型一致

### 1.5.2.对象引用::实例方法名

```java
Employee employee = new Employee(111, "账单", 20, 5000.00);
Supplier<String> supplier = () -> employee.getEname();
System.out.println(supplier.get());

Supplier<String> sup = employee::getEname;
System.out.println(sup.get());
```

### 1.5.3.类名::静态方法名

```java
BiFunction<Double,Double,Double> fun = (x,y) -> Math.max(x,y);
System.out.println(fun.apply(20.1, 20.2));

BiFunction<Double,Double,Double> biFun = Math::max;
System.out.println(biFun.apply(30.01, 30.02));
```

### 1.5.4.类名::实例方法名

此时若lambda参数列表的第一个参数是方法的调用者，则第二个参数（或无参）是实例方法的参数

```java
BiPredicate<String,String> predicate = (x,y) -> x.equals(y);
System.out.println(predicate.test("word", "world"));

BiPredicate<String,String> pre = String::equals;
System.out.println(pre.test("word", "world"));
```

## 1.6.构造器引用

类名::new，构造器的参数列表，需要与函数式接口的参数列表保持一致

```java
//无参构造器
Supplier<Employee> supplier = Employee::new;
System.out.println(supplier.get());

//两个参数的构造器
BiFunction<Integer,String,Employee> fun = Employee::new;
System.out.println(fun.apply(100, "张三"));

//一个参数的构造器
Function<Integer,Employee> function = Employee::new;
System.out.println(function.apply(201));
```

## 1.7.数组引用

类型[]::new，

```java
//创建指定长度的数组
Function<Integer,String[]> fun = (args) -> new String[args];
System.out.println(fun.apply(10));

fun = String[]::new;
System.out.println(fun.apply(10));

Function<Integer,Employee[]> function = Employee[]::new;
System.out.println(function.apply(10));
```







# 二、输入参数

- `lambda`的输入参数在lambda运算符的左边
- 参数的个数大于等于0，多个参数之间是有逗号分隔`,`
- 只有当输入参数为1时，可省略左边的小括号`()`

## 2.1.实例

```java
()->Console.WriteLine("This is a Lambda expression.");  
//This is a Lambda expression.
```

此时的参数个数为0，不能省略左边的`()`

```java
m->m*2;
//计算2*m的值
```

此时参数个数为1，所有可以省略`()`，未省略的表达式`(m)->m*2`

```java
(m,n)->m*n;
//计算m*n的值
```

## 2.2.表达式

下面式子的右侧就是一个表达式

```java
m->m*n;  //计算参数m的平方值
```

## 2.3.语句块	

若表达式的右侧是一个语句块，需要使用`{}`包围

```java
(m, n)->{
    int result = m * n;
    Console.WriteLine(result);
}
```

此时lambda的表达式包含多条语句，所有使用`{}`包围为一个语句块

## 2.4.查询表达式

- 查询表达式是一种使用查询语法的表达式，用于查询或转化来自任意LINQ数据源中的数据

- 查询表大达式是以`from`语句开始，以`select`或`group`语句结束
- 查询表达式中间可包含的语句有
  - `from`:
  - `where`:
  - `left`:
  - `join`:
  - `orderby`:
  - `group`:
  - `into`:
  - `select`:

### 2.4.1.`from`

`from`指定查询操作的数据源和范围变量



# 三、优化方案演化

## 3.1.匿名内部类

```java
@Test
public void test01() {
    //字符串长度排序
    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.compare(o1.length(), o2.length());
        }
    };
    TreeSet<String> ts = new TreeSet<>(comparator);

	//简写
    TreeSet<String> treeSet = new TreeSet(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.compare(o1.length(), o2.length());
        }
    });
}
```

## 3.2.Lambda表达式

```java
//Lambda表达式
@Test
public void test02() {
    Comparator<String> com = (o1, o2) -> Integer.compare(o1.length(), o2.length());
    TreeSet ts = new TreeSet(com);

    TreeSet<String> treeSet = new TreeSet<>((Comparator<String>) (o1, o2) -> {
        return Integer.compare(o1.length(), o2.length());
    });
}
```

## 3.3.基础版本

````java
List<Employee> emps = Arrays.asList(
    new Employee(101, "张三", 18, 9999.99),
    new Employee(102, "李四", 59, 6666.66),
    new Employee(103, "王五", 28, 3333.33),
    new Employee(104, "赵六", 8, 7777.77),
    new Employee(105, "田七", 38, 5555.55)
);


//获取公司年龄小于35的员工信息
public List<Employee> filterEmployeeByAge(List<Employee> emps) {
    ArrayList<Employee> list = new ArrayList<>();
    for (Employee emp : emps) {
        if (emp.getAge() <= 35) {
            list.add(emp);
        }
    }
    return list;
}

@Test
public void test03() {
    List<Employee> employees = filterEmployeeByAge(emps);
    for (Employee employee : employees) {
        System.out.println(employee);
    }
}

//需求：获取公司中工资大于 5000 的员工信息
public List<Employee> filterEmployeeBySal(List<Employee> emps) {
    ArrayList<Employee> list = new ArrayList<>();
    for (Employee emp : emps) {
        if (emp.getSal() >= 5000) {
            list.add(emp);
        }
    }
    return list;
}

@Test
public void test04() {
    List<Employee> employees = filterEmployeeBySal(emps);
    for (Employee employee : employees) {
        System.out.println(employee);
    }
}
````

### 3.3.1.优化方案一：策略模式

```java
//策略接口
@FunctionalInterface
public interface MyPredicate<T> {
    boolean test(T t);
}
```

```java
//按年龄过滤的策略
public class FilterEmployeeByAge implements MyPredicate<Employee> {
    @Override
    public boolean test(Employee employee) {
        return employee.getAge() <= 35;
    }
}
```

```java
//按薪资过滤的策略
public class FilterEmployeeBySal implements MyPredicate<Employee> {
    @Override
    public boolean test(Employee employee) {
        return employee.getSal() >= 5000;
    }
}
```

```java
public List<Employee> filterEmployee(List<Employee> emps, MyPredicate<Employee> predicate) {
    ArrayList<Employee> list = new ArrayList<>();
    for (Employee employee : emps) {
        //使用的策略
        if (predicate.test(employee)) {
            list.add(employee);
        }
    }
    return list;
}

@Test
public void test05() {
    List<Employee> employees = filterEmployee(emps, new FilterEmployeeByAge());
    for (Employee employee : employees) {
        System.out.println(employee);
    }
    System.out.println("-----------------------");
    List<Employee> list = filterEmployee(emps, new FilterEmployeeBySal());
    for (Employee employee : list) {
        System.out.println(employee);
    }

}
```

### 3.3.2.优化方案二：匿名内部类

```java
@Test
public void test06() {
    //直接使用匿名内部类实现策略
    List<Employee> employees = filterEmployee(emps, new MyPredicate<Employee>() {
        @Override
        public boolean test(Employee employee) {
            return employee.getAge() <= 35;
        }
    });
    for (Employee employee : employees) {
        System.out.println(employee);
    }
}
```

### 3.3.3.优化方案三:Lambda

```java
@Test
public void test07() {
    List<Employee> employees = filterEmployee(emps, (e) -> e.getAge() <= 35);
    for (Employee employee : employees) {
        System.out.println(employee);
    }
}
```

### 3.3.4.优化方案四：Stream API

```java
@Test
public void test08() {
    emps.stream()
        .filter((e) -> e.getAge() <= 35)  //过滤条件
        .forEach(System.out::println);    //遍历输出

    System.out.println("---------------------");

    //遍历输出集合的名称
    emps.stream()
        .map(Employee::getEname)  //获取集合的名称
        .limit(2)   //限制条数
        .forEach(System.out::println);  //输出
}
```





​	