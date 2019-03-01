# 一、`Stream`

流是数据渠道，用于操作数据源（集合、数组等）所生成的元素序列；集合讲的是数据，流讲的是计算

- Stream不会自己存储元素
- Stream不会改变源对象，会返回一个持有结果的新Stream
- Stream操作是延迟执行的，即等到需要结果的时候才执行

## 1.1.Stream API的操作步骤

- 创建Stream
- 中间操作，对数据源的数据进行操作
- 终止操作（终端操作），执行中间操作链，获取结果

## 1.2.创建Stream的方式

> 使用Collection提供了两个方法stream()与parallelStream()

```java
ArrayList list = new ArrayList();
//获取一个顺序流
Stream stream = list.stream();
//获取一个并行流
Stream parallelStream = list.parallelStream();
```

> 使用Arrays中的stream()获取一个数据流

```java
Integer[] nums = new Integer[]{12,14,1,5};
//将数组转化为一个数据流
Stream<Integer> stream = Arrays.stream(nums);
```

> 使用Stream的静态方法of()，可以接受任意数据类型

```java
Stream<Integer> stream= Stream.of(1, 2, 2, 3, 3);
Stream<Employee> empStream = Stream.of(new Employee());
```

>创建无限流

```java
//迭代生成所有偶数
Stream<Integer> stream = Stream.iterate(0, (x) -> x + 2);
stream.forEach(System.out::println);

//生成随机数
Stream<Double> generate = Stream.generate(Math::random).limit(3);
generate.forEach(System.out::println);
```

## 1.3.中间操作

中间操作是短路的，不会立即执行，只有调用终止操作时，才一起执行

### 1.3.1.筛选和切片

- `filter(Predicate<? super T> predicate)`从流中排除某些元素，保留符合条件的元素
- `limit(long maxSize)`截取流，使元素不超过指定数量
- `skip(long n)`跳过元素，返回一个扔掉前n个元素的流；若流中元素个数小于n，则返回一个空流
- `distinct()`去除重复元素，使用`equals()`和`hashCode()`方法

```java
Stream<Employee> stream = emps.stream()
    .filter((x) -> x.getAge() > 10)
    .limit(3)
    .skip(2)
    .distinct();
stream.forEach(System.out::println);
```

### 1.3.2.映射

- `map(Function<? super T, ? extends R> mapper)`将元素转化为其它形式或提取信息；接收一个函数作为参数，会把该函数应用到每个元素上，并将其映射成一个新的元素
- `flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)`接收一个函数作为参数，将流中的每个值转化为另一个流，并将所有流连接成一个流返回

```java
//map
String[] strs = new String[]{"aaa","bbb","ccc","sss"};
Arrays.stream(strs).map((x) -> x.toUpperCase())
    .forEach(System.out::println);

//map与flatMap的区别
//map 类似于二位数组[{1,2},{3,4},{5,6}]
Arrays.stream(strs).map((x) -> filterCharacter(x))
    .forEach((x) -> x.forEach(System.out::println));
Stream<Stream<Character>> stream = Arrays.stream(strs)
    .map(StreamTest::filterCharacter);
stream.forEach(System.out::println);
//flatMap  [{1,2},{3,4},{5,6}]-->[1,2,3,4,5,6]
Arrays.stream(strs).flatMap((x) -> filterCharacter(x)).forEach(System.out::println);
```

### 1.3.3.排序

- `sorted()`自然排序，若对象为没有实现Comparable接口，可能会出错	
- `sorted(Comparator<? super T> comparator)`定制排序

```java
//按姓名排序
emps.stream().map(Employee::getEname).sorted().forEach(System.out::println);
```

```java
//按年龄排序，若年龄相同则按薪资排序
emps.stream().sorted((x, y) -> {
    if (x.getAge() == y.getAge()) {
        return Double.compare(x.getSal(), y.getSal());
    } else {
        return Integer.compare(x.getAge(), y.getAge());
    }
}).forEach(System.out::println);
```

