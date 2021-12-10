<template>
	<view>
		<text> {{ id }}</text>
		<view class="pic">
			<text class="text_label_">更新身份码</text>
			<view class="img_box" @click="addpic">
				<image class="img" src="../../static/onload.png"></image>
			</view>
			<view class="img_box">
				<image class="img" :src="picurl" mode="aspectFill"></image>
			</view>
		</view>
		<view class="bottom">
			<button class = "sub" @click="ffinish" v-if="dis == 0">结束订单</button>
			<text v-else>订单结束</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dis: 0,		//改进：向后端请求status作为标记，即结束订单的时候后端返回的数据包含status
				picurl: '',
				id: 0
			}
		},
		onLoad: function(r) {
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
			addpic: async function(){
				let that = this;
				uni.chooseImage({
					count: 1,
					sizeType:['original', 'compressed'],
					sourceType: ['album'],
					success: function(res){
						let path = res.tempFilePaths[0];
						let index = path.lastIndexOf(".");
						let ext = path.substr(index);	/* 文件后缀 */
						that.picurl = res.tempFilePaths;
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
										console.log(r);
										uni.request({
											url: 'https://api.suishoubang.myrating.cn/order/updatePic',
											method: "POST",
											header: that.getHeader(),
											data: {
												id: that.id,
												picUrl: r.data.data.URL
											},
											success(r) {
												uni.showModal({
													content: "成功更新身份码"
												});
												console.log("更新成功");
												console.log(r)
											}
										})
									}
								})
							}
						})
					}
				})
			},
			ffinish: function(r) {
				var that = this;
				var fin_id = that.id;
				wx.request({                            
				    url: 'https://api.suishoubang.myrating.cn/order/finishOrder',
				    method:'POST',
				    header: that.getHeader(),
				    data:{  
				        //将该订单的status置为3
						id: fin_id
				    },  
				    success(r){
						console.log("===========");
						that.dis = 1;
				        console.log(r);
						uni.showModal({
							content: "订单已结束"
						})
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
	.img{
		height: 20px;
		width: 20px;
		margin: auto;
	}
</style>
