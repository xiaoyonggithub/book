

- `JavaScript`是一门单线程语言，是基于回调队列的

- `v8`的主要包含两个组件

  - `Memory Heap`内存分配发生的地方
  - `Call Stack`代码执行时栈帧的位置，是一种数据结构，记录程序的位置
    - 进入函数，就把它放在堆栈的顶部，若从函数返回，就将其从堆栈顶部弹出
    - 调用栈的每一个条目，叫做`栈帧（Stack Frame）`，这就是异常抛出时堆栈追踪的构造方式
    - `爆栈`当达到调用栈的最大大小时发生

- 异步回调


# 一、数据类型

- `script`标签引用的外部文件，标签内的代码就不会执行了

- 字面量：一些不可改变的值，可直接使用（但一般不使用字面量），如`1,2,3,4...`

- 变量：可以保存字面量，变量的值可任意改变

## 1.1.基本数据类型

基本数据类型：`String,Number,Boolean,Undefined,Null`

### 1.1.1.String

- JS的字符串需使用引号引起来
- 字符串中同类型引号不能嵌套 
- 特殊符号需要使用`\`转义

### 1.1.2.Number

* 整数和小数都是Number类型
* Number.MAX_VALUE  //最大值 1.7976931348623157e+308
* Number.MIN_VALUE  //最小正值 5e-324
* Infinity    //表示正无穷
* -Infinity   //表示负无穷
* NAN  //表示一个特殊的数据值，Not A Number
* 整数的计算可以得到精确的值，浮点数计算可能得一个不精确的值

## 1.1.3.Boolean

* Boolean只有两个值：true和false

### 1.1.4.Undefined

* Undefined类型的值只有undefined
* undefined表示声明了一个变量，但是没有赋值

### 1.1.5.Null

* Null类型的值只有null
* null表示一个空的对象

### 1.1.6.typeof

typeof查看变量的类型

```js
var str = '123';
var num = 123;
console.log(typeof str);       //string
console.log(typeof num);       //number
console.log(typeof Infinity);  //number
console.log(typeof NaN);       //number
console.log(typeof null);      //object
console.log(typeof undefined); //undefined
```

### 1.1.7.强制类型的转化

指将一个数据类型转化为其他的数据类型，主要指将其他类型的数据转化为String,Number和Boolean

#### 1.1.7.1.其他数据类型转String

- 调用toString()方法转化，不会影响原变量，只是把转化的结果值返回

- ```js
  var num = 123;
  var res = num.toString();
  console.log(res); 			//123
  console.log(typeof num);     //number
  console.log(typeof res);     //string
  
  var flag = true;
  var res = flag.toString();
  console.log(res);             //true
  console.log(typeof flag);     //boolean
  console.log(typeof res);      //string
  
  注意：null和undefined没有toString()方法，调用会报错
  ```

  调用String()函数

  - 针对String和Boolean还是调用toString()方法
  - 针对null和undefined就不会调用toString()，而是直接转化为字符串
  - 不能影响原始的变量的类型

  ```js
  var num = 123;
  var str = String(num);
  console.log(str);        //123
  console.log(typeof num); //number
  console.log(typeof str); //string
  
  var a = null;
  console.log(String(a));   //null
  
  var b = undefined;
  console.log(String(b));   //undefined
  ```

- 与空串相加（""），隐式转化

  ```js
  var c = 123;
  c = c + ""; //123 此时类型为string
  ```




#### 1.1.7.2.其他数据类型转Number

- 使用Number()函数

  - 若是纯数字字符串，就直接转为数字

  ```js
  var str = '123';
  var num = Number(str);
  console.log(num);       //123
  console.log(typeof str);//string
  console.log(typeof num);//number
  ```

  * 若字符串中包含非数字字符串，就转化为NAN

  ```js
  var str = 'ab12';
  console.log(Number(str)); //NaN
  ```
  - 若字符串是空串或只包含空格的字符串，就转化为0 

  ```js
  console.log(Number(''));    //0
  console.log(Number('   ')); //0
  console.log(Number('\t'));  //0
  console.log(Number('\n'));  //0
  ```

  - Boolean --> Number,true转为1，false转为0

  ```js
  console.log(Number(true));  //1
  console.log(Number(false)); //0
  ```

  * null --> number : 0 

  ```js
  console.log(Number(null));  //0
  ```

  - undefined --> number: NaN

  ```js
  console.log(Number(undefined)); //NaN
  ```

- parseInt()和parseFloat()，只能用于将字符串转为数字，可以将字符串中有效的数字取出来

```js
var str = '123px';
console.log(parseInt(str));    //123

str = '123b456';
console.log(parseInt(str));   //123

str = 'b234'; 
console.log(parseInt(str));   //NaN 

str = '123.653px'; 
console.log(parseInt(str));   //123，parseInt()只取整数