```java
//sorted()对对象排序需指定排序规则，默认调用对象的compareTo()方法
emps.stream().sorted().forEach(System.out::println);
```

```java
public class Employee implements Comparable<Employee> {
    @Override
    public int compareTo(Employee e) {
        if (this.getAge() == e.getAge()) {
            return Double.compare(this.getSal(), e.getSal());
        }else{
            return Integer.compare(this.getAge(), e.getAge());
        }
    }
}
```

## 1.4.终止操作

> 准备工作

```java
List<Employee> emps = Arrays.asList(
    new Employee(102, "李四", 79, 6666.66, Status.BUSY),
    new Employee(101, "张三", 18, 9999.99, Status.FREE),
    new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
    new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
    new Employee(104, "赵六", 8, 7777.77, Status.FREE),
    new Employee(104, "赵六", 8, 7777.77, Status.FREE),
    new Employee(105, "田七", 38, 5555.55, Status.BUSY)
);
```

流进行了终止操作后，就不能再次进行操作

- `boolean allMatch(Predicate<? super T> predicate)`检查是否匹配所有的元素
- `boolean anyMatch(Predicate<? super T> predicate)`检查是否至少匹配一个元素
- `boolean noneMatch(Predicate<? super T> predicate)`检查是否没有匹配元素
- `Optional<T> findFirst()`返回第一个元素
- `Optional<T> findAny()`返回当期流的任意元素
- `long count()`返回流中元素的总个数
- `Optional<T> min(Comparator<? super T> comparator)`返回流中的最大值
- `Optional<T> max(Comparator<? super T> comparator)`返回流中的最小值

```java
boolean b = emps.stream().allMatch((e) -> Status.BUSY.equals(e.getStatus()));
System.out.println(b);

b = emps.stream().anyMatch((e) -> Status.BUSY.equals(e.getStatus()));
System.out.println(b);

b = emps.stream().noneMatch((e) -> Status.VOCATION.equals(e.getStatus()));
System.out.println(b);

Optional<Employee> first = emps.stream().findFirst();
System.out.println(first);

Optional<Employee> any = emps.stream().findAny();
System.out.println(any);

long count = emps.stream().count();
System.out.println(count);

Optional<Integer> min = emps.stream().map(Employee::getAge).min(Integer::compareTo);
System.out.println(min);

Optional<Double> max = emps.stream().map(Employee::getSalary).max(Double::compareTo);
System.out.println(max);
```

### 1.4.1.归约

- `T reduce(T identity, BinaryOperator<T> accumulator)`将流中的元素反复结合得到一个值
- `Optional<T> reduce(BinaryOperator<T> accumulator)`

```java
//计算和
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Integer sum = list.stream().reduce(0, (x, y) -> x + y);
System.out.println(sum);

//计算薪资和
Optional<Double> reduce = emps.stream().map(Employee::getSalary).reduce(Double::sum);
System.out.println(reduce);
```

### 1.4.2.收集

- `<R, A> R collect(Collector<? super T, A, R> collector)`将流转转化为其它形式；接收一个Collector接收的实现，用于给Stream中的元素做汇总信息

```java
//提取姓名到List中
List<String> collect = emps.stream().map(Employee::getName).collect(Collectors.toList());
System.out.println(collect);

//提取姓名到Set中，可去除重复元素
Set<String> set = emps.stream().map(Employee::getName).collect(Collectors.toSet());
System.out.println(set);

//提取姓名到指定的集合中
HashSet<String> hashSet = emps.stream().map(Employee::getName).collect(Collectors.toCollection(HashSet::new));
System.out.println(hashSet);
```

```java
//获取最高薪资
Optional<Double> max = emps.stream().map(Employee::getSalary).max(Double::compare);
System.out.println(max.get());

max = emps.stream().map(Employee::getSalary).collect(Collectors.maxBy(Double::compareTo));
System.out.println(max.get());
```

