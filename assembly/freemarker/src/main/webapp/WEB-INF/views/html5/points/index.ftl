<!doctype html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=0,maximum-scale=1" charset="UTF-8">
<title>积分中心</title>
<link type="text/css" href="${base}/res/html5/points/css/reset.css" rel="stylesheet"/>
<link type="text/css" href="${base}/res/html5/points/css/index.css" rel="stylesheet"/>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery.event.drag-1.5.min.js"></script>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery.touchSlider.js"></script>
</head>

<body>
<!--轮播图片start-->
<script type="text/javascript">
$(document).ready(function(){

	$(".main_visual").hover(function(){
		$("#btn_prev,#btn_next").fadeIn()
	},function(){
		$("#btn_prev,#btn_next").fadeOut()
	});
	
	$dragBln = false;
	
	$(".main_image").touchSlider({
		flexible : true,
		speed : 200,
		btn_prev : $("#btn_prev"),
		btn_next : $("#btn_next"),
		paging : $(".flicking_con a"),
		counter : function (e){
			$(".flicking_con a").removeClass("on").eq(e.current-1).addClass("on");
		}
	});
	
	$(".main_image").bind("mousedown", function() {
		$dragBln = false;
	});
	
	$(".main_image").bind("dragstart", function() {
		$dragBln = true;
	});
	
	$(".main_image a").click(function(){
		if($dragBln) {
			return false;
		}
	});
	
	timer = setInterval(function(){
		$("#btn_next").click();
	}, 5000);
	
	$(".main_visual").hover(function(){
		clearInterval(timer);
	},function(){
		timer = setInterval(function(){
			$("#btn_next").click();
		},5000);
	});
	
	$(".main_image").bind("touchstart",function(){
		clearInterval(timer);
	}).bind("touchend", function(){
		timer = setInterval(function(){
			$("#btn_next").click();
		}, 5000);
	});
	
});
</script>
<div class="main_visual">
	<#assign advPositionTag = newTag("advTag") />
    <#assign pointAdvs = advPositionTag("{'apId':'6'}") />
    <#if pointAdvs?? && pointAdvs!''>
	<#assign points = pointAdvs.advList />
		<div class="flicking_con">
			<#list points as point >
				<a href="#">${point_index+1}</a>
			</#list>
		</div>
		<div class="main_image">
			<ul>
				<#list points as point>
					<li>
						<span style="background: url('${imgServer }${point.resUrl}') center top no-repeat;background-size:cover;"></span>
					</li>
				</#list>
			</ul>
		</div>
	</#if>
</div>
<!--轮播图片end-->

<div class="duihuan">
推荐商品兑换
</div>
<div class="gengduo">
<a href="${base}/points/html5More">更多...</a>
</div>
<div id="content">
<#assign recommendPointsGoodsTag = newTag("recommendPointsGoodsTag") />
<#assign pager = recommendPointsGoodsTag("") />
<#list pager.result as pointsGoods >
	<div <#if (pointsGoods_index+1)%2==0>class="right_xiangxi"<#else>class="left_xiangxi"</#if>>
		<a href="${base}/points/pointsDetail?id=${pointsGoods.id}" target="_blank">
			<img src="${imgServer}${pointsGoods.pointsGoodsImage}" width="160px;" height="160px;" >
		</a>
		<p>${pointsGoods.pointsGoodsName}</p>
		<span style="color:#C81316;font-size:18px;">
			<b style="font-size:16px">需要积分</b> 
			${pointsGoods.pointsnums}
		</span>
	</div>
</#list>
</div>
<script>
	var height = 0;
	var length = $("#content div").length;
	if(length%2==0){
		height = length*120;
	}else{
		height = (length/2+1)*215;
	}
	$("#content").css("height",height);
</script>
 <div class="con_jz_bottom">
      <ul>
       <li><a href="${base}/m/index/index"><span></span><em>首页</em></a></li>
       <li><a href="#"><span></span><em>联系我们</em></a></li>
       <li><a href="#"><span></span><em>分享</em></a></li>
      </ul>
</div>
</body>
</html>