str = '123.653px';
console.log(parseFloat(str));  //123.653

str = '123.653.2323px'; 
console.log(parseFloat(str));   //123.653 
```

若操作了非字符串，会先将其转化为字符串在进行处理

```js
 var a = true;
console.log(parseInt(a));  //NaN
```

注意：parseInt()可以对数字进行取整

```js
console.log(parseInt(123.456));  //123
```

- 隐式转化（`-1 、*1、/1`）

```javascript
var res = '123';//string
res = res - 1; //number
res = res * 1; //number
res = res / 1; //number
console.log(typeof res);
```

* 隐式转化（+），使用正号`+`运算符将其转化为Number

```javascript
var res = '123';
res = +res;//number 123
res = +true;//number 1
res = +false;//number 0
res = +null;//number 0
res = +undefined;//number  NaN
```



### 1.1.7.3.其他数据类型转Boolean

* 使用Boolean()转化

  *  数字转Boolean，除了0和NaN，其余的都是true

  * ```javascript
    var a = 123;  //true
    a = -123;  //true
    a = 0;  //false
    a = Infinity;  //true 
    a = NaN;  //true 
    console.log(Boolean(a));  
    ```

    字符串 -->  Boolean，除了空串（""），其余的都是true

    ```javascript
    var a = "";  //false
    a = "123"// true
    a = "  "; //true
    console.log(Boolean(a));  
    ```

  * null --> Boolean 为false

    ```js
    var a = null;  //false
    console.log(Boolean(a));  
    ```

  * undefined --> Boolean 为false

    ```js
    var a =undefined;  //false
    console.log(Boolean(a));  	
    ```

  * object --> Boolean 为true

- 隐式转化（!!）

```javascript
var a = !!0;
console.log(a); //false
console.log(typeof a);//boolean
```



### 1.1.8.其它进制的数据

```js
console.log(0x10);  //16进制 16
console.log(070);   //8进制 56 
console.log(0b10);  //二进制 2 有的浏览器不支持
```

注意："070"字符串转数字有些浏览器会当成8进制解析，有些会当成10进制解析，统一解析的方式

```js
parseInt（"070"，10）;  //指定为按十进制解析
```



## 1.2.引用数据类型

引用数据类型：Object

### 1.2.1.对象的分类

- 内建对象：由ES标准中定义的对象，在任何的ES实现中都可以使用，如：Math、String、Number、Boolean、Function、Object
- 宿主对象：由JS运行环境提供的对象，比如浏览器提供的BOM和DOM
- 自定义对象

### 1.2.2.创建对象

1. 使用构造函数创建

```js
var obj = new Object();
obj.name = "zhangsan";
```

2.使用对象字面量创建对象

```javascript
var obj = {};
obj.name = "lisi";
```

```javascript
var obj = {name:"李四",age:28}; //对象的属性可以不加引号，建议不加，对于特殊的属性名称需加引号
```

```js
var obj = {
  name : "lisi",
  address : {
	id : "CD0001",
    name : "成都市"  
  },
    getName:function(){  //对象的方法
        return this.name;   //使用对象自己的属性需使用this
    }  
};
```

3.创建函数对象

```js
//定义函数，此种方式基本不使用
var fun = new Function("console.log('这是函数的代码')");
fun();  //调用函数
```

4. 工厂方法创建对象

```javascript
//通过工厂方法批量创建对象
function createPerson(name,age,gender){
    return {
        name:name,
        age:age,
        gender:gender,
        getName:function(){
            console.log(this.name);
        }
    }
}

var obj1 = createPerson('张三',17,'男');
var obj2 = createPerson('xiaohong',18,'女');

console.log(obj1);
console.log(obj2);
```





### 1.2.3.使用特殊的属性名

```js
var obj = new Object();
obj["123"] = "zhangsan";
console.log(obj["123"]);

//使用[]操作属性更方便，可以传递变量
var key = "id";
var val = "123";
obj[key] = val;
```

### 1.2.4.检查对象是否包含某个属性

```js
console.log("name" in obj);  //true
```

### 1.2.5.枚举对象的属性

```javascript
var obj = {
    name:'lisi',
    age:15,
    address:'成都市'  
};
//枚举对象的属性
for(var attr in obj){
    console.log(attr);
    console.log(obj[attr]);
}    
```

注意：使用`in`检查对象属性时，对象原型中的属性也会算作对象的属性

```javascript
function Person(){}
Person.pertotype.name = "原型中的name";

var obj = new Person();
console.log("name" in obj); //true
```

可使用`hasOwnProperty`来区分是否是原型的属性

```javascript
//判断对象本身对否包含name属性
console.log(obj.hasOwnProperty("name")); //true

