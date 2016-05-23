<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="Generator" content="68ECSHOP v4_1" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="" />
<meta name="Description" content="" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>用户中心_标题名称标题名称版-标题名称标题名称  </title>
<link rel="shortcut icon" href="${base}/resjd/images/index/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${base}/resjd/css/68ecshopcom_360buy/68ecshop_commin.css" />
<link type="text/css" rel="stylesheet" href="${base}/resjd/css/68ecshopcom_360buy/passport.css"/>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/jquery_email.js"></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/jquery-1.9.1.min.js" ></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/placeholder.js" ></script>
<script type="text/javascript" src="${base}/resjd/js/68ecshopcom_360buy/validate/messages_zh.js"></script>
<script type="text/javascript" src="${base}/resjd/js/index/js/jquery.json.js"></script>
<script type="text/javascript" src="${base}/resjd/js/index/js/transport.js"></script>
<script type="text/javascript" src="${base}/resjd/js/index/js/common.js"></script>
<script type="text/javascript" src="${base}/resjd/js/index/register.js"></script>
<script src="${base}/resjd/js/index/jquery.validation.min.js" ></script>
<script type="text/javascript" src="${base}/res/js/layer/layer.js"></script>
<script>
$(function(){
	$(".tab_item").click(function(){
		var register_type = $(this).attr("register_type");
		$("#register_type").val(register_type);
		if(register_type=='mobile'){
			$("#tab").val(0);
		}
		if(register_type=='email'){
			$("#tab").val(1);
		}
		$("#formRegister").submit();
	});
	
});
</script>
</head>
<body onclick="ecshop68_onclick();">
<script>
function erweima1(obj, sType) { var oDiv = document.getElementById(obj); if (sType == 'show') {oDiv.style.display = 'block';} if (sType == 'hide') {oDiv.style.display = 'none';} }
</script>
	<div class="Logo-r">
		<div class="Logo-info-r">
			<a href="${base}/" class="logo"></a>
            <span class="findpw">欢迎注册</span>
			<!-- <a href="${qqnurl}" class="qq" ></a> 
			<a href="${sinaurl}" class="sina"></a> 
			<span href="${weixinurl}" class="weixin" onmouseover="erweima1('erweima', 'show');" onmouseout="erweima1('erweima', 'hide');"></span> -->
			<div class="erweima" id="erweima">
				<img alt="微信" src="${base}/resjd/images/index/68ecshopcom_360buy/images/login/weixin3.png" width="167" height="202">
			</div>
		</div>
	</div>
	<div class="blank"></div>
	<div class="blank"></div>
	<div class="w">
	<script type="text/javascript" src="js/utils.js"></script>		
	<div class="w" id="regist">
			<div class="mcon register_con">
				<div id="reg-pic" class="box-pic box-pic1"></div>
				<div id="login-box" class="uc_box uc_box_reg">
                	<form action="${base}/signUp" method="post" id="formRegister" name="formRegister">
                        <input type="hidden" id="register_type" name="register_type" value="" />
                        <input type="hidden" id="tab" name="tab" value="" />
                        <ul class="register_tab">
                            <li id="li_register_type_mobile"  register_type="mobile"<#if tab==0>class="tab_item fl cur"<#else> class="tab_item fl"</#if>>手机注册<span class="bg_ff"></span></li>
                            <li id="li_register_type_email" register_type="email"<#if tab==1>class="tab_item fl cur"<#else> class="tab_item fl"</#if>>邮箱注册<span class="bg_ff"></span></li>
                            <li class="have_account fr">已有账号！<a href="${base}/login">登录</a></li>
                        </ul>
                    </form>
					<form id="register_form" method="post" action="" >
						<div class="form">
							<!--手机注册 -->
							<#if tab==0>
								<input type="hidden" name="register_type" value="mobile" />
								<div class="item">
									<input id="mobile" name="memberMobile" type="text"  placeholder="手机"  class="text" onchange="validateMobile();"/>
									<i class="i-phone"></i>
									<div>
										<span class="label" id="mobileError" style="color:red;"></span>
									</div>
								</div>
								<div id="o-password">
									<div class="item">
										<input name="memberPasswd" type="password" id="password" placeholder="密码(6-16位字符，由英文、数字及标点组成)"  onkeyup="checkIntensity(this.value)" onchange="validatePass();" class="text" />
										<i class="i-pass"></i>
										<div id="pwd_intensity">
											<span class="label">
											<table width="295" height="25px" border="0" cellspacing="0" cellpadding="0" style="padding: 0px 0px 10px;">
												<tr align="center">
													<td width="33%" id="pwd_lower">弱</td>
													<td width="33%" id="pwd_middle">中</td>
													<td width="33%" id="pwd_high">强</td>
												</tr>
											</table>
											</span>
										</div>
									</div>
									<div class="item">
										<input type="password" id="passwordConfirm" name="passwordConfirm" onchange="validatePassConfirm();" class="text" placeholder="确认密码" autocomplete="off" />
										<i class="i-pass"></i>
										<div>
											<span class="label" id="PassError" style="color:red;"></span>
										</div>
									</div>
								</div>
								<div class="item">
									<input id="mobileCode" class="text text_te" type="text" placeholder="手机验证码" name="mobileCode">
									<i class="i-phone"></i>
									<input id="zphone" class="zphone" type="button" value="获取手机验证码 ">
									<span id="timeto" style="color:red;"></span> 
									<div>
										<span id="zphoneCode" class="label" style="padding-top:40px;margin-left:-240px;color:red;"></span>
									</div>
								</div>
								<div class="item item_t">
									<div class="safety">
										<input name="agreement" type="checkbox" value="1" checked="checked" class="checkbox" />
										<label for="remember" class="mar-b">我已看过并接受《<a href="${base}/help/content?acId=9&articleId=45" style="color:red;" target="_blank;">用户协议</a>》</label>
									</div>
								</div>
								<div class="item">
									<input name="act" type="hidden" value="register">
									<input type="hidden" name="back_act" value="user.php" />
									<input type="button" id="submitBtn" name="Submit" class="btn-img btn-regist" value="立即注册" />
								</div>
							</#if>
							<#if tab==1>
								<!--邮箱注册 -->
								<input type="hidden" name="register_type" value="email">
								<div class="item">
									<input id="email" name="memberEmail" type="text"  onchange="validateEmail()" placeholder="邮箱" class="text email">
									<i class="i-email"></i>
									<div>
										<span class="label" id="emailError" style="color:red;"></span>
									</div>
								</div>
								<div id="o-password">
									<div class="item">
										<input name="memberPasswd" type="password" id="password" placeholder="密码(6-16位字符，由英文、数字及标点组成)"  onkeyup="checkIntensity(this.value)" onchange="validatePass();" class="text" />
										<i class="i-pass"></i>
										<div id="pwd_intensity">
											<span class="label">
											<table width="295" height="25px" border="0" cellspacing="0" cellpadding="0" style="padding: 0px 0px 10px;">
												<tr align="center">
													<td width="33%" id="pwd_lower">弱</td>
													<td width="33%" id="pwd_middle">中</td>
													<td width="33%" id="pwd_high">强</td>
												</tr>
											</table>
											</span>
										</div>
									</div>
									<div class="item">
										<input type="password" id="passwordConfirm" name="passwordConfirm" class="text" onchange="validatePassConfirm();" placeholder="确认密码" autocomplete="off" />
										<i class="i-pass"></i>
										<div>
											<span class="label" id="PassError" style="color:red;"></span>
										</div>
									</div>
							    </div>
								<div class="item">
									<input id="emailCode" class="text text_te" type="text" placeholder="邮箱验证码" name="emailCode" maxlength="8">
									<i class="i-email"></i>
									<input id="zemail" class="zemail" type="button" value="获取邮箱验证码 ">
									<span id="timeto" style="color:red;"></span> 
									<div>
										<span id="zemailCode" class="label" style="padding-top:40px;margin-left:-240px;color:red;"></span>
									</div>
								</div>
								<div class="item item_t">
									<div class="safety">
										<input name="agree" type="checkbox" value="1" checked="checked" class="checkbox">
										<label for="remember" class="mar-b">我已看过并接受《<a href="${base}/help/content?acId=9&articleId=45" style="color:red;" target="_blank;">用户协议</a>》</label>
									</div>
								</div>
								<div class="item">
									<input name="act" type="hidden" value="register">
									<input type="hidden" name="back_act" value="user.php">
									<input type="button" id="submitBtn" name="Submit"  class="btn-img btn-regist" value="立即注册">
								</div>
							</#if>
						</div>
					</form>
					<!--[if !ie]>form end<![endif]-->
					
				</div>
			</div>
		</div>
				
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
<script type="text/javascript">
var wait=60;
function time(o) {
	var timeto = $("#timeto");
	console.log(wait);
	if (wait == 0) {
		timeto.html("");
		o.removeAttr("disabled");
		wait = 60;
	} else {
		o.attr("disabled", true);
		timeto.html("重新发送(" + wait + ")");
		wait--;
		setTimeout(function() {
			time(o)
		}, 1000)
	}
	return false;
}
//注册表单验证
$(function(){
	var phoneflag = true;
	//获取手机验证码
	$("#zphone").click(function() {
		var self = $(this);
		var mobile = $('#mobile').val();
		if(!validateMobile()){
			phoneflag = false;
			return false;
		}
		var getTimestamp = new Date().getTime();
		$.post("${base}/getMobileCode?mobile=" + mobile
				+ "&currStr=" + getTimestamp, null, function(data) {
			if (data) {
				if (data.success == "true") {
				    time(self);//验证用户不存在后再出验证码
					$("#zphoneCode").html("");
					$("#zphoneCode").html(data.message);
					phoneflag = true;
					return true;
				} else {
					$("#zphoneCode").html("");
					$("#zphoneCode").html(data.message);
					phoneflag = false;
					return false;
				}
			}
		});
	});
	//获取邮箱验证码
	$("#zemail").click(function() {
	    var self = $(this);
		var email = $('#email').val();//邮箱
		if(!validateEmail()){
			phoneflag = false;
			return false;
		}
		var getTimestamp = new Date().getTime();
		$.post("${base}/getEmailCode?email=" + email
				+ "&currStr=" + getTimestamp, null, function(data) {
			if (data) {
				if (data.success == "true") {
				    time(self);//验证用户不存在后再出验证码
					$("#zemailCode").html("");
					$("#zemailCode").html(data.message);
					phoneflag = true;
					return true;
				} else {
					$("#zemailCode").html("");
					$("#zemailCode").html(data.message);
					phoneflag = false;
					return false;
				}
			}
		}); 
	});
    //表单提交
    $("#submitBtn").click(function(){
    	var tab = ${tab};
    	if(tab==0){
    		//手机注册
            if(validateForm()&&phoneflag){//验证输入框和验证码发送
            	$("#submitBtn").attr("disabled",true);
            	var mobileCode = $("#mobileCode").val();//手机验证码
            	if($.trim(mobileCode)==""){
            		$("#zphoneCode").html("请输入验证码");
            	}
            	var mobile = $("#mobile").val();//手机号
            	var password = $("#password").val();//密码
            	$.ajax({//验证输入的手机验证码和短信验证码
    		         type: "post",
    		         url: '${base}/checkValidCode',
    		         data: { 
    		        	 	mobileCode:mobileCode,
    		        	 	mobile:mobile
    		        	   },
    		         dataType: "json",
    				 async:true,
    				 success:function(data) {
    				 	if(data.success == "true"){
    				        $.ajax({//用户注册
    					         type: "post",
    					         url: '${base}/sign',
    					         data: {
    					        	 tab:tab,
    					        	 mobileCode:mobileCode,
    		    		        	 mobile:mobile,
    		    		        	 password:password
    					         },
    					         dataType: "json",
    							 async:true,
    							 success:function(data) {
    							 	if(data.success == "true"){
    							 		 layer.msg(data.message,{icon: 1,time:1000},function(){
    							 			window.location="${base}/login";
    							 		 });
    								  }else{
    			                         $("#submitBtn").removeAttr("disabled");
    			                         $("#zphoneCode").html(data.message);
    								  }
    							  }
    						}); 
    					  }else{
    						$("#zphoneCode").html(data.message);
                            $("#submitBtn").removeAttr("disabled");
    					  }
    				  }
    			});
    	     }
    	}
    	if(tab==1){
    		//邮箱注册
            if(validateForm()&&phoneflag){//验证输入框和验证码发送
            	$("#submitBtn").attr("disabled",true);
            	var emailCode = $("#emailCode").val();//手机验证码
            	if($.trim(emailCode)==""){
            		$("#zemailCode").html("请输入验证码");
            	}
            	var email = $("#email").val();//邮箱
            	var password = $("#password").val();//密码
            	$.ajax({//验证输入的邮箱验证码和短信验证码
    		         type: "post",
    		         url: '${base}/checkEmailCode',
    		         data: { 
    		        	 	 tab:tab,
	    		        	 emailCode:emailCode,
	    		        	 email:email,
	    		        	 password:password
    		        	   },
    		         dataType: "json",
    				 async:true,
    				 success:function(data) {
    				 	if(data.success == "true"){
    				        $.ajax({//用户注册
    					         type: "post",
    					         url: '${base}/sign',
    					         data: {
    					        	 tab:tab,
    					        	 emailCode:emailCode,
    					        	 email:email,
    		    		        	 password:password
    					         },
    					         dataType: "json",
    							 async:true,
    							 success:function(data) {
    							 	if(data.success == "true"){
    							 		 layer.msg(data.message,{icon: 1,time:1000},function(){
    							 			window.location="${base}/login";
    							 		 });
    								  }else{
    			                         $("#submitBtn").removeAttr("disabled");
    			                         $("#zemailCode").html(data.message);
    								  }
    							  }
    						}); 
    					  }else{
    						$("#zemailCode").html(data.message);
                            $("#submitBtn").removeAttr("disabled");
    					  }
    				  }
    			});
    	     }
    	}
   });
});
//验证手机号码
function validateMobile() 
{   
	var flag = true;
    if($.trim($("#mobile").val())=="") 
    { 
       $("#mobileError").html("请输入手机号码");
       flag = false;
       return false; 
    }     
    if($.trim($("#mobile").val()).length!=11) 
    { 
    	$("#mobileError").html("请输入有效的手机号码");
    	flag = false;
        return false; 
    } 
     
    var regmoblie = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
    if(!regmoblie.test($("#mobile").val())) 
    { 
    	$("#mobileError").html("请输入有效的手机号码");
    	flag = false;
        return false; 
    } 
    if(flag){
    	$("#mobileError").empty();
    }
    return true;
}

