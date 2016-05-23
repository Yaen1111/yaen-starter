<ul class="nch-sidebar-article-list">
    <#assign articleTag = newTag("articleTag") />
	<#assign articleList = articleTag("{'pageNo':'${pageNo}','pageSize':'10','tagDataType':'2','articleShow':'1'}") />
		<#if articleList??>
			<#list articleList as articles>
				<li>
				<i class="icon-dot"></i>
				<#if articles.articleUrl == "">
                    <a href="${base}${articles.realUrl}" >${articles.articleTitle }</a>
			   <#else>
			  <a href="${articles.articleUrl}" title="${articles.articleTitle}" target="_blank">${articles.articleTitle}</a>
				</#if>				
			</li>
		</#list>
	</#if>
</ul>