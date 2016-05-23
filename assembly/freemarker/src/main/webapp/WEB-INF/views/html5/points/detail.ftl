<!doctype html>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=0,maximum-scale=1" charset="UTF-8">
<title>积分中心</title>
<link type="text/css" href="${base}/res/html5/points/css/reset.css" rel="stylesheet"/>
<link type="text/css" href="${base}/res/html5/points/css/detail.css" rel="stylesheet"/>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery.event.drag-1.5.min.js"></script>
<script type="text/javascript" src="${base}/res/html5/points/js/jquery.touchSlider.js"></script>
<!-- 手机端弹出框开始 -->
<script src="${base}/res/html5/js/mobile/layer.m.js" charset="utf-8"></script>
<!-- 手机端弹出框结束 -->
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
<#assign pointsGoodsTag = newTag("pointsGoodsTag") />
<#assign pointsGoods = pointsGoodsTag("{'id':'${id}'}") />
<#assign dateFormatTag = newTag("dateFormatTag") />
<#assign hotPointsGoodsTag = newTag("hotPointsGoodsTag") />
<#assign hotPointsOrderGoods = hotPointsGoodsTag("{'id':'${pointsGoods.id }'}") />
<div class="main_visual">
	<div class="flicking_con">
		<a href="#">1</a>
		<a href="#">2</a>
	</div>
	<div class="main_image">
		<ul>
			<li><span class="img_1"></span></li>
			<li><span class="img_2"></span></li>
		</ul>
	</div>
</div>
<!--轮播图片end-->
<div class="biaoti">
	所需积分:<span>&nbsp ${pointsGoods.pointsnums}积分</span><span>&nbsp V${pointsGoods.memberGradeId }会员</span>
</div>
<div class="jiage">
	<p>
		商品名称:<b>${pointsGoods.pointsGoodsName}</b>
	</p>
	<p>
		市场价格:<b><strike>${pointsGoods.pointsGoodsStorePrice}</strike>   |   礼品编号：${pointsGoods.pointsGoodsSerial}</b>
	</p>
    <#if pointsGoods.pointsGoodsStarttime??&&pointsGoods.pointsGoodsEndtime?? >
    <#assign pointsGoodsStarttime = dateFormatTag("{'date':'${pointsGoods.pointsGoodsStarttime }'}") />
    <#assign pointsGoodsEndtime = dateFormatTag("{'date':'${pointsGoods.pointsGoodsEndtime }'}") />
		<p>
			兑换开始:<b>${pointsGoodsStarttime }至${pointsGoodsEndtime }</b>
		</p>
	</#if>
	<p>
		剩余数量:<b>${pointsGoods.pointsGoodsStorage-hotPointsOrderGoods.pointGoodscount}</b>
		<input type="hidden" id="storagenum" value="${pointsGoods.pointsGoodsStorage-hotPointsOrderGoods.pointGoodscount }"/>
	</p>
	<p>
		每个会员最多兑换:<b>${pointsGoods.exchangeCount}个</b>
		<input name="limitnum" type="hidden" class="text" id="limitnum" value='${pointsGoods.exchangeCount}' size="4"/>
	</p>
	<p>兑换数量:</p>
	<span>
    	<input class="put"  type="text"  name="exnum"  id="exnum" value='1' size="4"  style="width:15%;height:25px;margin-top:-22%;margin-left:100px;"/>
	</span>
	<#assign nowTime=.now?long/>
    <#if pointsGoods.pointsGoodsShow==0>
    	 <input name= "anniu" type="button"  disabled="disabled" value="商品已下架" style="width:40%; height:35px;color:#fff;font-weight:bold;margin-bottom:2%;margin-left:-145px;font-size:16px;background:gray; line-height:30px;border-radius: 4px;"/>
    <#elseif pointsGoods.pointsGoodsStorage-hotPointsOrderGoods.pointGoodscount ==0>
    	 <input name= "anniu" type="button"  disabled="disabled" value="商品已售罄" style="width:40%; height:35px;color:#fff;font-weight:bold;margin-bottom:2%;margin-left:-145px;font-size:16px;background:gray; line-height:30px;border-radius: 4px;"/>
    <#elseif pointsGoods.pointsGoodsStarttime &gt;=nowTime || pointsGoods.pointsGoodsEndtime &lt;=nowTime >
    	 <input name= "anniu" type="button"  disabled="disabled" value="兑换结束" style="width:40%; height:35px;color:#fff;font-weight:bold;margin-bottom:2%;margin-left:-145px;font-size:16px;background:gray; line-height:30px;border-radius: 4px;"/>
    <#else>
	    <input name= "anniu" type="button"  value="我要兑换" onclick="add_to_cart();" style="width:40%; height:35px;color:#fff;font-weight:bold;margin-bottom:2%;margin-left:-145px;font-size:16px;line-height:30px;background: #FD6161;border-radius: 4px;"/>
	</#if>
</div>
<div class="xiangqing">
   <h2 style="font-size: 18px;padding-right:15px;">礼品介绍</h2>
   <P style="padding-right:20px;">
	   ${pointsGoods.pointsGoodsBody }
   </P>
</div>
<script type="text/javascript">
var isLogin = false;
<@shiro.user>
	isLogin = true;
</@shiro.user>
//加入购物车
function add_to_cart(){
	if(!isLogin){
		layer.open({content:'请先登录', time: 1});
		location.href="${base}/m/index/login";
	}else{
		var storagenum = parseInt($("#storagenum").val());//库存数量
		var limitnum = parseInt($("#limitnum").val());//限制兑换数量
		var quantity = parseInt($("#exnum").val());//兑换数量	
		//验证数量是否合法
		var checkresult = true;
		var msg = '';
		if(!quantity >=1 ){//如果兑换数量小于1则重新设置兑换数量为1
			quantity = 1;
		}
		if(limitnum > 0 && quantity > limitnum){
			checkresult = false;
			msg = '兑换数量不能大于限兑数量';
		}
		if(storagenum > 0 && quantity > storagenum){
			checkresult = false;
			msg = '兑换数量不能大于剩余数量';
		}
		if(checkresult == false){
			layer.open({content:msg, time: 1});
			//layer.alert(msg,{icon:2});
			return false;
		}else{
			//异步地址
			var url = '${base}/points/addCart';
			//参数
			var args = {"shopPointsGoodsId":'${pointsGoods.id}',"pointsGoodsNum":quantity};
			//异步加载table
			$.post(url, args, function(result){
				if(result.result=='true'){
					//layer.open({content:'加入购物车成功', time: 1});
					window.location.href = '${base}/points/pointsCart';
				}else{
					layer.open({content:result.msg, time: 1});
					//layer.alert(result.msg,{icon:2});
				}
				return false;
			});
			
		}
	}
}
</script> 
</body>
</html>