console.log(obj.hasOwnProperty("hasOwnProperty")); //fasle
console.log(obj.__proto__.hasOwnProperty("hasOwnProperty")); //fasle,hasOwnProperty方法不在原型对象中
console.log(obj.__proto__.__proto__.hasOwnProperty("hasOwnProperty")); //true
//表示hasOwnProperty在obj原型对象的原型对象中
```



## 1.3.数据的储存

基本数据类型保存在栈内存中，值与值之间是独立存在的

对象是保存在堆内存中，变量保存的是对象的地址

## 1.4.内建对象

### 1.4.1.数组

- 数组也是对象，数组使用索引操作数组；而对象是使用属性名操作对象
- 注意：读取数组不存在的索引不会报错
- 数组中元素可以是任意数据类型，可以是数组、对象和函数等

#### 1.4.1.1.创建数组

1. 使用构造函数创建数组

```javascript
var arr = new Array();
arr[0] = 0;
arr[1] = 1;
arr[arr.length] = arr.length;//向数组最后添加元素
arr[arr.length] = arr.length;
console.log(arr.length); //数组的长度

var arr = new Array(1,2,3,4);
var arr = new Array(10);//创建长度为10的数组
var arr = [10];//创建一个值为10数组，
```

2. 使用字面量创建数组

```javascript
var arr = [];
var arr = [1,2,3,4];
```

#### 1.4.1.2.数组函数

1. forEach()将一个函数作为参数

```javascript
var arr = ['张三','李四','杨帆','牧尘','吕树'];
arr.forEach(function(value,index,obj){
    console.log(a);  //张三
    console.log(b);  //0
    console.log(c);  //['张三','李四','杨帆','牧尘','吕树']
});
//第一个参数：当前正在遍历的元素
//第二个参数：当前正在遍历元素的索引
//第三个参数：当前正在遍历的数组
```

注意：IE8不支持forEach()

2. silce(start，end)截取数组的一部分返回，不会改变原始数组，[start,end）

```javascript
arr.silce(2,-2); //-2表示截取到倒数第二个元素，即倒序截取
```

3.splice(start,len,item1,item2...)删除数组指定元素，会影响原始数组

```javascript
arr.splice(2,3,'小鱼','奥拓','杨洋');//替换第二个元素后的三个元素
```

4.concat(arr,..),连接多个数组，不会影响原数组

```javascript
var arr2 = ['雷丝丝'];
arr.concat(arr2,'王国',arr3);
```

5.join(str),将数组转为字符串

```javascript
arr.join(":"); //张三:李四:杨帆:牧尘:吕树
```

6. reverse()反转数组，会影响原数组

7. sort()数组排序，注意是按Unicode编码排序

```js
//定义排序规则
var arr = [3,7,13,2,9,1,7,2,5,-1,-3,10];
arr.sort(function(a,b){  //传入回调函数，排序的规则;参数的不确定，但一定是a在b的前面
    //若返回值小于等于0,不会交换位置
    //若返回值大于0,会交换位置
    if(a > b){
        return 1;
    }else if(a < b){
        return -1;
    }else{
        return 0;
    }
});    
console.log(arr);//[-3, -1, 1, 2, 2, 3, 5, 7, 7, 9, 10, 13]
```

简化：

```javascript
arr.sort(function(a,b){  
	return a-b; //升序排列
}); 
```

### 1.4.2.`Date`

```javascript
var d = new Date();//当前时间
d = new Date('09/16/2018 21:44:04');//指定日期
console.log(d);
console.log(d.getDate());//日期 16
console.log(d.getDay());//周几 0(周日)
console.log(d.getMonth());//月份[0-11] 8(9月)
console.log(d.getFullYear());//年份 2018
console.log(d.getTime());//时间戳,即距离1970/01/01 00:00:00的毫秒数
console.log(Date.now());//当前时间的时间戳
```

### 1.4.3.`Math`

Math与其他对象不同，不是一个构造函数，是一个工具类，封装了数学相关的属性和方法

1. 生成随机数

```javascript
console.log(Math.random());//生成0-1之间的随机数
//Math.round(Math.random()*x)  生成0-x之间的随机数
console.log(Math.round(Math.random()*10));//生成0-10之间的随机数
//Math.round(Math.random()*(y-x)+x)  生成x-y之间的随机数
console.log(Math.round(Math.random()*5+1));//生成1-6之间的随机数
```

## 1.5.包装类

JS提供了三个包装类，可以将基本数据类型转化为对象

- Number()
- String()
- Boolean()

在底层字符串是以字符数组的形式保存

## 1.5.1.String

1. charCodeAt(index)获取指定位置字符的Unicode编码
2. silce(start,end)截取字符串[start,end）
3. substring(start,end)截取字符串[start,end)，传递负数时默认转化为0，截取两个数之间的字符

```javascript
var str = "123456";
console.log(str.substring(1,2));//2
console.log(str.substring(2,1));//2
console.log(str.substring(2,-1));//12
```

4. substr(start,len)截取字符串，不是标准，但所有浏览器都支持

5. split(separator)拆分字符串

```javascript
var str = 'ashdsadgasda';
str.split("");//按字符拆分,["a", "s", "h", "d", "s", "a", "d", "g", "a", "s", "d", "a"]
```





# 二、运算符

1. 运算符对一个或多个计算，并返回结果

```js
var num = 123;
var result = typeof num;
console.log(result);  //number 
```

2. 任何值与NaN计算都是NaN

```js
var res = 12 + NaN;  //NaN,任何值与NaN计算返回NaN
res = true + 1;  //2
res = false + 1;  //1
res = true + false;  //1
res = null + 1;  //1
res = undefined + 1; //NaN
console.log(res);
```

3.任何值与字符串相加，都会转化为字符串再拼接

```js
var res = "123" + 1; //1231 
res = 123 + "456"; //123456 
res = 123 + ""; //123 typeof res = string
res = true + "1";//true1
res = NaN + "1"; //NaN1
res = null + "1";//null1
res = undefined + "1";//undefined1
var obj = {};
obj.name = "zhangsan";
res = obj + "1"; //[object Object]1
console.log(res);

