<@p.userHeader title="买家首页"/>
<script type="text/javascript" src="${base}/res/js/dialog/dialog.js" id="dialog_js" charset="utf-8"></script> 
<script type="text/javascript" src="${base}/res/js/nc-sideMenu.js" charset="utf-8"></script>
<script type="text/javascript" src="${base}/res/js/utils.js" charset="utf-8"></script>
<script type="text/javascript" src="${base}/res/js/layer/layer.js" charset="utf-8"></script>
<script type="text/javascript" src="${base}/res/js/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<link href="${base}/res/css/font-icons/style.css"  rel="stylesheet" />
<div id="container" class="wrapper">
	<div class="layout">
    	<@u.left  title="我的"/>
    	<div class="right-content">
      		<div class="path">
        		<div>
        			<a href="${base}">我的商城</a><span>&raquo;</span>
                    <a href="#"/>我的订单</a><span>&raquo;</span>
                   	换货申请
                </div>
      		</div>
      		<div class="main">
       			<link rel="stylesheet" type="text/css" href="${base}/res/js/jquery-ui/themes/ui-lightness/jquery.ui.css"  />
				<style type="text/css">
				.store-name {
					width: 130px;
					display: inline-block;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
				}
				</style>
				<div class="wrap">
  					<div class="tabmenu">
    					<ul class="tab pngFix">
  							<li class="normal"><a  href="${base}/user/list">订单列表</a></li>
  							<li class="normal"><a  href="${base}/user/refundList">退款记录</a></li>
  							<li class="normal"><a  href="${base}/user/returnList">退货记录</a></li>
  							<li class="active"><a  href="#">换货记录</a></li>
  						</ul>
  					</div>
  					<form method="post" action="${base}/user/barterList">
    					<table class="search-form">
      						<input type="hidden" name="act" value="member_barter" />
       						<input type="hidden" name="op" value="index" />
      						<tr>
        						<td>&nbsp;</td>
        						<th style="width:115px">
        							<select name="type">
            							<option value="orderSn" <#if type=="orderSn">selected="selected"</#if>>订单编号</option>
            							<option value="barterSn" <#if type=="barterSn">selected="selected"</#if>>换货编号</option>
            							<option value="storeName" <#if type=="storeName">selected="selected"</#if>>店铺名</option>
          							</select>：
          						</th>
        						<td class="w160"><input type="text" class="text" name="key" value="${key}" /></td>
        						<th>换货时间：</th>
        						<td class="w260">
        							<input name="startTime"  type="text" class="txt Wdate" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />&#8211;
          							<input name="endTime" type="text" class="txt Wdate" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
          						</td>
          						<td class="w90 tc"><input type="submit" class="submit" value="搜索" /></td>
      						</tr>
    					</table>
  					</form>
  					<table class="ncu-table-style">
    					<thead>
      						<tr>
        						<th class="w150">订单编号</th>
       							<th class="w150">换货编号</th>
        						<th>店铺</th>
        						<th class="w50">换货数量</th>
        						<th class="w100"><p class="goods-time">换货时间</p></th>
       							<th class="w60">审核状态</th>
       							<th class="w80">物流状态</th>
        						<th class="w90">操作</th>
      						</tr>
    					</thead>
        				<tbody>
        					<#if list??>
        						<#list list as shopbarter>
		            				<tr class="bd-line" >
		        						<td><span style="color: blue">${shopbarter.orderSn}</span></td>
		        						<td class="goods-num">${shopbarter.barterSn}</td>
		        						<td><span style="color: blue">${shopbarter.storeName}</span></td>
		        						<td><strong>${shopbarter.goodsNum}</strong></td>
		        						<td class="goods-time">${shopbarter.createTimeStr?string('yyyy-MM-dd')}</td>
		        						<td>
		        							<#if shopbarter.sellerState==20>
		        								待审核
		        							<#elseif shopbarter.sellerState==30>
		        								同意
		        							<#elseif shopbarter.sellerState==40>
		        								不同意
		        							</#if>
		        						</td>
		        						<td>
		        						   <#if shopbarter.goodsState ??&& shopbarter.goodsState!=null>
			        							<#if shopbarter.goodsState==1>
			        								买家待发货
			        							<#elseif shopbarter.goodsState==2>
			        								待卖家收货
			        							<#elseif shopbarter.goodsState==3>
			        								卖家已收货
			        							<#elseif shopbarter.goodsState==4>
			        								买家待收货
			        							<#elseif shopbarter.goodsState==5>
			        								换货完成
			        							<#else>
			        							            暂无物流信息
			        							</#if>
		        							</#if>
		        						</td>
		        						<td><a href="${base}/user/barterDetail?barterId=${shopbarter.barterId}" target="_blank"> 查看 </a>
		        							<#if shopbarter.sellerState==30&&shopbarter.goodsState==1>
		        								<a href="${base}/user/barterDeliveryIndex?barterId=${shopbarter.barterId}">发货</a>
		        							</#if>
		        							<#if shopbarter.goodsState==4>
		        								<a href="javascript:void(0)" onclick="finishReturn('${shopbarter.barterId}')" >确认收货</a>
		        							</#if>
		        						</td>
		      						</tr>
	      						</#list>
      						</#if>
                		</tbody>
    					<tfoot>
            				<tr>
        						<td colspan="20">
        							<#import "/commons/usertagpage.ftl" as q> <#--引入分页-->
									<#if recordCount??>
									    <@q.pager pageNo=pageNo pageSize=pageSize recordCount=recordCount toURL="${toUrl}"/>
									</#if>
        						</td>
      						</tr>
          				</tfoot>
  					</table>
				</div>
      		</div>
    	</div>
	</div>
</div>
<script>
	/**确认收货**/
	function finishReturn(id) {
	   	layer.open({
		    type: 2,
		    area: ['500px', '240px'],
		    skin: 'layui-layer-rim',
		    title: '确认收货',
		    //content: APP_BASE + '/cart/addresslist'
		    content :  ['${base}/user/finishBarterIndex?barterId=' + id, 'no'],
		    success: function(layero, index){
		    	layer.getChildFrame('#confirm_button',index).on('click', function(){
					var barterId=layer.getChildFrame("#barterId",index).val();
					var fmUrl = '${base}/user/finishBarter';
					$.ajax({
			             type: "post",
			             url: fmUrl,
			             data: {barterId:barterId},
			             dataType: "json",
						 success:function(data) {
							if(data.success){
								parent.layer.alert("确认收货成功",{icon:1},function(){
									location.reload();
								});	
							}else{
								parent.layer.alert("确认收货失败",{icon:2},function(){
									location.reload();
								});		
							}
						}
			         }); 
				});	 
		    }
		});
	}
</script>
<@p.userfooter/>