<template>
	<view class="content" style="background-image: url(../../static/background1.png);background-size:100% 100%;">
		<!-- 登录 -->
		<view class="blank1"></view>
		<view class="userinfo" v-if="nickName">
			<image :src="avatarUrl" class="photo"></image>
			<view class="blank2"></view>
			<text class="name">{{ nickName }}</text>
			<!-- 登陆成功，进行学生认证 -->
			<view class = "student">
				<view class = "stu_card" v-if="status == 2">
					<text class="text">{{ name }}</text>
					<text class="text">{{ sid }}</text>
					<text class="text">{{ place }}</text>
					<text class="text">{{ phone }}</text>
				</view>
				<view class="stu_card" v-if="status == 1">
					<text class="text">信息正在审核...</text>
				</view>
				<view class="stu_card" v-if="status == 3">
					<text class="text">审核未通过，请重新认证...</text>
					<button class="but" @click="goto">去认证</button>
				</view>
				<view v-if="status == 0">
					<button class="but" @click="goto">去认证</button>
				</view>
			</view>
		</view>
		<view class="predown" v-else>
			<button @tap="getUserProfile_token" class="but" v-if="session_key">注册及登陆</button>
			<button @tap="getUserProfile" class="but" v-else>请登陆</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				code: '',
				nickName: uni.getStorageSync("nickName"),
				avatarUrl: uni.getStorageSync("avatarUrl"),
				session_key: uni.getStorageSync("session_key"),
				status: '',
				name: '',
				picURL: '',
				sid: '',
				phone: '',
				place: ''
			}
		},
		onLoad: function(e){
			var that = this;
			uni.request({    
			    url:'https://api.suishoubang.myrating.cn/login/getInfo',
			    method: 'Get',
			    header: that.getHeader(),
			    success(r) {
					that.status = r.data.data.reviewStatus;
					that.picURL = r.data.data.picUrl;
					that.name = r.data.data.name;
					that.place = r.data.data.place;
					that.phone = r.data.data.phone;
					that.sid = r.data.data.sid;
			        console.log(r);
			    }
			})
		},
		onPullDownRefresh: function(e) {
			var that = this;
			that.nickName = uni.getStorageSync("nickName");
			that.avatarUrl = uni.getStorageSync("avatarUrl");
			that.session_key = uni.getStorageSync("session_key");
			uni.request({
			    url:'https://api.suishoubang.myrating.cn/login/getInfo',
			    method: 'Get',
			    header: that.getHeader(),
			    success(r) {
					that.status = r.data.data.reviewStatus;
					that.picURL = r.data.data.picUrl;
					that.name = r.data.data.name;
					that.place = r.data.data.place;
					that.phone = r.data.data.phone;
					that.sid = r.data.data.sid;
			        console.log(r);
			    }
			})
		},
		methods: {
			getUserProfile(e) {
				console.log(e);
				var that = this;
				uni.login({
					provider:'weixin',
					success: function(resp){
						let code = resp.code;
						that.code = code;
						console.log(code);
					}
				})
				uni.getUserProfile({
					desc:'登录以获取信息',
					success: function(resp){
						that.nickName = resp.userInfo.nickName;
						console.log(that.nickName);
						that.avatarUrl = resp.userInfo.avatarUrl;
						console.log(that.avatarUrl);
						that.data = {
							code: that.code,
							nickname: that.nickName,
							photo: that.avatarUrl
						};
						uni.setStorageSync('nickName', that.nickName)
						uni.setStorageSync('avatarUrl', that.avatarUrl)
						console.log(that.data);
						uni.request({
							url:'https://api.suishoubang.myrating.cn/login/doLogin',
							method:'Get',
							data: {
								code: that.data.code,
								avatarUrl: that.avatarUrl
							},
							success(r) {
								console.log("-------");
								console.log(r);
								console.log("session_key"+r.data.data.session_key);
								uni.setStorageSync('tokenName', r.data.data.tokenName)
								uni.setStorageSync('tokenValue', r.data.data.tokenValue)
								uni.setStorageSync('session_key', r.data.data.session_key)
								uni.setStorageSync('openid', r.data.data.openid)
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
			getUserProfile_token() {
				var that = this;
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/login/isLogin',
					header: that.getHeader(),
					method: 'Get',
					success(r) {
						console.log(r);
					}
				})
			},
 			goto() {
				uni.navigateTo({
					url:'../stuinfo/stuinfo'
				})
			} 
		},
	}
</script>

<style>
	page{
		height: 100%;
	}
	.content{
		height: 100%;
	}
	.blank1{
		height: 100px;
	}
	.blank2{
		height: 30px;
	}
	.photo{
		border-radius: 50%;
		display: block;
		margin: auto;
		height: 50px;
		width: 50px;
	}
	.name{
		display: block;
		margin: auto;
		text-align: center;
	}
	.but{
		display: block;
		margin-top: 20px;
		border-radius: 13px;
		width: 100px;
		background-color: rgb(246,237,218);
		border: 3px solid rgb(219,229,228);
	}
	.stu_card{
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: 20px;
	}
	.text{
		margin-top: 5px;
	}
</style>
