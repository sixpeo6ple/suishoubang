# 订单接口

> 用户必须通过学生认证，即数据库中的is_student为true

## 1.创建订单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/create
```

注意事项：

+ 首先需要上传身份码获得url

  ```js
  wx.request({                             
      url: 'https://api.suishoubang.myrating.cn/pic/uploadPic',
      method:'POST',
      header: util.getHeader(),
      data:{
          //base64格式pic
          pic:pic,
          //上传到身份码对应的文件夹中
          name:'SUFE/ExpressCode/'+wx.getStorageSync('openid')+ext
      },  
      success(r){
          console.log(r);//返回图片在服务器的url
      }
  })
  ```

测试代码：

```js
wx.request({     
    url: 'https://api.suishoubang.myrating.cn/order/create',
    method: 'POST',
    header: util.getHeader(),  
    data: {  
        //身份码图片地址
        picUrl:"https://wxapp-1303032424.cos.ap-shanghai.myqcloud.com/SUFE/ExpressCode/onLBk5Wz3Nn48W-9IEqLF-eOQ1aM.jpg",
        //取货码 多个取货码可以用;分隔
        info:"31-2-5011;12-1-3112", 
        //支付金额(必须大于0)
        price:3,  
        //类型
        type:"快递",
        //送货时间 按着这个格式写
        deliverTime:"2021-07-19 20:00:00", 
        //送货方式
        deliverType:"送货上门",
        //送货地址 默认学生认证地址
        deliverPlace:"1号楼113",
    },
    success(r) {  
        console.log(r);
        // 创建订单成功唤起真实的微信支付 
        // 以下内容可以注释掉以取消支付
        const params = r.data.data;
        wx.requestPayment({ 
            timeStamp: params.timeStamp, 
            nonceStr: params.nonceStr,
            package:params.package,
            signType: params.signType,
            paySign: params.paySign,
            success (res) { 
            },
            fail (res) {
                //如果支付失败则删除订单
                wx.request({                            
                    url:'https://api.suishoubang.myrating.cn/order/delOrder',
                    method:'POST',
                    header: util.getHeader(),
                    data:{  
                        outTradeNo:params.outTradeNo
                    },  
                    success(r){
                        console.log(r);
                    }
                }) 

            }
        }) 
    }
})
```

## 2.展示订单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/showAll
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/showAll',
    method:'GET',
    header: util.getHeader(),
    data:{  
        //0全部 1还没人接的单子 2有人接还没有完成的单子 3已经完成的单子        
        status:0,  
        //可以不写,表示全部
        type:"快递" //外卖...
    },  
    success(r){
        console.log(r);
    }
})
```

## 3.接单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/receiveOrder
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/receiveOrder',
    method:'POST',
    header: util.getHeader(),
    data:{  
        //接单的id号
        //会将该订单的receiver_openid改成你的openid，将status置为2
        id:9
    },  
    success(r){
        console.log(r);
    }
})
```

## 4.完成订单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/finishOrder
```

测试代码：

```js
//只能是创建订单的人才能完成订单
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/finishOrder',
    method:'POST',
    header: util.getHeader(),
    data:{  
        //将该订单的status置为3
        id:9
    },  
    success(r){
        console.log(r);
    }
})
```

## 5.展示我的订单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/showMyOrder
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/showMyOrder',
    method:'GET',
    header: util.getHeader(),
    data:{  
        //查看订单类型 payer表示我下的订单 receiver表示我接的单
        personType:"payer",
        //可以不写,表示全部
        orderType:"快递" //外卖...
        status:0,
    },  
    success(r){
        console.log(r);
    }
})
```

## 6.更新身份码

接口地址：

```api
https://api.suishoubang.myrating.cn/order/updatePic
```

测试代码：

```js
wx.request({                            
    url:'https://api.suishoubang.myrating.cn/order/updatePic',
    method:'POST',
    header: util.getHeader(),
    data:{  
        //订单id号
        id:10, 
        //身份码url
        picUrl:"newurl"
    },  
    success(r){
        console.log(r);
    }
}) 
```

## 7.根据id查询订单

接口地址：

```api
https://api.suishoubang.myrating.cn/order/showOrderById
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/showOrderById',
    method:'GET',
    header: util.getHeader(),
    data:{    
        id:10
    },    
    success(r){
        console.log(r);
    }
})
```

## 8.分页查询

接口地址：

```api
https://api.suishoubang.myrating.cn/order/showAllWithPage
```

```api
https://api.suishoubang.myrating.cn/order/showMyOrderWithPage
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/showAllWithPage',
    method:'GET',
    header: util.getHeader(),
    data:{     
        status:0,  
        //每页数据个数  
        size:5,
        //当前是第几页
        currentPage:2,
        //可以不写,表示全部
        type:"快递" //外卖...
    },  
    success(r){
        console.log(r);
    }
})
```

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/showMyOrderWithPage',
    method:'GET',
    header: util.getHeader(),
    data:{  
        //查看订单类型 payer表示我下的订单 receiver表示我接的单
        personType:"payer",
        status:0,
        //每页数据个数  
        size:5,
        //当前是第几页
        currentPage:2,
        //可以不写,表示全部
        orderType:"快递" //外卖...
    },  
    success(r){
        console.log(r);
    }
})
```

## 9.取消订单(退款)

接口地址：

```api
https://api.suishoubang.myrating.cn/order/refund
```

测试代码：

```js
wx.request({                            
    url: 'https://api.suishoubang.myrating.cn/order/refund',
    method:'POST',
    header: util.getHeader(),
    data:{  
        //订单id
        id:'42'
    },  
    success(r){
        console.log(r);
    }
}) 
```

