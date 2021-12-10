<template>
	<view>
		<view class="blank3"></view>
		<view class="expressage_message">
			<view class="place">
				<text class="text_label">取货码</text>
				<input class="in" @input="fcode"/>
			</view>
			<view class="blank4"></view>
			
			<view class="place">
				<text class="text_label">送货日期</text>
				<!-- 日期选择器 -->
				<view class="uni-list-cell-db">
				    <picker mode="date" :value="exp_data.date" :start="startDate" :end="endDate" @change="bindDateChange">
				        <view class="uni-input">{{ exp_data.date }}</view>
				    </picker>
				</view>
			</view>
			<view class="blank4"></view>
			<view class="place">
				<text class="text_label">送货时间</text>
				<!-- 时间选择器 -->
				<view class="uni-list">
				    <picker mode="time" :value="time" start="08:00" end="22:00" @change="bindTimeChange">
				        <view class="uni-input">{{ time }}</view>
			        </picker>
				</view>
			</view>
			<view class="blank4"></view>
			<view class="place">
				<text class="text_label">送货方式</text>
				<!-- 单选 -->
				<radio-group @change="radioChange">
					<view class="select">
						<label class="uni-list-cell" v-for="(item, index) in items" :key="item.value">
							<view>
								<radio :value="item.value" :checked="index === current" />
							</view>
							<view>{{item.name}}</view>
						</label>
					</view>
				</radio-group>
			</view>
			<view class="blank3"></view>
			<view class="place">
				<text class="text_label_">快递大小</text>
				<input class="in" @input="fbag_size"/>
			</view>
			<view class="blank4"></view>
			<!-- 上传身份码 -->
			<view class="pic">
				<text class="text_label_">上传身份码</text>
				<view class="img_box" @click="addpic">
					<image class="img" src="../../static/onload.png"></image>
				</view>
				<view class="img_box">
					<image class="img" :src="exp_data.idcode_url" mode="aspectFill"></image>
				</view>
			</view>
			<view class="blank4"></view>
			<view class="place">
				<text class="text_label_">悬赏价格</text>
				<input class="in" @input="fmoney"/>
			</view> 
		</view>
		
		<view class="bottom">
			<button class = "sub" @click="frelease">发布</button>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			const currentDate = this.getDate({
			    format: true
			})
			return{
				items: [{
					/* 单选数据 */
					value: 'in',
					name: "送货上门"
					},
					{
						value: 'under',
						name: "送到楼下"
					},
				],
				/* 单选数据结束 */
				time: '08:00',
				exp_data: {
					date: '年-月-日',	// 送货日期
					code: '',	// 取件码
					receviced_time: '08:00',	// 送货时间
					method: '',		//送货方式
					bag_size: '',	//包裹
					money: '',		//悬赏金额
					idcode_url: uni.getStorageSync("idcodeurl")		//身份码照片
				}
			}
		},
		computed: {
		    startDate() {
		        return this.getDate('start');
		    },
		    endDate() {
		        return this.getDate('end');
		    }
		},
		methods: {
			/* 获取输入数据 */
			fcode: function(e){
				this.exp_data.code = e.detail.value;
				console.log(this.exp_data.code)
			},
			/* 日期选择方法 */
			bindDateChange: function(e) {
			    this.exp_data.date = e.target.value;
				console.log(this.exp_data.date)
			},
			getDate(type) {
			    const date = new Date();
			    let year = date.getFullYear();
			    let month = date.getMonth() + 1;
			    let day = date.getDate();
			    if (type === 'start') {
			        year = year - 60;
			    } else if (type === 'end') {
			        year = year + 2;
			      }
			    month = month > 9 ? month : '0' + month;
			    day = day > 9 ? day : '0' + day;
			    return '${year}-${month}-${day}';
			},
			/* 时间选择方法 */
			bindTimeChange: function(e) {
				this.exp_data.receviced_time = e.target.value;
				console.log(this.exp_data.receviced_time);
			},
			/* 时间选择方法结束 */
			/* 单选方法 */
			radioChange: function(e){
				for (let i = 0; i < this.items.length; i++) {
				    if (this.items[i].value === e.detail.value) {
				        this.current = i;
						console.log(this.current);
						console.log(this.items[i].name);
						this.exp_data.method = this.items[i].name;
				        break;
				    }
				}
			},
			/* 单选方法结束 */
			fbag_size: function(e){
				this.exp_data.bag_size = e.detail.value;
				console.log(this.exp_data.bag_size);
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
										uni.setStorageSync('idcodeurl', r.data.data.URL);
										console.log(r);
										console.log(r.data.data.URL)
									}
								})
							}
						})
						that.exp_data.idcode_url = res.tempFilePaths;
						console.log(that.exp_data.idcode_url);
					}
				})
			},
			fmoney: function(e){
				this.exp_data.money = e.detail.value;
				console.log(this.exp_data.money);
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
			/* 发布按钮方法 */
			frelease: function(e){
				var that = this;
				console.log(that.exp_data);
				/* uni.setStorageSync("place", that.exp_data.place);
				uni.setStorageSync("yizhan_name", that.exp_data.yizhan_name);
				uni.setStorageSync("code", that.exp_data.code);
				uni.setStorageSync("receviced_time", that.exp_data.receviced_time);
				uni.setStorageSync("method", that.exp_data.method);
				uni.setStorageSync("bag_size", that.exp_data.bag_size);
				console.log("需求数据已缓存"); */
				console.log(JSON.stringify(that.exp_data.idcode_url));
				if (uni.getStorageSync('idcodeurl') === undefined || uni.getStorageSync('idcodeurl') === ''
					|| that.exp_data.code == '' || that.exp_data.bag_size == '' || that.exp_data.money == ''
					|| that.exp_data.method === '' || that.exp_data.method === undefined) {
						uni.showModal({
							title: '提示',
							content: '请填写完整信息'
						})
				}
				else{
					uni.request({
						url: 'https://api.suishoubang.myrating.cn/order/create',
						method: 'Post',
						data: { 
							picUrl: uni.getStorageSync('idcodeurl'),
							info: that.exp_data.code,
							type: that.exp_data.bag_size,
							price: that.exp_data.money,
							deliverTime: that.exp_data.date + ' ' + that.exp_data.receviced_time + ':00',
							deliverType: that.exp_data.method
						},
						header: that.getHeader(),
						success(r) {
							console.log(r);
							if (r.data.msg == "没有通过学生认证") {
								uni.showModal({
									title: '提示',
									content: '请先进行学生认证'
								})
							}
							else{
								console.log("数据缓存成功");
								uni.showModal({
									content: '发布成功'
								})
								/* uni.navigateTo({
									url:'../add/add'
								}) */
							}
						}
					})
				}
			}
		}
	}
</script>

<style>
	.blank3{
		height: 15px;
	}
	.blank4{
		height: 1px;
	}
	.expressage_message{
		width: 90%;
		margin: auto;
	}
	.place{
		width: 90%;
		height: 45px;
		border-radius: 12px;
		border-style: solid;
		border-color: gray;
		display: flex;
	}
	.text_label{
		width: 22%;
		height: 20px;
		display: block;
		margin-left: 10px;
		margin-top: 11px;
		border-style: inset;
		border-color: #00aaff;
		border-width: 1px;
		border-radius: 8px;
		color: #ffffff;
		background-color: #00aaff;
	}
	.in{
		display: block;
		height: 30px;
		margin-left: 10px;
		margin-top: 5px;
		border-top-width: 0;
		border-left-width: 0;
		border-right-width: 0;
		border-bottom-width: 1px;
		border-style: solid;
		border-color: #ff00ff;
	}
	.select{
		display: flex;
	}
	.uni-list{
		
	}
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
