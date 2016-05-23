<@p.header title="LmShop B2B2C V3.0 2015版商城系统-内容"/>
<div class="clear"></div>
<!--主页内容-->
<#assign articleTag = newTag("articleTag") />
<#assign article = articleTag("{'articleId':'${articleId}'}") />

<#assign articleClassTag = newTag("articleClassTag")/>
<#assign articleClass = articleClassTag("{'acId':'${article.acId}'}") />
<div class="nch-breadcrumb-layout">
	<div class="nch-breadcrumb wrapper">
		<i class="icon-home"> </i> <span><a href="${base }">首页</a></span>
		<span class="arrow">&gt;</span>
        <span><#if articleClass??><a href="${base}${articleClass.realUrl}">${articleClass.acName}</a></#if></span>
		<span class="arrow">&gt;</span><span>文章内容</span>
	</div>
</div>
<link href="${base }/res/css/layout.css"
	rel="stylesheet" type="text/css">
<!---文章内容--->
<div class="nch-container wrapper">
	<!---左侧--->
	<div class="left">
		<div class="nch-module nch-module-style01">
			<div class="title">
				<h3>文章分类</h3>
			</div>
			<div class="content" id="content-type">
			</div>
		</div>
		
		<div class="nch-module nch-module-style03">
			<div class="title">
				<h3>最新文章</h3>
			</div>
			<div class="content" id="new-article">
			</div>
		</div>
	</div>
	<!---右侧--->
	<div class="right">
		<div class="nch-article-con">
		
			<#if article?? && article!'' >
				<h1>${article.articleTitle!''}</h1>
				<h2>${article.articleTime!''}</h2>
				<div class="default">
					<div>
					${article.articleContent!''}
					</div>
				</div>
			</#if>
			<!-- <div class="more_article">
				<span class="fl">上一篇: <a
					href="#?act=article&article_id=8">忘记密码</a> <span>2011-01-11
						09:28</span>
				</span> <span class="fr">下一篇: <a
					href="#?act=article&article_id=6">如何注册成为会员</a> <span>2011-01-11
						09:25</span>
				</span>
			</div> -->
		</div>
	</div>
</div>
<div class="clear"></div>
<script type="text/javascript">
    $(document).ready(function() {
        $("#new-article").load("${base}/help/news");
        $("#content-type").load("${base}/help/articletype");
    });
</script>
<!-----footer------>
<@p.footer />
