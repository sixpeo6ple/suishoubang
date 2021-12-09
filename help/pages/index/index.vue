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
					<navigator url="../eachorder/eachorder">
						<view class="block">
							<text>送货点：{{ array_mess[index].destination }}</text>
							<br>
							<text>送货方式：{{ array_mess[index].method }}</text>
							<br>
							<text>报酬：{{ array_mess[index].money}}</text>
							<br>
						</view>
						<view class="blank4"></view>
					</navigator>
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
			wx.request({                            
			    url: 'https://api.suishoubang.myrating.cn/order/showAll',
			    method:'GET',
			    header: that.getHeader(),
			    data:{  
			        //0全部 1还没人接的单子 2有人接还没有完成的单子 3已经完成的单子        
			        status:0,  
			    },  
			    success(r){
					that.num = r.data.data.length;
					var array_mess = new Array();
					for (var i = 0; i < that.num; i++){
						var each_mess = {id:'', destination:'', method:'', money:''};
						each_mess.id = r.data.data[i].id;
						each_mess.destination = r.data.data[i].payerPlace;
						each_mess.method = r.data.data[i].deliverType;
						each_mess.money = r.data.data[i].price;
						that.array_mess.push(each_mess);
					}
			        console.log(r);
					console.log(r.data.data.length);
					console.log(that.num);
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
