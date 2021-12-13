# 数据统计接口

## 1.获取用户类型和人数

接口地址：

```api
http://api.suishoubang.myrating.cn:8099/Users/cnt
```

返回示例：

```js
"data": [
    {
        "cnt": 1,
        "student": false
    },
    {
        "cnt": 3,
        "student": true
    }
]
```

## 2.获取订单类型、金额和数量

接口地址：

```api
http://api.suishoubang.myrating.cn:8099/Orders/cnt
```

返回示例：

```js
"data": [
    {
        "orderType": "快递",
        "priceSum": 74.5,
        //订单 
        "cnt": 16
    }
]
```

