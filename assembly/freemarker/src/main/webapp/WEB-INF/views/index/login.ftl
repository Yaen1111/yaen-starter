<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Generator" content="68ECSHOP v4_1" />
<title>LmShop B2B2C V3.0 2015版商城系统-欢迎登录</title>
<meta name="keywords" content="商城系统演示站"/>
<meta name="description" content="商城系统演示站"/>
<meta name="author" content="商城系统演示站">
<meta name="copyright" content="商城系统演示站">
<link rel="shortcut icon" href="${base}/resjd/images/index/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${base}/resjd/css/68ecshopcom_360buy/68ecshop_commin.css" />
<link type="text/css" rel="stylesheet" href="${base}/resjd/css/68ecshopcom_360buy/passport.css"/>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/jquery_email.js"></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/jquery-1.9.1.min.js" ></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/placeholder.js" ></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/user.js" ></script>
<script type="text/javascript" src="${base}/resjd/js/indexjs/jquery.json.js"></script>
<script type="text/javascript" src="${base}/resjd/js/indexjs/transport.js"></script>
<script type="text/javascript" src="${base}/resjd/js/indexjs/common.js"></script>
<script type="text/javascript" src="${base}/resjd/js/index/user.js"></script>
<script src="res/js/jquery.js" ></script>
<script src="res/js/jquery.validation.min.js" ></script>
<script type="text/javascript" src="${base}/res/js/layer/layer.js"></script>
<style>
/* 用户登录错误验证样式 */
.error {
    color: #F30;
    line-height:20px;
    letter-spacing:1px;
}
</style>
</head>
<body onclick="ecshop68_onclick();">
<script>
function erweima1(obj, sType) { 
	var oDiv = document.getElementById(obj); 
	if (sType == 'show') {oDiv.style.display = 'block';} 
	if (sType == 'hide') {oDiv.style.display = 'none';} 
}
//变换验证码
function changeCaptcha() {
    var captchaImg = '${base}/generateImage?t=' + Math.random();
    $("#captcha_img").attr("src", captchaImg);
}
$(document).ready(function(){
    $("#login_form ").validate({
        errorPlacement: function(error, element){
           var error_td = element.parent('div');
           error_td.find('span').hide();
           error_td.append(error);
        },
        rules: {
            username: "required",
            password: "required",
			captcha:{
				required:true,
				remote:{
					url: "${base}/webcodeCheck",  //后台处理程序
		            type: "post",               //数据发送方式
		            dataType: "json",           //接受数据格式 
		            async:false,
		            data: {                     //要传递的数据
		                  "captcha": function(){return jQuery("#code").val();}
				    }
				}
		   }
        },
        messages: {
			username:{required:"用户名不能为空"},
			password:{required:"密码不能为空"},
			captcha:{
		  		required:"验证码不能为空",
		  		remote:"验证码不正确"
			}
	  }
    });
    
    $('#submitBtn').click(function(){
    	loginFun();       
    });   
    
    //回车登陆事件
    document.onkeydown = function(e){ 
    	var ev = document.all ? window.event : e;
	    if(ev.keyCode==13) {
        	loginFun();    
	    }
    }
});

// 登录方法函数
var loginFun = function(){
	if($("#login_form").valid()){
		$.ajax({
            type: "post",
            url: 'loginCheck',
            data: $("#login_form").serialize(),
            dataType: "json",
            async:true,
            success:function(data) {
                if(data.success){
                    //$("#errors").html("");
					var referer = '${referer}';
					if(referer.indexOf("signUp") || referer.indexOf("login")){
                        window.location.href='${base}/';
					} else {
                        window.location.href='${referer}';
					}

                }else{
                	layer.msg(data.message,{icon: 2,time:1000});
                    //$("#errors").html(data.message);
                }
            }
        });
	}
}
</script>
<div class="Logo-r">
	<div class="Logo-info-r"> 
		<a href="${base}/" class="logo"></a>
		<span class="findpw">欢迎登录</span>
		<!-- <a href="${qqnurl}" class="qq" ></a> 
		<a href="${sinaurl}" class="sina"></a>  -->
		<!-- <span href="${weixinurl}" class="weixin" onmouseover="erweima1('erweima', 'show');" onmouseout="erweima1('erweima', 'hide');"></span> -->
	    <div class="erweima" id="erweima">
			<img alt="微信" src="${base}/resjd/images/index/68ecshopcom_360buy/images/login/weixin3.png" width="167" height="202">
		</div>
	</div>
