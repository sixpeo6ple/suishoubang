# 登录接口

## 1.登录

> 请不要重复登录

接口地址：

```api
https://api.suishoubang.myrating.cn/login/doLogin
```

测试代码：

```js
wx.request({
    url: 'https://api.suishoubang.myrating.cn/login/doLogin',
    method: 'Get',
    data: {
        //通过wx.login得到的code
        code: wx.getStorageSync('code')
        //通过wx.getUserProfile得到的avatarUrl
        avatarUrl:"https://thirdwx.qlogo.cn/mmopen...Q/132"
    },
    success(r) {
        console.log(r);
        // 在登录时，将得到的数据存储在本地
        wx.setStorageSync('tokenName', r.data.data.tokenName)
        wx.setStorageSync('tokenValue', r.data.data.tokenValue)
        //wx.setStorageSync('session_key', r.data.data.session_key)
        //wx.setStorageSync('openid', r.data.data.openid)
    }
})
```

## 2.判断是否登录

> 测试是否正确携带请求头

接口地址：

```api
https://api.suishoubang.myrating.cn/login/isLogin
```

测试代码：

```js
function getHeader() {
    let tokenName = wx.getStorageSync('tokenName');   
    let tokenValue = wx.getStorageSync('tokenValue');   
    let header = {
        "content-type": "application/x-www-form-urlencoded"
    };
    if (tokenName != undefined && tokenName != '') {
        header[tokenName] = tokenValue;
    }
    return header;
}

module.exports = {
    getHeader
}
```

```js
// 在发起请求时将 header 对象塞到请求头部
// 通过tokenValue鉴别身份
wx.request({
    url: 'https://api.suishoubang.myrating.cn/login/isLogin',
    header: util.getHeader(),
    method: 'Get',
    success(r) {
        //学生认证后会返回学生用户登录
        console.log(r);
    }
})
```

## 3.上传学生认证信息

接口地址：

```api
https://api.suishoubang.myrating.cn/login/StudentAuth
```

测试代码：

```js
wx.request({   
    url: 'https://api.suishoubang.myrating.cn/login/StudentAuth',
    method: 'Post',
    data: { 
        name:"夏杨阳", 
        sid:"2019111327",
        //上传图片时返回的url
        picUrl:"https://wxapp-1303032424.cos.ap-shanghai.myqcloud.com/SUFE/StuCard/onLBk5Wz3Nn48W-9IEqLF-eOQ1aM.jpeg",
        place:"1号楼113",
        phone:"18221653530"
    },
    header: util.getHeader(),
    success(r) {
        console.log(r);
    }
})
```

## 4.获取用户信息

接口地址：

```api
https://api.suishoubang.myrating.cn/login/getInfo
```

测试代码：

```js
wx.request({    
    url:'https://api.suishoubang.myrating.cn/login/getInfo',
    method: 'Get',
    header: util.getHeader(),
    success(r) {
        console.log(r);
    }
})
```

返回格式：

```js
data:{
    id: 1
    isStudent: true
    name: "夏杨阳"
    openid: "onLBk5Wz3Nn48W-9IEqLF-eOQ1aM"
    phone: "18221653530"
    picUrl: "https://wxapp-1303032424.cos.ap-shanghai.myqcloud.com/SUFE/StuCard/onLBk5Wz3Nn48W-9IEqLF-eOQ1aM.jpg"
    place: "1号楼113"
    //审核状态 0=无状态, 1=审核中, 2=审核成功, 3=审核失败
    reviewStatus: 2
    sid: "2019111327"
}

```