//验证密码
function validatePass() 
{   
	var flag = true;
    if($.trim($("#password").val())=="") 
    { 
       $("#PassError").html("请输入密码");
       flag = false;
       return false; 
    }     
    if($.trim($("#password").val()).length<6 || $.trim($("#password").val()).length>20) 
    { 
    	$("#PassError").html("密码长度应在6-20个字符之间");
    	flag = false;
        return false; 
    } 
    if(flag){
    	$("#PassError").empty();
    }
    return true;
}

//验证确认密码
function validatePassConfirm() 
{   
	var flag = true;
    if($.trim($("#passwordConfirm").val())=="") 
    { 
       $("#PassError").html("请再次输入密码");
       flag = false;
       return false; 
    }     
    if($.trim($("#passwordConfirm").val()).length<6 || $.trim($("#passwordConfirm").val()).length>20) 
    { 
    	$("#PassError").html("密码长度应在6-20个字符之间");
    	flag = false;
        return false; 
    } 
    if($.trim($("#passwordConfirm").val())!=$("#password").val()) 
    { 
       $("#PassError").html("两次密码输入不一致");
       flag = false;
       return false; 
    }     
    if(flag){
    	$("#PassError").empty();
    }
    return true;
}

//验证邮箱
function validateEmail() { 
	var flag = true;
	if ($.trim($("#email").val()) == "") { 
		$("#emailError").html("邮箱不能为空"); 
		flag = false;
		return false; 
	}                                 
	if (!$.trim($("#email").val()).match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)) { 
		$("#emailError").html("邮箱格式不正确");
		flag = false;
		return false; 
	} 
	if(flag){
    	$("#emailError").empty();
    }
	return true;
} 
function validateForm(){
	var tab = ${tab};
	if(tab==0){
		//手机注册
		if(validateMobile()&&validatePass()&&validatePassConfirm()){
			return true;
		}else{
			return false;
		}
	}
	if(tab==1){
		//邮箱注册
		if(validateEmail()&&validatePass()&&validatePassConfirm()){
			return true;
		}else{
			return false;
		}
	}
}
</script>
</body>
</html>