res = 1 + 2 + "3";  //33
res = "1" + 2 + 3; //123
res =  1 + +"2" + 3;//6
```

4.特殊的减法运算

```js
var res = 100 - true; //99
res = 100 - "1";  //99
res = "100" - 1 ; //99
res = "100" - "1"; //99
console.log(res);
```

5. 乘法的特殊情况

```js
var res = 2 * "2" //4
res = "2" * "4";//8
res = 2 * "";//0
res = 2 * "  ";//0
res = 2 * true;//2
res = 2 * false;//0
res = 2 * NaN; //NaN
res = 2 * undefined;//NaN
res = 2 * null;//0
console.log(res);
```

6. 除法的特殊情况

```js
var res;
res = 2 / true;//2
res = 2 / false;//Infinity
res = 2 / "";//Infinity
res = "" / 2; //0
res = 2 / "  ";//Infinity
res = "  "/2; //0
res = 2 / NaN; //Infinity
res = NaN / 2;//NaN
res = null / 2; //0
res = 2 / null;//Infinity
res = 2 / undefined;//NaN
res = undefined / 2;//NaN
res = "12" / "2";//6
console.log(res);
```

7. 任何值做`- * /`都会自动转化为Number
8. 取模的特殊情况，`%0`w为NaN，`0%`为0

```javascript
var res;
res = NaN % 2;//NaN
res = 2 % NaN; //NaN
res = 2 % null;//NaN
res = null % 2;//0
res = 2 % undefined;//NaN
res = undefined % 2; //NaN
res = 2 % "";//NaN
res = 2 % false;//NaN
console.log(res);
```

9.自增，a++和++a都会使原值a立即加1,区别在于表达式a++与++a的值不同

```javascript
var a = 1;
console.log(a++);  //1  
console.log(a);    //2
```

```javascript
var a = 1;
console.log(++a);  //2  
console.log(a);    //2
```

```javascript
var a = 10;
var res = a++ + ++a + a;  //34
console.log(res);
```

```javascript
var a = 10;
a = a++;  
console.log(a);  //10
```

10.自减，num--和--num都会使原值num立即减1,区别在于表达式num--与--num的值不同

```javascript
var num = 10;
console.log(num--);//10
console.log(num);//9
```

```javascript
var num = 10;
console.log(--num);//9
console.log(num);//9
```

11.`!`的特殊情况

```js
var a;
a = !0; //true
a = !10;//false
a = !-10;//false
a = !"";//true
a = !" ";//false
a = !NaN; //true
a = !null;//ture
a = !undefined;//true
console.log(a);
```

12.`&&`短路与

```js
true&&alert("看我执不执行");  //执行第二个
false&&alert("看我执不执行");  //不执行第二个
```

13.`||`短路或

```js
true&&alert("看我执不执行");  //不执行第二个
false&&alert("看我执不执行");  //执行第二个
```

14.`&&`或`||`对于非布尔值会先将其转化为布尔值进行计算，但是会返回原值

```javascript
//true && true 
var res = 5 && 6; //6，若两个值都为true,返回后面的值
//false && true 
var res = 0 && 6; //0
//true && false
var res = 6 && 0; //0
//false && false
var res = NaN && 0;//NaN
var res = 0 && NaN;//0
//总结：&& 返回最后的执行的值
//即若第一值为true,返回第二个值；若第一个值为false,就返回第一个值

