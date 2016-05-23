<@p.userHeader title="换货详情"/>
<script type="text/javascript" src="${base}/res/js/nc-sideMenu.js" charset="utf-8"></script>
<script type="text/javascript" src="${base}/res/js/utils.js" charset="utf-8"></script>
<!-- 步骤条js -->
<link href="${base}/res/css/font-icons/style.css"  rel="stylesheet" />
<!-- 步骤条样式 -->
<div id="container" class="wrapper">
	<div class="wrap-shadow">
		<div class="wrap-all ncu-order-view"> 
			<h2>换货详情</h2>
			<dl class="box">
				<dt>审核状态：</dt>
				<dd>
	                <#if barterDetail.sellerState==10>
                    	<strong><span style="color:#F30">待审核</span></strong>
	                <#elseif barterDetail.sellerState==20>
	                    <strong><span style="color:#F30">同意</span></strong>
	                <#elseif barterDetail.sellerState==30>
	                	<strong><span style="color:#F30">不同意</span></strong>
	                </#if>
				</dd>
				<dt>换货编号：</dt>
				<dd>${barterDetail.barterSn}</dd>
				<dt>申请时间：</dt>
				<dd>${barterDetail.createTimeStr}</dd>
			</dl>
			<h3>换货信息</h3>
			<table class="ncu-table-style">
				<thead>
					<tr>
						<th class="w10"></th>
						<th class="w70"></th>
						<th>商品名称</th>
						<th>换货数量</th>
					</tr>
				</thead>
				<tbody>
					<tr class="bd-line">
						<td></td>
						<td>
							<div class="goods-pic-small">
								<span class="thumb size60">
									<i></i>
									<a target="_blank" href="${base}/product/detail?id=${barterDetail.goodsId}">
										<img style="display: inline;" src="${imgServer}${barterDetail.goodsImage}" onload="javascript:DrawImage(this,60,60);">
									</a>
								</span>
							</div>
						</td>
						<td>
							<dl class="goods-name">
								<dt>
									<a href="${base}/product/detail?id=${barterDetail.goodsId}" target="_blank">${barterDetail.goodsName}</a>
								</dt>
								<#-- <dd>${barterDetail.specInfo}</dd> -->
							</dl>
						</td>
						<td>${barterDetail.goodsNum}</td>
					</tr>
				</tbody>
			</table>
			<ul class="order_detail_list">
				<li>
					换货原因：${barterDetail.buyerMessage}
				</li>
				<li>
					店铺名称：${barterDetail.storeName}
				</li>
				<#if barterDetail.picInfo!=''>
					<li>
						图片信息：
						<#list barterDetail.picInfo?split(',') as img>
				     		<#if img!=''>
				     			<img width="80px" height="80px" src="${imgServer}${img}"/>
				     		</#if>
				     	</#list>
					</li>
				</#if>
				<#if barterDetail.sellerMessage??>
					<li>
						卖家备注：${barterDetail.sellerMessage}
					</li>
				</#if>
				<#if barterDetail.adminMessage??>
					<li>
						管理员备注：${barterDetail.adminMessage}
					</li>
				</#if>
			</ul>
			<h3>发货信息</h3>
			<#if barterDetail.orderDaddress??>
				<dl class="logistics">
					<dt>发&nbsp; 货 &nbsp;人：</dt>
					<dd>${barterDetail.orderDaddress.sellerName}&nbsp;</dd>
					<dt>联系电话：</dt>
					<dd>${barterDetail.orderDaddress.mobPhone}&nbsp;</dd>
					<dt>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编：</dt>
					<dd>${barterDetail.orderDaddress.zipCode}&nbsp;</dd>
					<dt class="cb">发货地址：</dt>
					<dd style="width: 90%;">${barterDetail.orderDaddress.areaInfo}&nbsp;${barterDetail.orderDaddress.address}</dd>
					<#if barterDetail.expressName??>
						<dt>配送公司：</dt>
						<dd>${barterDetail.expressName}&nbsp;</dd>
					</#if>
					<#if barterDetail.invoiceNo??>
						<dt>物流单号：</dt>
						<dd>${barterDetail.invoiceNo}&nbsp;</dd>
					</#if>
				</dl>
			</#if>
			<#if barterDetail.buyerInvoiceNo ??&& barterDetail.buyerInvoiceNo!=null>
				<h3>卖家物流信息</h3>
				<dl class="logistics">
					<dt>发&nbsp; 货 &nbsp;人：</dt>
					<dd>${barterDetail.storeName}&nbsp;</dd>
					<dt>配送公司：</dt>
					<dd>${barterDetail.sellerExpressName}&nbsp;</dd>
					<dt>物流单号：</dt>
					<dd>${barterDetail.sellerInvoiceNo}&nbsp;</dd>
				</dl>
			</#if>
			<h3>操作历史</h3>
			<ul class="log-list">
				<#if barterDetail.shopBarterLogList?? && barterDetail.shopBarterLogList?size gt 0>
					<#list barterDetail.shopBarterLogList as shopbarterlog>
						<li>
						<span class="operator"> ${shopbarterlog.operator} </span>
						<span class="log-time">${shopbarterlog.createTimeStr}</span>
						<span >${shopbarterlog.stateInfo}</span>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
	</div>
</div>
<@p.userfooter/>
<script type="text/javascript">
</script>