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
		<view class="pic">
			<text class="title">身份码</text>
			<image class="img2" :src="picurl" mode="aspectFit" @click="enlarge(picurl)"></image>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
					that.delivertime = r.data.data[0].deliverTime;
					that.price = r.data.data[0].price;
					that.payerphone = r.data.data[0].payerPhone;
					that.picurl = r.data.data[0].picUrl;
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
			/* 放大图片 */
			
			enlarge(e) {
				var URL = e;
				uni.navigateTo({
					url: '../exh/exh?po=' + URL
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
	.pic{
		text-align: center;
	}
	.img{
		width: 40px;
		height: 40px;
	}
	.img2{
		height: 60px;
	}
</style>