//总结：|| 返回最后的执行的值
//即若第一值为false,返回第二个值，若第一个值为true,就返回第一个值
```

15.非数值的比较（==、>、<、>=、<=），会将数字转化为数值再进行比较；任何值与NaN比较都是false

```js
console.log(9 >= NaN）;  //false
console.log(true > false); //true            
```

若比较符的两边都是字符串，不会将其转化为数字，会直接比较字符串中字符的Unicode的编码

```js
console.log("1" < "5");// true
console.log("11" < "5");//true
console.log("a" < "b");//true
console.log("abc" < "b");//true
//比较字符编码时是一位一位的比较，若两位相同，才比较下一位；比较中文没有意义
```

16.Unicode编码

在js中使用Unicode编码，使用`\u`

```js
console.log('\u0032'); //2
```

在网页中显示Unicode编码，使用`&#`输出，此时需要转化为十进制

```html
<span>&#9760</span>
```

17.`==`的特殊情况

```js
console.log(null == 0);  //false
console.log(undefined == 0);//false
console.log(null == undefined);//true,因为undefined衍生自null,所以为true
console.log(null == NaN);//false
console.log(NaN == NaN);//false,NaN和任何值不相等,包括本身,故判断一个数是否是NaN，要使用isNaN()
console.log(null == null);//true
console.log(undefined == undefined);//true
console.log(true == "1");//true
console.log("true" == 1);//false
```

注意：`==`和`!=`都会对变量进行自动转化；`===`和`!==`不会做自动类型的转化，`===`若两个值的类型不同直接返回false

```js
console.log(null == undefined);//true
console.log(null === undefined);//false
```

18.三元运算符

```js
//比较三个数的大小
var a = 100;
var b = 70;
var c = 40;
var max = a > b ? (a > c ? a : c) : (b > c ? b : c);
var max = a > b ? a > c ? a : c : b > c ? b : c;
console.log(max);
```

```js
""？alert(1):alter(2);     //2
"str"？alert(1):alter(2);  //1,会将字符串转化为boolean值，可用于判断是否是空串（""）
```

19.运算符的优先级

* `&&` 的优先级高于`||`

```js
var res = 1 || 2 && 3;  //1  
```

| 运算符                             | 描述                                         |
| ---------------------------------- | -------------------------------------------- |
| . [] ()                            | 字段访问、数组下标、函数调用以及表达式分组   |
| ++ -- - ~ ! delete new typeof void | 一元运算符、返回数据类型、对象创建、未定义值 |
| * / %                              | 乘法、除法、取模                             |
| + - +                              | 加法、减法、字符串连接                       |
| << >> >>>                          | 移位                                         |
| < <= > >= instanceof               | 小于、小于等于、大于、大于等于、instanceof   |
| == != === !==                      | 等于、不等于、严格相等、非严格相等           |
| &                                  | 按位与                                       |
| ^                                  | 按位异或                                     |
| \|                                 | 按位或                                       |
| &&                                 | 逻辑与                                       |
| \|\|                               | 逻辑或                                       |
| ?:                                 | 条件                                         |
| = oP=                              | 赋值、运算赋值                               |
| ,                                  | 多重求值                                     |

不需要记忆优先级，可通过`()`改变优先级

# 三、流程控制语句

3.1.JS的代码块只有分组的作用，代码块内的内容，在外部完全可见

3.2.prompt()可输入值

```js
var score = prompt("请输入成绩：",60);
console.log(score);
```

3.3. switch

```js
var socre = 70; 
switch (true) {
    case score > 60 :
        console.log('合格');
        break;
    default:
        console.log('不合格');
        break;
}
```

3.4.`braeak`退出最近的一个循环，`continue`退出本次循环

```js
//退出多个循环
outer:
for(var i =0 ;i< 5;i++){
    console.log("outer");
    for(var j = 0; j<5;j++){
        console.log('inner');
        break outer;
    }
}
```





# 四、函数

## 4.1.计时器

```js
//开启一个计时器 
console.time("test");
//结束一个计时器 
console.timeEnd("test");
```

## 4.2.数学函数

```javascript
console.log(Math.sqrt(9));  //开方，√2
```

## 4.3.创建函数分类

### 4.3.1.创建函数对象

```js
var fun = new Function("console.log('这是函数的代码')");  //不建议使用
```

### 4.3.2.函数声明创建函数

- `javascript`引擎规定，`function`关键字在行首，一律解析成函数声明语句
- `function`关键字后不能跟`()`

- 匿名函数是函数声明的一种

```js
//函数声明语句
function fun(){
    console.log('这是函数的代码');
}
```

### 4.3.3.使用函数表达式

- `function`关键字后可以跟`()`

```js
//函数表达式，匿名函数
var fun = function(){
    console.log('这是匿名函数的代码');
};
```

## 4.4.`arguments`

- 解析器不会检查参数的类型，也不会检查参数的个数

  - 多余的参数不会被赋值，即忽略
  - 若实参的个数小于形参的个数，则没有对应实参的的形参将是undefined

  ```js
  function sum(a,b){
  	console.log(a); //12
      console.log(b); //undefined 
      return a+b;
  }
  
  sum(12);  //NaN
  ```

- 参数的实参可以是任意数据类型



## 4.5.`return`

- `return`后的语句不会执行，可用于中断函数的执行（`return;`）
- `return`后不跟值（`return;`）会返回undefined，函数不写return也是返回undefined
- `return`可返回任意类型的值(函数)

```html
<a href="abc.htm" onclick="return fnClick()">Open</a>
```

- 事件中调用函数时，`return`的返回值实际上是对`window.event.returnvalue`进行设置，该值决定了当前操作是否继续。

  - `return true`时，会跳转页面。
  - `return false`时，会执行函数，不会跳转页面。

  - 没有`return`时，执行完函数后仍然跳转页面。



## 4.6.立即执行函数

`IIFE(Immediately invoked Function Expression)`立即执行函数，在函数声明时立即调用函数

- 使用立即执行函数时末尾的`;`最好加上，因为遇到两个`()`包裹的`IIFE`可能会出现问题

```js
(function(){
    console.log('立即调用匿名函数');
})();
```

```js
(function(a,b){
	return a  + b ;
})(123,445);
```

- 将函数声明转化为函数表达式，就是使`function`不出现在行首

```js
(function(){...}());
(function(){...})();
(function foo(){...})();            
```



## 4.6.1.`IIFE`的作用

- 为调用一次的函数表达式达到作用域隔离、命名冲突、减少内存占用的问题
- 创建块级（私有）作用域，避免向全局作用域添加变量和函数；
  - `IIFE`中定义的变量和函数都会在执行结束后被销毁，可以减少闭包占用内存的问题（原因：没有指向匿名函数的引用）

## 4.7.`this`

- this指向函数执行的上下文环境

- 根据函数调用方式的不同，会指向不同的对象
  - 以函数方式的调用，this就是window对象

  ```javascript
  function fun(){
      console.log(this);
  }
  fun(); //window对象
  ```

  - 以方法的形式调用，this就是调用方法的对象

  ```javascript
  function fun(){
      console.log(this);
  }
  var obj = {
      name:'zhangsan',
      getObj:fun 
  };
  obj.getObj();//obj对象
  ```

  - 当以构造函数调用时，this就是新创建的对象

  ```javascript
  function Person(){
  	console.log(this); //Person {}
  }
  var obj = new Person();
  ```

  - 使用call()和apply()调用函数，this是指定的那个对象

  ```javascript
  var obj = {};
  function fun(a,b){
      console.log(this); //{}
      console.log(a);//2
      console.log(b);//4
  }
  fun.call(obj,2,4); //this是obj对象
  ```

函数中访问属性的区别：

```javascript
var name = '全局';
function fun(){
    console.log(name); //此时只能输出name变量的值
    console.log(obj.name);//此时只能调用obj对象的name属性值
    console.log(this.name);//会根据上下文环境调用对应的name
}
```

缺点：使用工厂方法创建的对象，不能区分对象的类型

## 4.8.构造函数

- 构造函数与普通函数的区别就是调用方式不一样
- 所有对象都是Object的后代

```javascript
function Person(name,age){
    this.name = name;
    this.age = age;
    this.getName = function(){
        return this.name;
    }
}

var obj = new Person('zhangan',10);
```

构造函数的执行流程：

1. 立即新建一个对象
2. 将新建的对象设置为函数的this,在构造函数中可使用this来引用新建的对象
3. 执行函数中的代码
4. 将新建的对象作为返回值返回

注意：上述Person构造函数每创建一次，就会创建一个新的getName()方法，此时会影响效率

优化后的方式：所有的对象共享一个方法

```javascript
function getName(){
    return this.name;
}

function Person(name,age){
    this.name = name;
    this.age = age;
    this.getName = getName;
}

var obj = new Person('zhangan',10);
//但此方式污染了全局作用域的命名空间，在全局作用域中也不太安全（访问权限太大）,可通过原型对象解决
```

## 4.9.`instanceof`

`instanceof`可以检查一个对象是否是一个类的实例(实例是通过构造函数创建的对象)

```javascript
console.log(obj instanceof Person);
```

## 4.10.`prototype`

- 每创建一个函数，解析器都会向函数中添加一个属性`prototype`

```javascript
function Person(){
    
}
console.log(Person.prototype);
```

- 当函数以构造函数的方式调用时，它所创建的对象中都会一个隐含的属性`prototype`，指向该构造函数的原型对象

```javascript
function Person(){
    
}
var obj = new Person();
console.log(obj.__proto__); //访问obj对象的prototype对象,不能通过obj.prototype直接访问
```

- 原型对象相当于一个公共区域，每个类的实例都可以访问该类的原型对象，故把对象的共有内容统一放在原型对象
- 原型对象也有原型对象
- 当访问对象的一个属性和方法时，会先在对象自身中寻找
  - 若找不到再在原型对象中寻找
  - 会在原型对象的原型对象中依次查找
  - 直到Object对象的原型

```js
function Person(name,age){
    this.name = name;
    this.age = age;
}
Person.prototype.getName = function(){
    return this.name;
}
//是否成年
Person.prototype.isAdult = function(sta){
    if(sta > this.age){
        console.log('成年');
        return true;
    }else{
        console.log('未成年');
        return false;
    }
}     
var obj = new Person('zhangsan',15);
console.log(obj); //Person {name: "zhangsan", age: 15}
console.log(obj.getName());//zhangsan
console.log(obj.isAdult(18));//成年 true
```

- Object对象的没有原型，Object的`__proto__`值为null

## 4.11.`toString()`

- toString()是Object对象提供的，默认输出`[object,Object]`
- 定制对象的toString()

```javascript
function Person(name,age){
    this.name = name;
    this.age = age;
}
Person.prototype.toString = function(){
    return '{name:'+this.name+',age:'+this.age+'}';
}

var obj = new Person('zhangsan',16);
console.log(obj.toString());
```

## 4.12.回调函数

由我们创建但是不由我们调用的函数称为回调函数 

## 4.13.函数对象的方法

- call()和apply()都会调用函数的执行

- call()和apply()可以将一个对象指定为第一个参数，此对象将会成为函数执行时的this

  ```javascript
  var obj = {};
  function fun(){
      console.log(this);
  }
  
  fun(obj);  //window
  //call()和apply()可以修改函数的this
  fun.call(obj);//{}
  fun.apply(obj);//{}
  ```

### 4.13.1.call()

call()可以将实参在对象之后传递

```javascript
var obj = {};
function fun(a,b){
    console.log(this); //{}
    console.log(a);//2
    console.log(b);//4
}
fun.call(obj,2,4);
```

### 4.13.2.apply()

apply()需将实参封装到一个数组中统一传递

```javascript
var obj = {};
function fun(a,b){
    console.log(this);
    console.log(a);
    console.log(b);
}

fun.apply(obj,[2,4]);//{}
```

## 4.14.`arguments`

arguments封装实参的对象，是一个类数组对象，不是数组

```javascript

```



## 4.15.执行一个函数发生了什么？

> 1.创建函数

1).开辟新的堆内存，为函数申请内存（堆允许程序在运行时动态地申请某个大小的内存空间）

