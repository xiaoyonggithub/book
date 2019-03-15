# 一、`volatile`

`volatile`修饰的共享变量，当多个线程操作时，可保证数据的可见性；即可相当于之间在主内存中操作数据。

- `volatile`是比`synchronized`更轻量级的同步策略 
- `volatile`不具有互斥性
- `volatile`不能保证变量的原子性

## 1.1.内存可见性

内存可见性：多个线程操作共享数据时，彼此之间的数据不可见

```java
public class MemoryRunnable implements Runnable {
	//共享变量
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("flag=true");
        flag = true;
    }
}
```

```java
@Test
public void test01(){
    MemoryRunnable runnable = new MemoryRunnable();
    Thread thread = new Thread(runnable);
    thread.start();
    while(true){
        if(runnable.isFlag()){
            System.out.println("退出循环");
            break;
        }
    }
}
```

> 此时只会输出“flag=true”，且不能退出循环

解决方法：使用`volitille`修饰共享数据

```java
private volitile boolean flag = false;
```



# 二、原子性

- 内部使用`volitile`修饰，保证内存可见性
- CAS（Compare-And-Swap）算法保证数据的原子性
  - CAS算法是硬件对并发操作共享数据的支持，是一种无锁算法
  - CAS包含三个操作数
    - 内存值`V`
    - 预估值（旧值）`A`
    - 更新值`B`
    - 当且仅当`V==A`时，才赋值`V=B`，否则不操作

```java
public class AtomicTest {

    public static void main(String[] args) {
        AtomicRunnable runnable = new AtomicRunnable();
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}

class AtomicRunnable implements Runnable {
    //共享变量
    public int serialNumber = 0;

    public int getSerialNumber() {
        return serialNumber++;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getSerialNumber());
    }
}
```

此时对于共享变量serialNumber，进行serialNumber++操作时，由于serialNumber++不是原子性操作，故可能会出现重复的数据。

解决方法：使用AtomicInteger类，保证serialNumber的自增操作是一个原子操作

```java
public class AtomicTest {

    public static void main(String[] args) {
        AtomicRunnable runnable = new AtomicRunnable();
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }

}

class AtomicRunnable implements Runnable{

    public AtomicInteger serialNumber = new AtomicInteger(0);

    public int getSerialNumber() {
        return serialNumber.getAndIncrement();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getSerialNumber());
    }
}
```



## 2.1.模拟CAS算法

```java
public class CompareAndSwap {
    
    //使用main()方法测试cas算法，使用@Test测试有些问题
    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectedValue = cas.get();
                    boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
                    System.out.println(b);
                }
            }).start();
        }
    }

    //内存值
    private int value;

    //获取内存值
    public synchronized int getValue() {
        return value;
    }

    /**
     *  比较值
     * @param expectedValue 预估值
     * @param newValue 新值
     */
    public synchronized int compareAndSwap(int expectedValue,int newValue){
        int oldValue = value;
        if(oldValue == expectedValue){
            this.value = newValue;
        }
        return oldValue;
    }

    /**
     * 设置新值
     * @param expectedValue 预故值
     * @param newValue 新值
     * @return
     */
    public synchronized  boolean compareAndSet(int expectedValue,int newValue){
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
```



## 2.2.CAS算法存在的问题

- ABA问题
- 循环时间长，开销大
- 只能保证一个共享变量的原子性操作



# 三、`ConcurrentHashMap`

`ConcurrentHashMap`采用锁分段机制

`concurrentLevel=16`每16个数据分为一个段，每个段都是独立的锁



## 3.1.`CopyOnWriteArrayList`

- 并发迭代操作多时可使用该类
- 添加操作多时，效率低，不建议使用
  - 原因：每次写入数据都复制一份，效率低

> List在迭代遍历时，进行插入操作

```java
public static void main(String[] args) {
    final ArrayList<Object> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    System.out.println(list);

    for (int i = 0; i < 10; i++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    Iterator<Object> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        list.add(4);
                    }
                }
            }
        }).start();
    }
}
```

此时会报并行修改异常`java.util.ConcurrentModificationException`，解决方法：使用`CopyOnWriteArrayList`

```java
public static void main(String[] args) {
    //该类每次添加会复制一份
    CopyOnWriteArrayList list = new CopyOnWriteArrayList();
    list.add(1);
    list.add(2);
    list.add(3);
    System.out.println(list);

    for (int i = 0; i < 10; i++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    Iterator<Object> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Object next = iterator.next();
                        list.add(4);
                    }
                }
            }
        }).start();
    }
}
```

## 3.2.`CopyOnWriteArraySet`





# 四、闭锁

`CountDownLatch`是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待；即某些运算完成，只有是其它线程所有的运算都完成时，当前线程运算才能执行

> 计算多个线程的执行时间

```java
public class DownLatchRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 50000; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
}
```

```java
public void test03() {
    CountDownLatch latch = new CountDownLatch(5);
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5; i++) {
        new Thread(new DownLatchRunnable()).start();
    }
    long end = System.currentTimeMillis();
    System.out.println((end - start) + "ms");
}
```

此时不能计算出所有线程执行的时间，因为可能有的线程还没执行完成，就计算了时间（不准确）

```java
public class DownLatchRunnable implements Runnable {

    private CountDownLatch latch;

    public DownLatchRunnable(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try{
            for (int i = 0; i < 50000; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        }finally {
            //每执行一个线程，等待数减1
            latch.countDown();
        }
    }
}
```

```java
public void test03() {
    //创建闭锁的数目
    CountDownLatch latch = new CountDownLatch(5);
    long start = System.currentTimeMillis();
    for (int i = 0; i < 5; i++) {
        new Thread(new DownLatchRunnable(latch)).start();
    }
    long end = System.currentTimeMillis();
    //等待所有其他线程执行完成
    try {
        latch.await();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println((end - start) + "ms");
}
```

此时就会等待所有的线程执行完成后，才计算执行时间

​	

# 五、`Callable`





# 六、线程池



![img](../../images/16974858b0a6f8af)