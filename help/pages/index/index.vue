<template>
	<view>
		<view class="top">
			<!-- <input class="top_input" type="text" confirm-type="search" />
			<image class="search" src="../../static/search.png"></image> -->
			<navigator url="../add/add" hover-class="nevigator-hover">
				<image class="add" src="../../static/add.png"></image>
			</navigator>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		/* onLoad() {

		}, */
		methods: {

		}
	}
</script>

<style>	
	.top{
		height: var(--status-bar-height);
		width: 100%;
	}
	
	.search{
		height: 20px;
		width: 20px;
		margin-top: 0;
		margin-left: auto;
	}
	
	.add{
		height: 20px;
		width: 20px;
		margin-top: 0;
		margin-left: 90%;
	}
	
	.top_input{
		height: 20px;
		width: 80%;
		
		border-radius: 20px;
		border: solid 1px #C0C0C0;
	}
</style>