2).将函数体代码放入堆内存，以字符串的形式（原因：无序）

3).在当前上下文中声明`fun`函数（变量），函数声明和定义会提升到最前面

4).将`1)`中开辟的堆内存地址赋值给函数名`fun`

> 2.执行函数

1).







- 内存的区域：代码区域，栈区域，堆区域



## 4.16.函数节流

函数节流`(throttle)`：指定时间间隔内只会执行一次任务；

- 让一个函数不要执行得太频繁，减少一些过快的调用来节流

```js
// 2、节流函数体
function throttle(fn, wait) {
    // 4、通过闭包保存一个标记
    let canRun = true;
    return function () {
        // 5、在函数开头判断标志是否为 true，不为 true 则中断函数
        if (!canRun) {
            return;
        }
        // 6、将 canRun 设置为 false，防止执行之前再被执行
        canRun = false;
        // 7、定时器
        setTimeout(() => {
            fn.call(this, arguments);
            // 8、执行完事件（比如调用完接口）之后，重新将这个标志设置为 true
            canRun = true;
        }, wait);
    };
}
```

### 4.16.1.应用

- 懒加载要监听计算滚动条的位置，使用节流按一定时间的频率获取。
- 用户点击提交按钮，假设我们知道接口大致的返回时间的情况下，可以使用节流，只允许一定时间内点击一次。

