
<template>
	<view class = "input_info">
		<view class="uni-form-item">
			<view class="title">姓名:</view>
			<input class="uni-input" name="input" @input="collect_name"/>

			<view class="title">学号:</view>
			<input class="uni-input" name="input" @input="collect_sid"/>
				
			<view class="title">地址:</view>
			<input class="uni-input" name="input" @input="collect_position" placeholder="X号楼XXX"/>

			<view class="title">联系电话:</view>
			<input class="uni-input" name="input" @input="collect_phone" />
				
			<!-- 图片 -->
			<view class="title">校园卡/身份证：</view>
			<view class="pic">
					
				<view class="img_box" @click="addpic">
					<image class="img" src="../../static/onload.png"></image>
				</view>
				<view class="img_box">
					<image class="img" :src="studata.url" mode="aspectFill"></image>
				</view>
			</view>
		</view>
			
		<view class="uni-btn-v">
			<button class='but' @click="formsubmit">保存</button>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			return {
				studata:{
					collectName: '',
					sid: '',
					position: '',
					phone: '',
					url: uni.getStorageSync('server_picurl')
				}
			}
		},
		methods: {
			collect_name: function(e){
				this.studata.collectName = e.detail.value;
				console.log(this.studata.collectName);
			},
			collect_sid: function(e){
				this.studata.sid = e.detail.value;
				console.log(this.studata.sid);
			},
			collect_position: function(e){
				this.studata.position = e.detail.value;
				console.log(this.studata.position);
			},
			collect_phone: function(e){
				this.studata.phone = e.detail.value;
				console.log(this.studata.phone);
			},
			addpic: async function(){
				let that = this;
				uni.chooseImage({
					count: 1,
					sizeType:['original', 'compressed'],
					sourceType: ['album'],
					success: function(res){
						that.studata.url = res.tempFilePaths;
						console.log(that.studata.url);
						let path = res.tempFilePaths[0];
						let index = path.lastIndexOf(".");
						let ext = path.substr(index);	/* 文件后缀 */
						wx.getFileSystemManager().readFile({
							filePath: path,
							encoding: "base64",
							success: function(r){
								let pic = r.data;
								/* 上传 */
								uni.request({
									url: 'https://api.suishoubang.myrating.cn/pic/uploadPic',
									method: "POST",
									header: that.getHeader(),
									data: {
										pic: pic,
										name: 'SUFE/StuCard/'+uni.getStorageSync('openid')+ext
									},
									success: function(r){
										/* 图片在服务器的url */
										uni.setStorageSync('server_picurl', r.data.data.URL);
										console.log(r);
										console.log(r.data.data.URL)
									}
								})
							}
						})
					}
				})
			},
			getHeader: function() {
				let tokenName = uni.getStorageSync('tokenName');   
				let tokenValue = uni.getStorageSync('tokenValue');   
				let header = {
					"content-type": "application/x-www-form-urlencoded"
				};
				if (tokenName != undefined && tokenName != '') {
					header[tokenName] = tokenValue;
				}
				return header;
			},
			formsubmit: function(e){
				var that = this;
				console.log(that.studata);
				uni.setStorageSync('collectName', that.studata.collectName);
				uni.setStorageSync('sid', that.studata.sid);
				uni.setStorageSync('position', that.studata.position);
				uni.setStorageSync('phone', that.studata.phone);
				uni.setStorageSync('url', that.studata.url);
				console.log(".;.;.;.;");
				uni.request({   
				    url: 'https://api.suishoubang.myrating.cn/login/StudentAuth',
				    method: 'Post',
				    data: { 
				        name: that.studata.collectName,
				        sid: that.studata.sid,
				        picUrl: uni.getStorageSync('server_picurl'),
				        place: that.studata.position,
				        phone: that.studata.phone
				    },
				    header: that.getHeader(),
				    success(r) {
				        console.log(r);
						console.log("数据缓存成功");
						uni.reLaunch({
							url:'../mine/mine'
						})
				    }
				})
			}
		}
	}
</script>

<style>
	page{
		background-color: rgb(219,228,229);
	}
	.title{
		margin-top: 20px;
		margin-left: 10px;
	}
	.uni-input{
		border: solid #555555;
		border-width: 0px 0px 1px 0px;
		height: 45px;
		margin-top: 5px;
		margin-left: 3px;
		margin-right: 3px;
		padding-left: 10px;
	}
	.img{
		height: 20px;
		width: 20px;
		margin: auto;
	}
	.img_box{
		margin-top: 10px;
		display: flex;
		align-items: center;
	}
	.img{
		height: 30px;
		width: 30px;
	}
	.but{
		background-color: rgb(246,237,218);
		border-radius: 5px;
		margin: 10px;
	}
</style>