```java
//计算和
Double sum = emps.stream().collect(Collectors.summingDouble(Employee::getSalary));
System.out.println(sum);

//计算平均值
Double avg = emps.stream().collect(Collectors.averagingDouble(Employee::getSalary));
System.out.println(avg);

//统计数量和
Long count = emps.stream().collect(Collectors.counting());
System.out.println(count);

//摘要统计，包含总数量，综合，最小值，最大值，平均值
DoubleSummaryStatistics collect = emps.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
System.out.println(collect);
```

### 1.4.3.分组

```java
//按状态分组
Map<Status, List<Employee>> statusMap = emps.stream().collect(Collectors.groupingBy(Employee::getStatus));
System.out.println(statusMap);

//多级分组，先按状态分组，再按年龄分组
Map<Status, Map<String, List<Employee>>> map = emps.stream().collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
    if (e.getAge() > 60) {
        return "老年";
    } else if (e.getAge() >= 35) {
        return "中年";
    } else {
        return "青年";
    }
})));
System.out.println(map);
```

### 1.4.4.分区

- `Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate)`分区，将满足与不满足条件的分区存放在不同的List中

```java
//分区
Map<Boolean, List<Employee>> map = emps.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 5000));
System.out.println(map);
//{false=[Employee [id=103, name=王五, age=28, salary=3333.33, status=VOCATION]],
// true=[Employee [id=102, name=李四, age=79, salary=6666.66, status=BUSY]}
Set<Boolean> keySet = map.keySet();
keySet.stream().map((x) -> map.get(x)).forEach(System.out::println);
```

## 1.5.惰性求值





## 1.6.并行流

- 并行流:把一个内容分为多个数据块，并使用不同的线程分别处理每个数据块的流
- 串行流:使用一个线程处理数据流

### 1.6.1.`ForkJoin`

`Fork/Join`框架就是将一个大任务拆分（fork）若干个小任务（拆到不可在拆时），最后将一个个的小任务运算的结果进行join汇总

```java
public class ForkJoinCalculate extends RecursiveTask<Long> {

    private long start;
    private long end;

    //临界值
    public static final long THRESHOLD = 10000L;

    public ForkJoinCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long len = end - start;
        if (len < THRESHOLD) {
            long sum = 0;
            for (long i = start; i < end; i++) {
                sum += i;
            }
            return sum;
        } else {
            long middle = (start + end) / 2;
            ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
            //拆分
            left.fork();

            ForkJoinCalculate right = new ForkJoinCalculate(middle + 1, end);
            right.fork();

            return left.join() + right.join();
        }
    }
}
```

```java
//计算数据和
public void test01() {
    long start = System.currentTimeMillis();
    ForkJoinPool pool = new ForkJoinPool(10);
    ForkJoinTask<Long> task = new ForkJoinCalculate(0L, 10000000000L);//891 5613

    Long sum = pool.invoke(task);
    System.out.println(sum);

    long end = System.currentTimeMillis();
    System.out.println("执行的毫秒数：" + (end - start));
}
```

`ForkJoin`框架与传统线程池的区别

- 传统的线程池中，若一个正在执行的任务由于某些原因无法继续执行，此时该线程会处于等待状态

- `FrokJoin`框架采用“工作窃取”模式`(work-stealing)`，若某个子问题由于等待另一个子问题的完成而无法继续执行，那么处理该子问题的线程，就会去寻找其它未执行的子问题执行；这样就减少了线程的等待时间，提高了执行效率

### 1.6.2.并行流

并行流就是采用`ForkJoin`框架实现的

```java
public void test03(){
    long start = System.currentTimeMillis();

    long sum = LongStream.rangeClosed(0L, 10000000000L).parallel().sum();
    System.out.println(sum);

    long end = System.currentTimeMillis();
    System.out.println("执行的毫秒数："+(end - start));//707 4656
}
```

# 二、`Optional`

`Optional`容器类用于避免空指针异常

 * 	`Optional.of(T t)`:创建一个 Optional 实例
