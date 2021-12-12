<template>
	<view>
		<view class="comb">
			<text class="title">取件码</text>
			<text class="value">{{ info }}</text>
			<text class="title">收件人</text>
			<text class="value">{{ payername }}</text>
			<text class="title">收件地址</text>
			<text class="value">{{ payerplace }}</text>
			<text class="title">收件方式</text>
			<text class="value">{{ delivertype }}</text>
			<text class="title">价格</text>
			<text class="value">{{ price }}</text>
			<text class="title">送达时间</text>
			<text class="value">{{ delivertime }}</text>
		</view>
		<view>
			<view class="bottom">
				<button class = "sub" @click="ftaking">接单</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			return{
				id: 0,
				info: '',
				payername: '',
				payerplace: '',
				delivertype: '',
				price: '',
				delivertime: '',
				tmp: 0,
				payerphone: '',
				picurl: ''
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
					that.delivertype = r.data.data[0].deliverType;
					that.info = r.data.data[0].info;
					that.payername = r.data.data[0].payerName;
					that.payerplace = r.data.data[0].payerPlace;
					that.price = r.data.data[0].price;
					that.delivertime = r.data.data[0].deliverTime;
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
							that.tmp = 1;
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
	page{
		background-color: rgb(228, 236, 241);
		height: 100%;
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
		font-size: 20px;
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
</style>
