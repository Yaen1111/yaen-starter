<div>
${tableName}
<ul>
			<#list columns as column>
				<li>${column.myField} -- ${column.myType}</li>
		</#list>
		</ul>
</div>