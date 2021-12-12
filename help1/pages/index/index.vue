<template>
	<view class="home">
		<view class="block1">
			<list>
				<cell v-for="(item, index) in array_mess" :key="array_mess.id">
					<view class="block" @click="goto(array_mess[index].id)">
						<view class="text">
							<text>送货点：{{ array_mess[index].destination }}</text>
							<br>
							<text>送货方式：{{ array_mess[index].method }}</text>
							<br>
							<text>报酬：{{ array_mess[index].money}} ¥</text>
						</view>
						<view class="pic1">
							<image class="pic" src="../../static/expressage.png"></image>
						</view>
					</view>
				</cell>
			</list>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				array_mess: []
			}
		},
		onLoad: function(r) {
			var that = this;
			uni.request({                            
			    url: 'https://api.suishoubang.myrating.cn/order/showAll',
			    method:'GET',
			    header: that.getHeader(),
			    data:{  
			        //0全部 1还没人接的单子 2有人接还没有完成的单子 3已经完成的单子        
			        status: 1,  
			    },  
			    success(r){
					var num = r.data.data.length;
					for (var i = 0; i < num; i++){
						var each_mess = {id:'', destination:'', method:'', money:''};
						each_mess.id = r.data.data[i].id;
						each_mess.destination = r.data.data[i].payerPlace;
						each_mess.method = r.data.data[i].deliverType;
						each_mess.money = r.data.data[i].price;
						that.array_mess.push(each_mess);
					}
			        console.log(r);
					console.log(r.data.data.length);
					console.log(that.array_mess)
			    }
			})
		},
		onPullDownRefresh: function(e) {
			var that = this;
			that.array_mess = [];
			uni.request({                            
			    url: 'https://api.suishoubang.myrating.cn/order/showAll',
			    method:'GET',
			    header: that.getHeader(),
			    data:{  
			        //0全部 1还没人接的单子 2有人接还没有完成的单子 3已经完成的单子        
			        status: 1,  
			    },  
			    success(r){
					var num = r.data.data.length;
					for (var i = 0; i < num; i++){
						var each_mess = {id:'', destination:'', method:'', money:''};
						each_mess.id = r.data.data[i].id;
						each_mess.destination = r.data.data[i].payerPlace;
						each_mess.method = r.data.data[i].deliverType;
						each_mess.money = r.data.data[i].price;
						that.array_mess.push(each_mess);
					}
			        console.log(r);
					console.log(r.data.data.length);
					console.log(that.array_mess)
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
			goto(r) {
				var iid = r;
				console.log(iid);
				uni.navigateTo({
					url: '../eachorder/eachorder?id=' + iid
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
	.pic{
		margin-top: 10px;
		height: 70px;
		width: 70px;
	}
</style>
