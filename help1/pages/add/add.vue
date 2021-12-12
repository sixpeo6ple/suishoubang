<template>
	<view class="add" style="background-image: url(../../static/background2.JPG); background-size:100% 100%;">
		<view class="combination">
			<navigator url="../expressage/expressage">
			<view class="comb">
				<image class="pic" src="../../static/expressage.png"></image>
				<text class="label">快递代取</text>
			</view>
			</navigator>
			<view class="comb">
				<image class="pic" src="../../static/errand.png"></image>
				<text class="label">校园跑腿</text>
			</view>
			<view class="comb">
				<image class="pic" src="../../static/game.png"></image>
				<text class="label">游戏陪玩</text>
			</view>
			<view class="comb">
				<image class="pic" src="../../static/eat.png"></image>
				<text class="label">拼饭拼单</text>
			</view>
			<view class="comb">
				<image class="pic" src="../../static/lease.png"></image>
				<text class="label">物品租借</text>
			</view>
			<view class="comb">
				<image class="pic" src="../../static/part-job.png"></image>
				<text class="label">外快兼职</text>
			</view>
			<view class="comb">
				<image class="pic" src="../../static/consult.png"></image>
				<text class="label">校园咨询</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style>
	page{
		height: 100%;
	}
	.add{
		height: 100%;
		display: flex;
		flex-direction: column;
	}
	.combination{
		margin-top: 20px;
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	}
	.comb{
		margin-top: 15px;
		display: flex;
		flex-direction: column;
		align-items: center;
		align-content: center;
		width: 200px;
	}
	.pic{
		height: 80px;
		width: 40%;
	}
	.label{
		margin: auto;
	}
</style>
