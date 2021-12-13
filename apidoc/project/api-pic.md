# 上传图片接口

## 1.上传图片

接口地址：

```api
https://api.suishoubang.myrating.cn/pic/uploadPic
```

注意事项：

+ 上传图片前必须登录，携带具有token的请求头
+ 对图片进行base64编码发到后端，后端会对图片进行转码保存到腾讯的对象存储服务器
+ 如果上传照片成功，会返回照片对应的url地址

测试代码：

```js
wx.request({                           
    url: 'https://api.suishoubang.myrating.cn/pic/uploadPic',
    method:'POST',
    header: util.getHeader(),
    data:{        
        pic:pic,
        //格式:文件夹名/文件名.后缀
        //如学生证照片：SUFE/StuCard/openid.jpg
        //身份码 SUFE/ExpressCode
        //外卖图片 SUFE/TakeoutPic
        //跑腿 SUFE/LegworkPic
        name:'SUFE/StuCard/'+wx.getStorageSync('openid')+ext
    },  
    success(r){
        //返回图片在服务器的url
        console.log(r);
    }
})
```

> 完整示例

```js
wx.chooseImage({
    // 最多可以选择的图片张数，默认9
    count: 1,
    // original 原图，compressed 压缩图，默认二者都有
    sizeType: ['original', 'compressed'],
    // album 从相册选图，camera 使用相机，默认二者都有
    sourceType: ['album', 'camera'],
    success: function (res) {
        let path = res.tempFilePaths[0];
        let index= path.lastIndexOf(".");
        let ext = path.substr(index); //获取文件后缀名
        wx.getFileSystemManager().readFile({
            //要读取的文件的路径 (本地路径)
            filePath: path,
            //指定读取文件的字符编码，如果不传 以 ArrayBuffer 格式读取文件的二进制内容
            encoding: "base64",
            success: function (res) {
                let pic = res.data;
                //转换完毕，执行上传
                // console.log(pic); 
                wx.request({                             
                    url: 'https://api.suishoubang.myrating.cn/pic/uploadPic',
                    method:'POST',
                    header: util.getHeader(),
                    data:{        
                        pic:pic,
                        //格式:文件夹名/文件名.后缀
                        //如学生证照片：SUFE/StuCard/{{openid}}.jpg
                        name:'SUFE/StuCard/'+wx.getStorageSync('openid')+ext
                    },  
                    success(r){
                        console.log(r);//返回图片在服务器的url
                    }
                })
            }
        })
    }
})
```

## 2.删除图片

接口地址：

```api
https://api.suishoubang.myrating.cn/pic/delPic
```

测试代码：

```js
wx.request({                           
    url: 'https://api.suishoubang.myrating.cn/pic/delPic',
    method:'POST',
    header: util.getHeader(),
    data:{        
        name:'SUFE/StuCard/onLBk5Wz3Nn48W-9IEqLF-eOQ1aM.jpeg',
    },  
    success(r){
        console.log(r);
    }
})
```

