<template>
	<view class="home">
		<view class="block1">
			<list>
				<cell v-for="(item, index) in array_mess" :key="array_mess.id">
					<view class="block" @click="goto(array_mess[index].id, array_mess[index].type)">
						<view class="pic2">
							<image class="picc" :src=array_mess[index].avatarurl></image>
						</view>
						<view class="text">
							<text>送货点：{{ array_mess[index].destination }}</text>
							<br>
							<text>送货方式：{{ array_mess[index].method }}</text>
							<br>
							<text>报酬：{{ array_mess[index].money}} ¥</text>
						</view>
						<view class="pic1" v-if="array_mess[index].type == '快递'">
							<image class="pic" src="../../static/expressage.png"></image>
						</view>
						<view class="pic1" v-if="array_mess[index].type == '外卖'">
							<image class="pic" src="../../static/errand.png"></image>
						</view>
					</view>
				</cell>
			</list>
			<text class="loading">
				{{ loadingtype === 0 ? contenttext.contentdown:(loadingtype === 1?contenttext.contentrefresh:contenttext.contentnomore)}}
			</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			var _self,
			page = 1;
			return {
				page: 1,
				loadingtype: 0,
				contenttext: {
					contentdown: "上拉显示更多",
					contentrefresh: "正在加载...",
					contentnomore: "到底了..."
				},
				array_mess: []
			}
		},
		onLoad: function(r) {
			//_self = this;
			this.getNewList();
		},
		onPullDownRefresh: function(e) {
			this.array_mess = [];
			this.getNewList();
		},
		onReachBottom:function(e){
			var that = this;
			that.page++;
			if (that._self.loadingtype != 0) {
				return false;
			}
			that._self.loadingtype = 1;
			console.log(that.page);
			uni.showNavigationBarLoading();
			uni.request({
				url: 'https://api.suishoubang.myrating.cn/order/showAllWithPage',
				method:'GET',
				header: that.getHeader(),
				data:{     
				    status: 1,  
				    //每页数据个数  
				    size: 6,
				    //当前是第几页
				    currentPage: that.page
				},
				success: function(r) {
					console.log("加载新数据");
					console.log(r.data.data);
					if (r.data.data.length == 0) {
						that._self.loadingtype = 2;
						uni.hideNavigationBarLoading();
						return false;
					}
					var num = r.data.data.length;
					for (var i = 0; i < num; i++){
						var each_mess = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
						each_mess.id = r.data.data[i].id;
						each_mess.destination = r.data.data[i].payerPlace;
						each_mess.method = r.data.data[i].deliverType;
						each_mess.money = r.data.data[i].price;
						each_mess.type = r.data.data[i].type;
						each_mess.avatarurl = r.data.data[i].payerAvatarUrl;
						that.array_mess.push(each_mess);
					};
					that._self.loadingtype = 0;
					uni.hideNavigationBarLoading();
				}
			})
		},
		methods: {
			getNewList: function(r) {
				var that = this;
				that._self.loadingtype = 0;
				uni.showNavigationBarLoading();
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/order/showAllWithPage',
					method:'GET',
					header: that.getHeader(),
					data:{     
					    status: 1,  
					    //每页数据个数  
					    size: 6,
					    //当前是第几页
					    currentPage: 1
					},
					success(r){
						console.log(r.data.data);
						var num = r.data.data.length;
						for (var i = 0; i < num; i++){
							var each_mess = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
							each_mess.id = r.data.data[i].id;
							each_mess.destination = r.data.data[i].payerPlace;
							each_mess.method = r.data.data[i].deliverType;
							each_mess.money = r.data.data[i].price;
							each_mess.type = r.data.data[i].type;
							each_mess.avatarurl = r.data.data[i].payerAvatarUrl;
							that.array_mess.push(each_mess);
						};
						if (r.data.data.length < 6) {
							that.loadingtype = 2;
						}
						uni.hideNavigationBarLoading();
						uni.stopPullDownRefresh();
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
			goto(r, s) {
				var iid = r;
				var ttype = s;
				console.log(iid);
				console.log(s);
				uni.navigateTo({
					url: '../eachorder/eachorder?id=' + iid + '&type=' + s
				})
			}
		}
	}
</script>

<style>
	page{
		width: 100%;
		background-color: rgb(228, 236, 241);
	}
	.block{
		width: 95%;
		height: 90px;
		margin: auto;
		margin-top: 20px;
		background-color: #F1F1F1;
		border-radius: 10px;
		border-style: solid;
		border-width: 0.1mm;
		border-color: rgb(119, 118, 68);
		display: flex;
	}
	.loading{
		display: block;
		text-align: center;
	}
	.text{
		display: flex;
		flex-direction: column;
		margin-top: 10px;
		margin-left: auto;
		font-size: 15px;
	}
	.pic1{
		margin-left: auto;
		margin-right: 20px;
	}
	.pic2{
		margin-left: 10px;
		margin-right: auto;
	}
	.pic{
		margin-top: 10px;
		height: 70px;
		width: 70px;
	}
	.picc{
		border-radius: 50%;
		margin-top: 10px;
		height: 40px;
		width: 40px;
	}
</style>