* 	`Optional.empty()`:创建一个空的 Optional 实例
* 	`Optional.ofNullable(T t)`:若 t 不为 null,创建 Optional 实例,否则创建空实例
* 	`isPresent()`:判断是否包含值
* 	`orElse(T t)`:设置默认值，如果调用对象包含值，返回该值，否则返回t
* 	`orElseGet(Supplier s)`:设置默认值，如果调用对象包含值，返回该值，否则返回 s 获取的值
* 	`map(Function f)`:如果有值对其处理，并返回处理后的Optional，否则返回 Optional.empty()
* 	`flatMap(Function mapper)`:与 map 类似，要求返回值必须是Optional

```java
//创建Optional实例，Optional.of()不能创建空实例
Optional<Employee> employee = Optional.of(new Employee());
System.out.println(employee.get());

//创建Optional空实例
Optional<Object> empty = Optional.empty();

//Optional.ofNullable(T t)
Optional<Object> o = Optional.ofNullable(null);
Optional<Employee> emp = Optional.ofNullable(new Employee());

//判断是否包含值
boolean present = emp.isPresent();
System.out.println(present);
```

```java
Optional<Employee> optional = Optional.ofNullable(new Employee());
if(optional.isPresent()){
    //获取值
    System.out.println(optional.get());
}
//为空，设置默认值
Employee defalut = optional.orElse(new Employee("张三"));

Employee employee = optional.orElseGet(() -> new Employee());
```

```java
Optional<Employee> optional = Optional.ofNullable(new Employee(1000, "张三", 20, 6000.0, Status.BUSY));
//获取姓名
Optional<String> name = optional.map(Employee::getName);
//此时的返回值必须是Optional对象
name = optional.flatMap((e) -> Optional.of(e.getName()));
```

## 2.1.应用

> 原来的方式

```java
public class Godness {
    private String name;

    public Godness(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Godness{" +
            "name='" + name + '\'' +
            '}';
    }
}
```

```java
public class Man {

    private Godness godness;

    public Man(Godness godness) {
        this.godness = godness;
    }

    public Godness getGodness() {
        return godness;
    }

    public void setGodness(Godness godness) {
        this.godness = godness;
    }
}
```

```java
@Test
public void test07(){
    Man man = new Man(null);
    String name = getGodnessName(man);
    System.out.println(name);

}

public String getGodnessName(Man man){
    if(man != null){
        Godness godness = man.getGodness();
        if(godness != null){
            return godness.getName();
        }
    }
    return null;
}
```

> 使用Optional的方式

```java
public class NewMan {

    private Optional<Godness> godness = Optional.empty();

    private Godness god;

    public NewMan() {
    }

    public NewMan(Optional<Godness> godness) {
        this.godness = godness;
    }

    public Optional<Godness> getGodness() {
        return godness;
    }

    public void setGodness(Optional<Godness> godness) {
        this.godness = godness;
    }

    public Optional<Godness> getGod() {
        return Optional.of(god);
    }
}

```

```java
@Test
public void test07(){
    Optional<Godness> godness = Optional.ofNullable(new Godness("小龙女"));
    Optional<NewMan> man = Optional.ofNullable(new NewMan(godness));
    String name = getGodnessName(man);
    System.out.println(name);
}

public String getGodnessName(Optional<NewMan> man){
    return man.orElse(new NewMan()) //设置默认值
        .getGodness()
        .orElse(new Godness("女神"))
        .getName();
}
```



```java
Passenger passenger = SomeMehtod.getPassenger();
if(passenger != null && passenger.getCert() != null 
   && passenger.getCert().getPersonalInfo != null){   
    return passenger.getCert().getPersonalInfo().getName();
}
else return "default name";

//有更差的实践是写成下面的多重嵌套if模式，这样在真实情况下很容易缩进七八层，甚至十几层，代码可读性基本上就没了。
if(passenger != null){
    if (passenger.getCert() != null){
        if(passenger.getCert().getPersonalInfo() != null){
             return passenger.getCert().getPersonalInfo().getName();
        }else return "default name"
    }
}

//还有更差的实践，比如生成很多只用一次的中间对象。对了，就是把 Cert，PersonalInfo 再都new出来。代码太难看，就不补全了。
```

