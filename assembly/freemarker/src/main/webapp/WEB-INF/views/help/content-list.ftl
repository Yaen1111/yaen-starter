	<ul class="nch-sidebar-article-class">
				<#assign articleClassTag = newTag("articleClassTag")/>
				<#assign listTag = articleClassTag("{'tagDataType':'1'}") />
				<#if listTag??>
					<#list listTag as articleClass >
                        <li><a href="${base}${articleClass.realUrl}">${articleClass.acName}</a></li>
					</#list>
				</#if>
				</ul>