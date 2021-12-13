<template>
	<view class="message">
		<!-- 选择查看我的订单还是我的接单 -->
		<view class="select">
			<button class="butt" @click="select1">我的订单</button>
			<button class="butt" @click="select2">我的接单</button>
		</view>
		<!-- 1.我的订单 -->
		<view v-if="current == 0">
			<text class="sec_top">-----我的订单-----</text>
			<list>
				<cell v-for="(item, index) in array_mess_payer" :key="array_mess_payer.id">
					<view v-if="array_mess_payer[index].type == '快递'">
						<view class="block" @click="goto1(array_mess_payer[index].id)">
							<view class="pic2">
								<image class="picc" :src=array_mess_payer[index].avatarurl></image>
							</view>
							<view class="text">
								<text>送货点：{{ array_mess_payer[index].destination }}</text>
								<br>
								<text>送货方式：{{ array_mess_payer[index].method }}</text>
								<br>
								<text>报酬：{{ array_mess_payer[index].money}}</text>
							</view>
							<view class="pic1">
								<image class="pic" src="../../static/expressage.png"></image>
							</view>
						</view>
					</view>
					<view v-if="array_mess_payer[index].type == '外卖'">
						<view class="block" @click="goto(array_mess_payer[index].id)">
							<view class="pic2">
								<image class="picc" :src=array_mess_payer[index].avatarurl></image>
							</view>
							<view class="text">
								<text>送货点：{{ array_mess_payer[index].destination }}</text>
								<br>
								<text>送货方式：{{ array_mess_payer[index].method }}</text>
								<br>
								<text>报酬：{{ array_mess_payer[index].money}}</text>
							</view>
							<view class="pic1">
								<image class="pic" src="../../static/errand.png"></image>
							</view>
						</view>
					</view>
				</cell>
			</list>
		</view>
		<!-- 2.我的接单 -->
		<view v-else>
			<text class="sec_top">-----我的接单-----</text>
			<list>
				<cell v-for="(item, index) in array_mess_receiver" :key="array_mess_receiver.id">
					<view v-if="array_mess_receiver[index].type == '快递'">
						<view class="block" @click="goto_1(array_mess_receiver[index].id)">
							<view class="pic2">
								<image class="picc" :src=array_mess_receiver[index].avatarurl></image>
							</view>
							<view class="text">
								<text>送货点：{{ array_mess_receiver[index].destination }}</text>
								<br>
								<text>送货方式：{{ array_mess_receiver[index].method }}</text>
								<br>
								<text>报酬：{{ array_mess_receiver[index].money}}</text>
							</view>
							<view class="pic1">
								<image class="pic" src="../../static/expressage.png"></image>
							</view>
						</view>
					</view>
					<view v-if="array_mess_receiver[index].type == '外卖'">
						<view class="block" @click="goto_1(array_mess_receiver[index].id)">
							<view class="pic2">
								<image class="picc" :src=array_mess_receiver[index].avatarurl></image>
							</view>
							<view class="text">
								<text>送货点：{{ array_mess_receiver[index].destination }}</text>
								<br>
								<text>送货方式：{{ array_mess_receiver[index].method }}</text>
								<br>
								<text>报酬：{{ array_mess_receiver[index].money}}</text>
							</view>
							<view class="pic1">
								<image class="pic" src="../../static/errand.png"></image>
							</view>
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
				current: 0,
				array_mess_payer: [],
				array_mess_receiver: []
			}
		},
		onLoad: function(r) {
			var that = this;
			if (that.current == 0) {
				uni.request({                            
					url: 'https://api.suishoubang.myrating.cn/order/showMyOrder',
					method:'GET',
					header: that.getHeader(),
					data:{  
						//查看订单类型 payer表示我下的订单 receiver表示我接的单
						personType: "payer",
						status: 0,
						orderType: ''
					},  
					success(r){
						var num = r.data.data.length;
						for (var i = 0; i < num; i++){
							var each_mess = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
							each_mess.id = r.data.data[i].id;
							each_mess.destination = r.data.data[i].payerPlace;
							each_mess.method = r.data.data[i].deliverType;
							each_mess.money = r.data.data[i].price;
							each_mess.type = r.data.data[i].type;
							each_mess.avatarurl = r.data.data[i].payerAvatarUrl;
							that.array_mess_payer.push(each_mess);
						}
						console.log(r);
						console.log(r.data.data.length);
						console.log(that.array_mess_payer)
					}
				})
			}
			if (that.current == 1) {
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/order/showMyOrder',
					method:'GET',
					header: that.getHeader(),
					data:{  
						//查看订单类型 payer表示我下的订单 receiver表示我接的单
						personType: "receiver",
						status: 0,
						orderType: ''
					},  
					success(r){
						var num = r.data.data.length;
						for (var i = 0; i < num; i++){
							var each_mess2 = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
							each_mess2.id = r.data.data[i].id;
							each_mess2.destination = r.data.data[i].payerPlace;
							each_mess2.method = r.data.data[i].deliverType;
							each_mess2.money = r.data.data[i].price;
							each_mess2.type = r.data.data[i].type;
							each_mess2.avatarurl = r.data.data[i].payerAvatarUrl;
							that.array_mess_receiver.push(each_mess2);
						}
						console.log(r);
						console.log(r.data.data.length);
						console.log(that.array_mess_receiver)
					}
				})
			}
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
			select1: function(r) {
				var that = this;
				that.current = 0;
				that.array_mess_payer = [];
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/order/showMyOrder',
					method:'GET',
					header: that.getHeader(),
					data:{  
						//查看订单类型 payer表示我下的订单 receiver表示我接的单
						personType: "payer",
						status: 0,
						orderType: ''
					},  
					success(r){
						var num = r.data.data.length;
						for (var i = 0; i < num; i++){
							var each_mess = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
							each_mess.id = r.data.data[i].id;
							each_mess.destination = r.data.data[i].payerPlace;
							each_mess.method = r.data.data[i].deliverType;
							each_mess.money = r.data.data[i].price;
							each_mess.type = r.data.data[i].type;
							each_mess.avatarurl = r.data.data[i].payerAvatarUrl;
							that.array_mess_payer.push(each_mess);
						}
						console.log(r);
						console.log(r.data.data.length);
						console.log(that.array_mess_payer)
					}
				})
			},
			select2: function(r){
				var that = this;
				that.current = 1;
				that.array_mess_receiver = [];
				uni.request({
					url: 'https://api.suishoubang.myrating.cn/order/showMyOrder',
					method:'GET',
					header: that.getHeader(),
					data:{  
						//查看订单类型 payer表示我下的订单 receiver表示我接的单
						personType: "receiver",
						status: 0,
						orderType: ''
					},  
					success(r){
						var num = r.data.data.length;
						for (var i = 0; i < num; i++){
							var each_mess2 = {id:'', destination:'', method:'', money:'', type:'', avatarurl:''};
							each_mess2.id = r.data.data[i].id;
							each_mess2.destination = r.data.data[i].payerPlace;
							each_mess2.method = r.data.data[i].deliverType;
							each_mess2.money = r.data.data[i].price;
							each_mess2.type = r.data.data[i].type;
							each_mess2.avatarurl = r.data.data[i].payerAvatarUrl;
							that.array_mess_receiver.push(each_mess2);
						}
						console.log(r);
						console.log(r.data.data.length);
						console.log(that.array_mess_receiver)
					}
				})
			},
			goto1(r) {
				var iid = r;
				console.log(iid);
				uni.navigateTo({
					url:"../myorder/myorder?id=" + iid
				})
			},
			goto(r) {
				var iid = r;
				console.log(iid);
				uni.navigateTo({
					url: '../myorder/myFood?id=' + iid
				})
			},
			goto_1(r) {
				var iid = r;
				console.log(iid);
				uni.navigateTo({
					url:"../myreceived/myreceived?id=" + iid
				})
			},
			goto_(r) {
				var iid = r;
				console.log(iid);
				uni.navigateTo({
					url: '../myreceived/myrecFood?id=' + iid
				})
			}
		}
	}
</script>

<style>
	page{
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
	.select{
		display: flex;
		flex-direction: row;
	}
	.butt{
		display: block;
		margin-top: 20px;
		border-radius: 13px;
		background-color: rgb(246,237,218);
		border: 3px solid rgb(219,229,228);
	}
	.pic1{
		margin-left: auto;
		margin-right: 20px;
	}
	.pic{
		margin-top: 10px;
		height: 70px;
		width: 70px;
		margin-left: auto;
		margin-right: 10px;
	}
	.sec_top{
		width: 100%;
		display: block;
		text-align: center;
	}
	.pic2{
		margin-left: 10px;
		margin-right: auto;
	}
	.picc{
		border-radius: 50%;
		margin-top: 10px;
		height: 40px;
		width: 40px;
	}
</style>

