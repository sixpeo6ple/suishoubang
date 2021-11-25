<template>
	<view class="content">
		<view class="userinfo">
			<button @tap="getUserProfile">授权</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
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
					}
				})
				uni.getUserProfile({
					desc:'登录以获取信息',
					success: function(resp){
						let nickName = resp.userInfo.nickName;
						console.log(nickName);
						let avatarUrl = resp.userInfo.avatarUrl;
						console.log(avatarUrl);
						let data = {
							code: that.code,
							nickname: nickName,
							photo: avatarUrl
						};
						console.log(data);
						uni.request({
							url:'https://api.suishoubang.myrating.cn/login/doLogin',
							method:'Get',
							data: {
								code: data.code
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
				/* uni.getUserProfile({
					desc: '登录以获取信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
					success: function(resp){
						uni.login({
							success:function(res){
								console.log(res.code);
								let code = res.code;
								let nickName = resp.userInfo.nickName;
								let avatarUrl = resp.userInfo.avatarUrl;
								let data = {
									code:code,
									nickname:nickName,
									photo:avatarUrl
								}
								console.log(data);
							},
						})
						console.log("success");
						console.log(resp.userInfo);
					},
					fail(err) {
						console.log("error");
					}
				}); */
			},
			}
	}
</script>

<style>

</style>