</div>
  <div class="blank"></div>
  <div class="blank"></div>
  <div class="w"> 
    <div class="w" id="entry">
    <div class="mcon">
      <div id="reg-pic" class="box-pic"></div>
      <div id="login-box" class="uc_box">
        <form id="login_form" action="" method="post">
          <h2><span>还没有账号？<a href="${base}/signUp">立即注册</a></span></h2>
          <div class="form">
            <div class="item">
            	<span class="label">账户名</span>
            	<div>
	                <input type="text" id="username"  name="username" class="text" tabindex="1" placeholder="手机号/用户名/邮箱"/>
	                <span class="error"></span>
                </div>
            </div>
            <div class="item"> 
            	<span class="label">密码</span>
            	<div>
	                <input type="password" id="password" name="password" class="text" tabindex="2"/>
	                <span class="error"></span>
                </div>
            </div>
            <div class="item " id="o-authcode"> 
            	<span class="label">验证码</span>
                <div>
	                <input type="text" name="captcha" id="code" maxlength="4" size="10" class="text text-1" tabindex="3"/>
	                <label class="img"> 
	                	<img src="generateImage" title="看不清？点击换一张" onclick="changeCaptcha()" id="captcha_img" alt="captcha" style="vertical-align: middle;cursor: pointer;"  /> 
	                </label>
	                <span class="error"></span>
                </div>
            </div>
            <div class="item" id="autoentry">
	            <div class="safety">
		            <input type="checkbox" value="1" name="remember" id="remember" class="checkbox"/>
		            <label for="remember" class="mar-b">请保存我这次的登录信息。</label>
		            <a class="forget_password" href="${base}/forget/index" style="color:#E4393C">忘记密码？</a><input type="hidden" value="index" name="ref_url">
	            </div>
            </div>
            <div class="item">
              	<!-- <input type="hidden" name="act" value="act_login" />
              	<input type="hidden" name="back_act" value="http://192.168.1.240/smallJD/" /> -->
              	<input type="button" name="submit" class="btn-img btn-entry" id="submitBtn" value="立即登录" />
            </div>
            <div class="blank"></div>
            <div class="item">
              <table width="300" border="0" cellpadding="5" cellspacing="0">
                <tr>
		          <td><a style="text-decoration:none" href="${weixinurl}"><i class="weixin"></i>&nbsp;&nbsp;微信</a></td>
                  <td><a style="text-decoration:none" href="${qqnurl}"><i class="qq"></i>&nbsp;&nbsp;QQ</a></td>
                  <td><a style="text-decoration:none" href="${sinaurl}"><i class="sina"></i>&nbsp;&nbsp;新浪</a></td>
                  <!-- <td><a style="text-decoration:none" href="#"><i class="alipay"></i>&nbsp;&nbsp;支付宝</a></td> -->
                </tr>
              </table>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
 <script>
$(".mcon").height($(".uc_box").height());
</script>  
    <div class="blank"></div>
  </div>
  
<div class="site-footer">
  <div class="wrapper">
    <div class="footer-info clearfix" >
	    <div class="info-text">
		    <p class="nav_bottom">
			    <#assign navTag = newTag("navigationTag") />
           	 	<#assign navTagList = navTag("{'navLocation':2,'navType':0,'navItemId':0}") />
	            <#if navTagList??>
	            	<#list navTagList as navigtion >
	            		<a href="${navigtion.navUrl }" <#if navigtion.navNewOpen==1>target="_blank"</#if> >${navigtion.navTitle }</a>
	                	<#if (navigtion_index+1 < navTagList?size)>|</#if>
	                </#list>
	             </#if>
	             |&nbsp;<a href="${base}/m/index/index" target="_blank">触屏版</a>
	             |&nbsp;<a href="${sellerServer}/login" target="_blank">商家管理中心</a>
	             |&nbsp;<a href="${adminServer}/login" target="_blank">后台管理中心</a>
	             |&nbsp;<a href="https://itunes.apple.com/cn/app/lei-ming-dian-shang/id1036114012?mt=8" target="_blank">ios原生版</a>
	         	 |&nbsp;<a href=" http://imjcoder.cn/qdmall-front/apk/LeimingOnline.apk" target="_blank">android原生版</a>
		    </p>
		    <p>
			    <a href="#">  &copy; 2011-2015 qdmall团队 Inc.All rights reserved.</a>
			    <a href="http://imjcoder.cn/qdmall-front">qdmall商城b2b2c</a>
			    <a href="#">      Tel: 010-65906171 13911052818      </a>
			    <a href="mailto:4407509@qq.com">      E-mail: 4407509@qq.com     </a>
		    </p>
		    <p>
		   <!--  <a href="http://wpa.qq.com/msgrd?V=1&amp;Uin=800007396&amp;Site=标题标题标题标题标题标题标题标题&amp;Menu=yes" target="_blank"><img src="http://wpa.qq.com/pa?p=1:800007396:4" height="16" border="0" alt="QQ" /> 800007396</a> -->
	    </div>      
    </div>    
  </div>
</div>

</body>
</html>
