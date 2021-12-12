<template>
	<view>
		<view class="comb">
			<text class="title">取件码</text>
			<text class="value">{{ info }}</text>
			<text class="title">取件人姓名</text>
			<text class="value">{{ payername }}</text>
			<text class="title">收货地址</text>
			<text class="value">{{ payerplace }}</text>
			<text class="title">收货方式</text>
			<text class="value">{{ delivertype }}</text>
			<text class="title">收货时间</text>
			<text class="value">{{ delivertime }}</text>
			<text class="title">价格</text>
			<text class="value">{{ price }}</text>
			<text class="title">取件人电话</text>
			<text class="value">{{ payerphone }}</text>
		</view>
		<view class="pic" v-if="sign != '3'">
			<text class="title">更新身份码</text>
			<view class="img_box" @click="addpic">
				<image class="img" src="../../static/onload.png"></image>
			</view>
			<view class="img_box">
				<image class="img" :src="picurl" mode="aspectFill" @click="enlarge(picurl)"></image>
			</view>
		</view>
		<view class="bottom">
			<button class = "sub" @click="ffinish" v-if="sign != '3'">结束订单</button>
			<text v-else>订单结束</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				sign: '',	//改进：向后端请求status作为标记，即结束订单的时候后端返回的数据包含status
				picurl: '',
				info: '',
				payername: '',
				payerplace: '',
				delivertype: '',
				delivertime: '',
				price: '',
				payerphone: '',
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
					that.delivertype = r.data.data[0].deliverType;
					that.info = r.data.data[0].info;
					that.payername = r.data.data[0].payerName;
					that.payerplace = r.data.data[0].payerPlace;
					that.price = r.data.data[0].price;
					that.payerphone = r.data.data[0].payerPhone;
					that.delivertime = r.data.data[0].deliverTime;
					that.picurl = r.data.data[0].picUrl;
					that.sign = r.data.data[0].status;
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
			/* 放大图片 */
			enlarge(e) {
				var URL = e;
				uni.navigateTo({
					url: '../exh/exh?po=' + URL
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
						that.sign = r.data.data.status;
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
	page{
		background-color: rgb(228, 236, 241);
	}
	.comb{
		display: flex;
		flex-direction: column;
	}
	.title{
		display: block;
		color: rgb(66, 66, 41);
		text-align: center;
		margin-top: 20px;
		margin-left: 10px;
		border-radius: 13px;
		width: 100px;
		background-color: rgb(246,237,218);
		border: 3px solid rgb(219,229,228);
	}
	.value{
		margin-top: 20px;
		margin-right: 20px;
		margin-left: 20px;
		text-align: right;
		border: solid rgb(196, 196, 120);
		border-width: 0px 0px 1px 0px;
		padding-right: 10px;
		font-size: 15px;
	}
	.bottom{
		margin-top: 20px;
	}
	.sub{
		background-color: rgb(246,237,218);
		border-radius: 5px;
		margin: 10px;
	}
	.pic{
		display: flex;
		flex-direction: row;
	}
	.img_box{
		text-align: center;
	}
	.img{
		height: 20px;
		width: 20px;
		margin-top: 20px;
		margin-left: 20px;
	}
</style>
