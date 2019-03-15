# 一、`Objects`

- `public static <T> T requireNonNull(T obj, String message)`



# 二、`try-with-resource`

```java
@Test
public void test01() {
    BufferedInputStream bin = null;
    BufferedOutputStream bout = null;
    try {
        bin = new BufferedInputStream(new FileInputStream(new File("test.txt")));
        bout = new BufferedOutputStream(new FileOutputStream(new File("out.txt")));
        int b;
        while ((b = bin.read()) != -1) {
            bout.write(b);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (bin != null) {
            try {
                bin.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bout != null) {
                    try {
                        bout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
```

```java
@Test
public void test02() {
    try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File("test.txt")));
         BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("out.txt")))) {
        int b;
        while ((b = bin.read()) != -1) {
            bout.write(b);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

为了能够配合`try-with-resource`，其中使用类必须实现`AutoClosable`接口，该接口的实现类需要重写`close`方法。

```java
public class Connection implements AutoCloseable {
    @Override
    public void close() throws Exception {
        System.out.println("正在关闭连接");
    }
}
```

- 在`catch(){}`中是不能访问到`try-with-resource`中的变量的

- 使用`try-with-recourse`时
  - `try`块中抛出的异常使用`e.getMessage()`获取异常信息；
  - 而调用`close()`方法抛出的异常使用`e.getSuppressed()`获取异常信息。
- `try-with-recourse`中定义了多个变量时，关闭顺序时从后向前。

## 2.1.原理



[]: https://juejin.im/post/5c62ba79f265da2de660e91e

