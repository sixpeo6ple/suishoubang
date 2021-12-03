<template>
	<view class="content">
		<view class="blank1"></view>
		<view class="userinfo" v-if="nickName">
			<image :src="avatarUrl" class="photo"></image>
			<view class="blank2"></view>
			<text class="name">{{ nickName }}</text>
		</view>
		<view class="predown" v-else>
			<button @tap="getUserProfile" class="but">登录</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				nickName: '',
				avatarUrl: ''
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
						console.log(that.data);
						uni.request({
							url:'https://api.suishoubang.myrating.cn/login/doLogin',
							method:'Get',
							data: {
								code: that.data.code
							},
							success(r) {
								console.log(r);
								uni.setStorageSync('tokenName', r.data.data.tokenName)
								uni.setStorageSync('tokenValue', r.data.data.tokenValue)
								uni.setStorageSync('session_key', r.data.data.session_key)
								uni.setStorageSync('openid', r.data.data.openid)
							}
						})
					}
				})
			},
			}
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
</style>
