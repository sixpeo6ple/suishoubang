<template>
	<view>
		<text>{{ id }}</text>
		<view class="bottom">
			<button class = "sub" @click="ftaking">接单</button>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			return{
				id: 0
			}
		},
		onLoad: function(r){
			this.id = r.id;
			console.log(r.id)
			var that = this;
			uni.request({
				url: 'https://api.suishoubang.myrating.cn/order/showOrderById',
				method:'GET',
				header: that.getHeader(),
				data: {
					id: r.id
				},
				success(r) {
					console.log(r)
				}
			})
		},
		methods: {
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
			ftaking: function(e){
				var that = this;
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/order/receiveOrder',
					method:'POST',
					header: that.getHeader(),
					data: {
						id: that.id
					},
					success(r) {
						console.log(r);
						if (r.data.msg == "没有通过学生认证") {
							uni.showModal({
								title: '提示',
								content: '请先进行学生认证'
							})
						}
						else{
							console.log(that.id);
							console.log(r);
							uni.showModal({
								content: '接单成功'
							})
						}
					}
				})
			}
		}
	}
</script>

<style>
	.bottom{
		display: block;
		width: 100%;
		position: fixed;
		bottom: 0;
	}
	.sub{
		display: block;
		width: 30%;
		border-radius: 15px;
		border-style: inset;
		border-color: #00aaff;
		border-width: 1px;
		color: #ffffff;
		background-color: #00aaff;
		margin-right: 0;
	}
</style>
