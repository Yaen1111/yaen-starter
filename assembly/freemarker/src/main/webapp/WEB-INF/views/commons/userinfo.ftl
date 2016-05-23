 <@shiro.user>
        <script type="text/javascript">
        	isLogin = true;
        </script>
            您好
            <span>&nbsp;
            	<a href="${base}/user/index"><@shiro.principal/></a>
            </span>，欢迎来到
           <a href="${base}/"  title="首页" alt="首页">
	            <#if siteSet?? && siteSet!='null'>
	            	${siteSet.siteName}
	            <#else>
	            	qdmall商城系统演示站
	            </#if>
            </a>
            <span>[<a href="${base}/logout">退出</a>]</span>
        </@shiro.user>
        <@shiro.guest>
        	<script type="text/javascript">
	        	isLogin = false;
	        </script>
            您好，欢迎来到
            <a href="${base}/" title="首页" alt="首页">
             	<#if siteSet?? && siteSet!='null'>
	            	${siteSet.siteName}
	            <#else>
	            	qdmall商城系统演示站
	            </#if>
            </a>
            <span>[<a href="${base}/login">登录</a>]</span>
            <span>[<a href="${base}/signUp">注册</a>]</span>
        </@shiro.guest>
 	<span class="seller-login">
				<a href="${sellerServer}/" title="商家中心" target="_blank">
					<i class="icon-signin"></i>商家管理中心
				</a>
			</span>