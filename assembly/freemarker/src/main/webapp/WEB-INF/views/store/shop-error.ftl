<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LmShop B2B2C V3.0 2015版商城系统-欢迎登录</title>
    <meta name="keywords" content="商城系统演示站"/>
    <meta name="description" content="商城系统演示站"/>
    <meta name="author" content="商城系统演示站">
    <meta name="copyright" content="商城系统演示站">
    <link rel="shortcut icon" href="res/css/favicon.ico"/>

    <link href="${base}/res/css/base.css" rel="stylesheet" type="text/css">
    <link href="${base}/res/css/home_header.css" rel="stylesheet" type="text/css">
    <link href="${base}/res/css/home_login.css" rel="stylesheet" type="text/css">
    <link href="${base}/res/css/login.css" rel="stylesheet" type="text/css">
    <script src="${base}/res/js/jquery.js" ></script>
    <script src="${base}/res/js/jquery.ui.js" ></script>
    <script src="${base}/res/js/jquery.validation.min.js" ></script>
    <script src="${base}/res/js/html5shiv.min.js"></script>
    <script type="text/javascript" src="${base}/res/js/layer/layer.js"></script>
</head>
<body>
<!--头部-->
<!-- PublicHeadLayout Begin -->
<div class="header-wrap">
    <header class="public-head-layout wrapper">
        <!--LOGO-->
        <h1 class="site-logo"><a href="${base}/"><img src="${base}/res/images/common/690ea902fe2708381da2cabff4ee46c8.png" class="pngFix"></a></h1>
        <!--搜索-->
    </header>
</div>
<!-- PublicHeadLayout End -->
<div class="clear"></div>
<script type="text/javascript">
    $(function () {
        $("#username").focus();
    });
    function changeCaptcha() {
        var captchaImg = '${base}/generateImage?t=' + Math.random();
        $("#captcha_img").attr("src", captchaImg);
    }
</script>
<style type="text/css">
    .wrapper {
        width: 1000px;
    }#footer-top ul li{margin-right:60px;}body,.header-wrap{background-color:#f2f2f2;}
</style>
<div class="nc-login-layout">
    <div class="nc-login-main">
        <div class="left-pic" style="padding-left:190px;font-size:25px;text-align:center;line-height:350px;color: red;">
               <#if store.storeState==0>
			        	 该店铺已关闭
			   </#if>
			   <#if store.storeState==2>
			     	该店铺正在审核中
			   </#if>
			   <#if store.isDel==1>
			    	 该店铺不存在
			   </#if>
			   <span id="timeto" style=" font-size: 14px;"></span>
        </div>
        <div class="nc-login">
            <div class="nc-login-content" id="demo-form-site">
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<script>
var wait=5;
$(function() {
	time();
});
function time() {
	//console.log(wait);
	if(wait==0){
		location.href="${base}/";
	}else{
		var timeto = $("#timeto");
		timeto.html("(" + wait + "秒后返回首页)");
		wait--;
		setTimeout(function() {
			time();
		}, 1000);
	}
}
</script>
<div class="clear"></div>
<!-----footer------>
<@p.footer/>
