<template>
	<view class="content">
		<!-- 登录 -->
		<view class="blank1"></view>
		<view class="userinfo" v-if="nickName">
			<image :src="avatarUrl" class="photo"></image>
			<view class="blank2"></view>
			<text class="name">{{ nickName }}</text>
		</view>
		<view class="predown" v-else>
			<button @tap="getUserProfile_token" class="but" v-if="session_key">登录1</button>
			<button @tap="getUserProfile" class="but" v-else>登录2</button>
		</view>
		<!-- 进行学生认证 -->
		<view class = "student">	
			<view class = "stu_card" v-if="name">
				<image :src="picURL" class="photo"></image>
				<text class="name">{{ name }}</text>
				<text class="sid">{{ sid }}</text>
				<text class="place">{{ place }}</text>
				<text class="phone">{{ phone }}</text>
			</view>
			<view v-else>
				<button class="but" @click="goto">去认证</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				nickName: uni.getStorageSync("nickName"),
				avatarUrl: uni.getStorageSync("avatarUrl"),
				session_key: uni.getStorageSync("session_key"),
				name: uni.getStorageSync("collectName"),
				picURL: uni.getStorageSync("url"),
				sid: uni.getStorageSync("sid"),
				phone: uni.getStorageSync("phone"),
				place: uni.getStorageSync("position")
			}
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
								code: that.data.code
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
						console.log("++++++++");
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
		margin: auto;
		border-radius: 12px;
		width: 100px;
	}
	.stu_card{
		display: flex;
		overflow: auto;
	}
</style>
