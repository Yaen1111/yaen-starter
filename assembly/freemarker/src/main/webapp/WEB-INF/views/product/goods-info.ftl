	          	<div class="nc-spec">
					<#if goodsSpecObj != null>
		            <#assign nameMap = goodsSpecObj.get("specname")>
		            <#assign valueMap = goodsSpecObj.get("specvalue")>
		            <#assign goodsSpecs = goodsSpecObj.get("goodsSpecs")>
		            <#assign goodsColImg = goodsSpecObj.get("goodsColImg")>
		            
			            <#if nameMap??>
				            <#list nameMap.keySet() as key>
					            <dl>
						            <dt>${nameMap.get(key)}：</dt>
						            <dd>
							            <ul nctyle="ul_sign">
								            <!-- 文字类型规格-->
								            <#if valueMap??>
								            	<#list valueMap[key] as goodsSpecMapValues>
									            	<li class="sp-txt">
											            	<a href="javascript:void(0)" spId="${key}" spValueId="${goodsSpecMapValues.spValueId}" onClick="selectSpec('${goodsSpecMapValues.spId}', this, '${goodsSpecMapValues.spValueId}')" class="">
											            	${goodsSpecMapValues.spValueName}
											            		<#if key==1>
											            			<#if goodsColImg?? && goodsColImg!''>
											            				<#list goodsColImg?keys as goodsColImgKey>
											            					<#if goodsSpecMapValues.spValueId==goodsColImgKey>
											            						<img alt="" src="${imgServer}${goodsColImg[goodsColImgKey]}" style="height: 25px;width: 25px;">
											            					</#if>
											            				</#list>
											            			</#if>
											            		</#if>
											            	<i></i>
										            	   </a>
									            	</li>
									        	 </#list>
			            					</#if>
							            </ul>
						            </dd>
					            </dl>
					        </#list>
			            </#if>
		            </#if>
		            <!-- E 商品规格值--> 
		            </div>
		            <!-- S 购买数量及库存 -->
		            <dl>
		           	<dt>购买数量：</dt>
					<dd class="nc-figure-input">
						<a href="javascript:void(0)" onclick="caculate('cut');">
							<i class="icon-minus decrease fl text-hidden"></i>
						</a>
						<input type="text" name="" id="quantity" value="1" size="3" maxlength="6" class="fl" style="border-radius:0;">
						<a href="javascript:void(0)" onclick="caculate('add');">
							<i class="icon-plus increase fl text-hidden"></i>
						</a> 
		                <em class="fl ml20">
			                (当前库存<strong nctype="goods_stock" id="stockGoods">0</strong>件)
		                </em> 
					</dd>
					<div class="clear"></div>
		            </dl>
		            <!-- E 购买数量及库存 --> 
		            <!-- S 提示已选规格及库存不足无法购买 -->
		            <dl class="nc-point" nctype="goods_prompt" style="display:none;"></dl>
		            <!-- E 提示已选规格及库存不足无法购买 -->
					<div class="clear"></div>
		            <!-- S 购买按钮 -->
		            <div class="nc-btn clearfix">
						<a href="javascript:buynow('');" class="buynow fl text-hidden" title="立即购买">立即购买</a><!-- 立即购买-->
						<a href="javascript:buy('');" class="addcart fl ml10 text-hidden" title="添加到购物车">添加到购物车<!-- 加入购物车--></a> 
		              	<!-- S 加入购物车弹出提示框 -->
		              	<div class="ncs_cart_popup">
		                	<dl>
		                  		<dt>
				                    <h3>成功添加到购物车</h3>
				                    <a title="关闭" onClick="$('.ncs_cart_popup').css({'display':'none'});">关闭</a>
								</dt>
		                  		<dd>
		                    		<p class="mb5">购物车共有 <strong id="bold_num"></strong> 种商品 总金额为：<em id="bold_mly" class="price"></em></p>
				                    <p>
				                      <input type="submit" class="btn1" name="" value="查看购物车" onClick="javascript:void(0);"/>
				                      <input type="submit" class="btn2" name="" value="继续购物" onClick="$('.ncs_cart_popup').css({'display':'none'});"/>
				                    </p>
		                  		</dd>
		                	</dl>
						</div>
		              	<!-- E 加入购物车弹出提示框 -->
					</div>
					<!-- E 购买按钮 -->
					<div class="clear"></div>
<script type="text/javascript">
/* 商品购买数量增减js */
	// 增加
	$('.increase').click(function(){
		num = parseInt($('#quantity').val());
		max = parseInt($('[nctype="goods_stock"]').text());
		if(num < max){
			$('#quantity').val(num+1);
		}
	});
	//减少
	$('.decrease').click(function(){
		num = parseInt($('#quantity').val());
		if(num > 1){
			$('#quantity').val(num-1);
		}
	});
$(function(){
		var tatolStorage = 0;
		//alert(specQty);
		//计算总库存
		if(specQty==1){
			//alert(specs[0].stock);
			tatolStorage = specs[0].stock;
		}else{
			 for(var i = 0; i < specQty; i++){
				//alert("spec:"+specs[i]);
				//alert("stock:"+specs[i].stock);
				tatolStorage += parseInt(specs[i].stock);
			}
		}
		
		$("[nctype=goods_stock]").html(tatolStorage);
		});
</script>