## 4.17.函数防抖

函数防抖`(debounce)`：规定时间后才执行；在事件被触发n秒后再执行回调，如果在这n秒内又被触发，则重新计时。

- 对于一定时间段的连续的函数调用，只让其执行一次

```js
/**
 * 防抖函数
 * @param fn  回调函数
 * @param wait 等待时间
 * @returns {Function}
 */
function debounce(fn, wait) {
    var timer = null;
    return function () {
        if (timer) {
            clearTimeout(timer);
            timer = null;
        }
        timer = setTimeout(function () {
            fn.apply(this, arguments)
        }, wait)
    }
}
```

```js
var ele = document.getElementById("debounce");
ele.addEventListener("click", debounce(sayDebounce, 1000));
```

### 4.17.1.应用

- 输入框获取联想词，因为频繁调用接口不太好，所以我们在代码中使用防抖功能，只有在用户输入完毕的一段时间后，才会调用接口，出现联想词。



[]: https://qishaoxuan.github.io/blog/js/throttleDebounce.html?tdsourcetag=s_pctim_aiomsg
[]: https://www.codercto.com/a/35263.html



## 4.18.重绘

重绘`(repaint)`:当元素样式改变，但是不影响页面布局时，浏览器会使用重绘对元素进行更新，由于此时只需要 UI层面的重新像素绘制，因此**损耗较少**。

