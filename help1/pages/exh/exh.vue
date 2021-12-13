<template>
	<image :src="po" mode="widthFix"></image>
</template>

<script>
	export default {
		data() {
			return{
				po: ''
			}
		},
		onLoad: function(r){
			this.po = r.po
		},
		methods:{
			
		}
	}
</script>

<style>
</style>
