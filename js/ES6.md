# 一、`暂时性死区(TDZ)`

`TDZ(Temporal Dead Zone)`暂时性死区





## 1.1.`let/const`

- `let/const`可以定义块级作用域
  - `let/const`在声明变量之前访问变量，会抛出`ReferenceError`错误
    - 原因：`let`声明的变量不存在变量提升作用
    - 
- `var`是函数作用域
  - `var`在声明变量之前访问变量，会得到`undefined`
  - 

