## 一、填充字符串

```java
/**
    *curno表示需要被填充的字符
    *length表示要填充的长度
    *fillStr表示需要填充的字符
    *根据要求来填充字符串
    */
public static String getCurNo(int curno, int length, String fillStr) {
    int temp = curno;
    StringBuffer sb = new StringBuffer(length);
    int count = 0;
    while (curno / 10 != 0) {
        curno = curno / 10;
        count++;
    }
    int size = length - count - 1;
    for (int i = 0; i < size; i++) {
        sb.append(fillStr);
    }
    sb.append(temp);
    return sb.toString();
}
```

