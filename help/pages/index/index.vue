<template>
	<view>
		<view class="top">
			<!-- <input class="top_input" type="text" confirm-type="search" />
			<image class="search" src="../../static/search.png"></image> -->
			<navigator url="../add/add" hover-class="navigator-hover">
				<image class="add" src="../../static/add.png"></image>
			</navigator>
		</view>
		<view>
			<list>
				<cell v-for="(item, index) in array_mess" :key="array_mess.id">
					<view class="block" @click="goto(array_mess[index].id)">
						<text>送货点：{{ array_mess[index].destination }}</text>
						<br>
						<text>送货方式：{{ array_mess[index].method }}</text>
						<br>
						<text>报酬：{{ array_mess[index].money}}</text>
						<br>
						<image class="pic" src="../../static/expressage.png"></image>
					</view>
					<view class="blank4"></view>
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
	.top{
		height: var(--status-bar-height);
		display: block;
		width: 100%;
		position: fixed;
		top: 0;
	}
	
	/* .search{
		height: 20px;
		width: 20px;
		margin-top: 0;
		margin-left: auto;
	} */
	
	.add{
		display: block;
		height: 20px;
		width: 20px;
		position: fixed;
		right: 0;
	}
	
	.block{
		width: 90%;
		height: 90px;
		border-radius: 12px;
		border-style: solid;
		border-color: gray;
		display: flex;
	}
	
	.pic{
		border-radius: 50%;
		height: 50px;
		width: 60px;
	}
	
	.blank4{
		height: 1px;
	}
	
	/* .top_input{
		height: 20px;
		width: 80%;
		
		border-radius: 20px;
		border: solid 1px #C0C0C0;
	} */
</style>