重绘的常用操作：

- 改变元素的颜色
- 改变元素的背景色

## 4.19.回流

回流`(reflow)`:又叫重排`(Layout)`；当元素的大小、尺寸或触发了某些属性时，浏览器会重新渲染页面。

由于此时浏览器需要重新经过计算，计算后还需要重新页面布局，因此是较重的操作。

常见的回流操作：

- 页面初次渲染
- 浏览器窗口的大小改变
- 元素尺寸、位置、内容的改变
- 元素字体的改变
- 新增或删除可见的DOM元素
- 激活CSS伪类`(:hover)`

> 回流一定会触发重绘，但是重绘不一定会触发回流；重绘的开销小，回流的代价较高。

由于回流的代价较高，因此要避免大量的触发重绘和回流：

- 避免频繁修改样式，可使用节流或防抖，汇总后统一一次修改
- 尽量使用class修改样式，而不是直接操作样式
- 减少DOM操作，可使用字符串一次性操作







# 五、作用域

## 5.1.全局作用域

- 直接编写在`<script>`标签中的JS代码，都在全局作用域
- 全局作用在页面打开时创建，在页面关闭时销毁
- 在全局作用域中有一个window对象，可直接使用
  - window的作用：在全局作用域中创建的变量，都会作为window对象的属性保存；创建的函数会作为window对象的方法保存
- 全局域的变量都是全局变量，可在页面的任意位置访问到

### 5.1.1.变量的声明提前

- 使用`var`关键字声明的变量，会在所有代码执行之前声明(但是不会提前赋值)

```js
console.log(a); //undefined
var a = '123'; 
```

```js
//等同于此种方式
var a;
console.log(a); //undefined
a = '123'; 
```

- 若声明变量时不使用`var`关键字，则变量的声明不会被提前

```js
console.log(a); //a is not defined
a = '123'; 
```

### 5.1.2.函数的声明提前

- 函数声明创建函数`function function_name(){}`，会在所有代码执行之前被创建 

```js
fun();  //声明式函数
function fun(){
    console.log('声明式函数');
}
```

- 使用函数表达式创建函数，不会提前创建函数，故不能提前调用

```js
fun();  //fun is not a function
var fun = function(){
    console.log('函数表达式');
}
```

## 5.2.局部作用域

- 局部作用域有叫函数作用域
- 调用函数时创建函数作用域，函数调用完毕后销毁函数作用域
- 每调用一次函数，就会创建一个新的函数作用域，它们之间是相互独立的
- 函数作用域操作一个变量时，会现在自身作用域中寻找，找不到再在上一级作用域中查找
- 若需直接访问全局作用域中的变量，可使用`window.变量`访问 
- 在函数作用域中也有提前声明的特性
- 在函数中不使用var声明的变量会成为全局变量
- 定义了形参就相当于在函数作用域中声明了变量

## 5.3.私有作用域

- `IIFE`实现



# 六、垃圾回收

当一个对象没有任何对象或属性对它引用时，我们无法操作该对象了，就表示该对象时一个垃圾。

我们需要将不再使用的对象是否null,以便能被GC回收



# 七、浏览器

## 7.1.浏览器解析`URL`

- 用户输入`URL`
- 对`URL`进行`DNS`解析
- 建立`TCP`连接(三次握手)
- 浏览器发送`HTTP`请求报文
- 服务器返回`HTTP`响应报文
- 关闭`TCP`连接(四次挥手)
- 浏览器解析文档并渲染页面

![img](../images/169721e312f11eea)

[]: https://www.cnblogs.com/wpshan/p/6282061.html



## 7.2.`DNS`域名解析

`DNS（Domain Name System）`**域名系统**，提供的服务是用于将主机名和域名转换为 IP 地址的工作。

当用户输入`www.baodu.com`后，`DNS`的解析步骤：



## 7.3.`TCP`三次握手与四次挥手



## 7.4.浏览器渲染页面

1. 浏览器通过HTMLParser根据深度遍历的原则把HTML解析成DOM Tree。

2. 浏览器通过CSSParser将CSS解析成CSS Rule Tree（CSSOM Tree）。

3. 浏览器将JavaScript通过DOM API或者CSSOM API将JS代码解析并应用到布局中，按要求呈现响应的结果。

4. 根据DOM树和CSSOM树来构造render Tree。

5. layout：重排（也可以叫回流），当render tree中任一节点的几何尺寸发生改变，render tree就会重新布局，重新来计算所有节点在屏幕的位置。

6. repaint：重绘，当render tree中任一元素样式属性（几何尺寸没改变）发生改变时，render tree都会重新画，比如字体颜色，背景等变化。

7. paint：遍历render tree，并调动硬件图形API来绘制每个节点。

![img](../images/169721ed68383402)