<@p.userHeader title="退款详情"/>
<script type="text/javascript" src="${base}/res/js/nc-sideMenu.js" charset="utf-8"></script>
<script type="text/javascript" src="${base}/res/js/utils.js" charset="utf-8"></script>
<!-- 步骤条js -->
<link href="${base}/res/css/font-icons/style.css"  rel="stylesheet" />
<!-- 步骤条样式 -->
<div id="container" class="wrapper">
	<div class="wrap-shadow">
		<div class="wrap-all ncu-order-view"> 
			<#if refundReturn??>
			<#if refundReturn.refundType == 1>
			<h2>退货详情</h2>
			<dl class="box">
				<dt>商家审核：</dt>
				<dd>
	                <#if refundReturn.sellerState==1>
                    	<strong><span style="color:#F30">待审核</span></strong>
	                <#elseif refundReturn.sellerState==2>
	                    <strong><span style="color:#F30">同意</span></strong>
	                <#elseif refundReturn.sellerState==3>
	                	<strong><span style="color:#F30">不同意</span></strong>
	                </#if>
				</dd>
				<dt>平台退款：</dt>
				<dd>
					<#if refundReturn.refundState??>
						<#if refundReturn.refundState==1>
							<strong><span style="color:#36C">未申请</span></strong>
						<#elseif refundReturn.refundState==2>
							<strong><span style="color:#36C">待处理</span></strong>
						<#elseif refundReturn.refundState==3>
							<strong><span style="color:#36C">已完成</span></strong>
						</#if>
					<#else>
						无
					</#if>
				</dd>
				<dt>退款编号：</dt>
				<dd>${refundReturn.refundSn}</dd>
				<dt>申请时间：</dt>
				<dd>${refundReturn.createTimeStr}</dd>
			</dl>
			<h3>退货信息</h3>
			<table class="ncu-table-style">
				<thead>
					<tr>
						<th class="w10"></th>
						<th class="w70"></th>
						<th>商品名称</th>
						<th>商品数量</th>
					</tr>
				</thead>
				<tbody>
					<#if refundReturn.orderGoodsId==0>
					<#list refundReturn.orderGoodsList as orderGoods>
						<tr class="bd-line">
							<td></td>
							<td>
								<div class="goods-pic-small">
									<span class="thumb size60">
										<i></i>
										<a target="_blank" href="${base}/product/detail?id=${orderGoods.goodsId}">
											<img style="display: inline;" src="${imgServer}${orderGoods.goodsImage}" onload="javascript:DrawImage(this,60,60);">
										</a>
									</span>
								</div>
							</td>
							<td>
								<dl class="goods-name">
									<dt>
										<a href="${base}/product/detail?id=${orderGoods.goodsId}" target="_blank">${orderGoods.goodsName}</a>
									</dt>
									<#-- <dd>${orderGoods.specInfo}</dd> -->
								</dl>
							</td>
							<td>${orderGoods.goodsNum}</td>
						</tr>
					</#list>
					<#else>
						<tr class="bd-line">
							<td></td>
							<td>
								<div class="goods-pic-small">
									<span class="thumb size60">
										<i></i>
										<a target="_blank" href="${base}/product/detail?id=${refundReturn.goodsId}">
											<img style="display: inline;" src="${imgServer}${refundReturn.goodsImage}" onload="javascript:DrawImage(this,60,60);">
										</a>
									</span>
								</div>
							</td>
							<td>
								<dl class="goods-name">
									<dt>
										<a href="${base}/product/detail?id=${refundReturn.goodsId}" target="_blank">${refundReturn.goodsName}</a>
									</dt>
									<#-- <dd>${refundReturn.specInfo}</dd> -->
								</dl>
							</td>
							<td>${refundReturn.goodsNum}</td>
						</tr>
					</#if>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="20" class="transportation">
							<dt>退款金额：</dt>
							<dd>
								<b>¥
									<script type="text/javascript">
		              					var amount = number_format(${refundReturn.refundAmount},2);
		              					document.write(amount);
		              				</script>
								</b>
							</dd>
						</td>
					</tr>
				</tfoot>
			</table>
			<ul class="order_detail_list">
				<li>
					退款原因：${refundReturn.buyerMessage}
				</li>
				<li>
					店铺名称：${refundReturn.storeName}
				</li>
				<#if refundReturn.picInfo!=''>
					<li>
						图片信息：
						<#list refundReturn.picInfo?split(',') as img>
				     		<#if img!=''>
				     			<img width="80px" height="80px" src="${imgServer}${img}"/>
				     		</#if>
				     	</#list>
					</li>
				</#if>
				<#if refundReturn.sellerMessage??>
					<li>
						卖家备注：${refundReturn.sellerMessage}
					</li>
				</#if>
				<#if refundReturn.adminMessage??>
					<li>
						管理员备注：${refundReturn.adminMessage}
					</li>
				</#if>
			</ul>
			<h3>操作历史</h3>
			<ul class="log-list">
				<#if refundReturn.returnLogList?size gt 0>
					<#list refundReturn.returnLogList as returnLog>
						<li>
						<span class="operator"> ${returnLog.operator} </span>
						<span class="log-time">${returnLog.createTimeStr}</span>
						<span >${returnLog.stateInfo}</span>
						</li>
					</#list>
				</#if>
			</ul>
			<#else>
				<h2>您查找的记录不存在</h2>
			</#if>
			<#else>
				<h2>您查找的记录不存在</h2>
			</#if>
		</div>
	</div>
</div>
<@p.userfooter/>
<script type="text/javascript">
</script>