```java
Passenger passenger = SomeMehtod.getPassenger();
return Optional.ofNullable(passenger)
               .map(Passenger::getCert)
               .map(Cert::getPersonalInfo)
               .map(PersonalInfo::getName)
               .orElse("default name");
```

```java
//需要抛出一个空指针异常而不是返回默认值
Passenger passenger = SomeMehtod.getPassenger();
return Optional.ofNullable(passenger)
               .map(Passenger::getCert)
               .map(Cert::getPersonalInfo)
               .map(PersonalInfo::getName)
               .orElesThrow(NullPointerException::new)
```





# 三、接口

## 3.1.接口的默认方法

接口中的默认方法使用**类优先**原则，在接口中定义了一个默认方法，而在接口中定义了一个同名的方法

- 选择父类中的方法；若父类中的同名方法提供了实现，则该方法会覆盖接口的同样的方法
- 接口冲突；实现的多个接口时，不同接口中有相同方法的默认实现，此时实现接口的类需要重写该方法，从而指定调用那个实现

```java
public interface MyFun {

    //默认方法
    default String getName(){
        return "默认方法";
    }
}
```

## 3.2.接口的静态方法

```java
public interface MyFun {
    //默认方法
    default String getName(){
        return "默认方法";
    }
}
```

```java
MyFun.getAge();
```

## 3.3.接口与抽象方法的区别

在`JDK1.8`以后

- 接口可以多实现；而抽象方法只能单继承

---

# 四、时间

## 4.1.时间的安全问题

传统的日期类`(Date,Calender,SimpleDateFormat)`都存在线程安全问题

```java
public void test08() throws ExecutionException, InterruptedException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Callable<Date> task = new Callable<Date>() {
        @Override
        public Date call() throws Exception {
            return sdf.parse("20150113");
        }
    };
    ExecutorService pool = Executors.newFixedThreadPool(10);

    ArrayList<Future<Date>> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        list.add(pool.submit(task));
    }
    for (Future future : list) {
        System.out.println(future.get());
    }
}
```

此时存在异常`java.util.concurrent.ExecutionException: java.lang.NumberFormatException: empty String`

> 解决方式

```java
public class DateFormatThreadLocal {

    private static final ThreadLocal<DateFormat> tl = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public static final Date convert(String source) throws ParseException {
        return tl.get().parse(source);
    }

}
```

```java
public void test09() throws ExecutionException, InterruptedException {
    Callable<Date> task = new Callable<Date>() {
        @Override
        public Date call() throws Exception {
            return DateFormatThreadLocal.convert("20181118");
        }
    };

    ExecutorService pool = Executors.newFixedThreadPool(10);
    ArrayList<Future<Date>> list = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
        Future<Date> future = pool.submit(task);
        list.add(future);
    }

    for (Future<Date> future : list) {
        System.out.println(future.get());
    }
}
```

## 4.2.Java8时间API

- `java.time`:Java日期和时间API的基础包，这些类是不可变的和线程安全的
- `java.time.chrono`:非ISO的日历系统定义了一些泛化的API，使用`AbstractChronology`类可创建自己的日历系统
- `java.time.format`:格式化和解析日期时间的对象的类，一般不直接使用，因为`java.time`中的类一提供相应的方法

- `java.time.temporal`:一些时态对象，用于查找特定的日期时间，如某个月的最后一天
- `java.time.zone`:时区处理的类

### 4.2.1.日期基本类

- `LocalDate`:创建日期
- `LocalTime`:创建时间
- `LocalDateTime`:创建日期和时间







# 五、注解

## 5.1.重复注解



## 5.2.类型注解



