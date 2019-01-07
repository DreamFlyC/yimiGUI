<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
	java.util.Date date = new java.util.Date();
	DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String Fordate = "";
	String Todate = "";
	try {
		Fordate = request.getParameter("Fordate").trim();
		Todate = request.getParameter("Todate").trim();
	} catch (Exception ignored) {
	}
	if (Fordate.equals("") || Todate.equals("")) {
		pageContext.setAttribute("Fordate", formatter.format(date));
		pageContext.setAttribute("Todate", formatter.format(date));
	} else {
		pageContext.setAttribute("Fordate", Fordate);
		pageContext.setAttribute("Todate", Todate);
	}
	try {
		pageContext.setAttribute("lotteryid", request.getParameter("lotteryid").trim());
	} catch (Exception ignored) {
	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8;"/>
	<%@ include file="../tag.jsp" %>
	<title>${appName}_测试管理</title>
</head>
<body>
<%@ include file="../top.jsp" %>
<%@ include file="../left.jsp" %>
<div class="place">
	<span>位置：</span>
	<ul class="placeul">
		<li><a href="<%=basePath%>sys/index.html">首页</a></li>
		<li><a href="<%=basePath%>manage/aabbcc.html">测试列表</a></li>
		<li><a href="<%=basePath%>manage/aabbcc/post.html">修改测试</a></li>
	</ul>
</div>
<div class="rightContainer">
	<script src="<%=basePath%>resources/My97DatePicker/WdatePicker.js"></script>

	<form class="form-horizontal" role="form" method="post" action="<%=basePath%>manage/aabbcc/edit.html"
		  id="userForm">
		<input type="hidden" name="id" value="${crmPurchacsePushLog.id}" />
		<div class="list-group-item active glyphicon glyphicon-chevron-up" data-toggle="collapse"
			 data-parent="#accordion" href="#panelInfo">修改测试
		</div>
		<div id="panelInfo" class="panel-collapse collapse in">
			<div class="panel-body">
				<table class="table table-bordered">
				<!-- begin -->
<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>id</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="id" value="${basSku.id}" placeholder="请输入id" nullmsg="请输入id" errormsg="id至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>产品代码</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="sku" value="${basSku.sku}" placeholder="请输入产品代码" nullmsg="请输入产品代码" errormsg="产品代码至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>中文描述</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="descrC" value="${basSku.descrC}" placeholder="请输入中文描述" nullmsg="请输入中文描述" errormsg="中文描述至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>英文描述</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="descrE" value="${basSku.descrE}" placeholder="请输入英文描述" nullmsg="请输入英文描述" errormsg="英文描述至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>包装描述</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="packDescr" value="${basSku.packDescr}" placeholder="请输入包装描述" nullmsg="请输入包装描述" errormsg="包装描述至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>存储环境(常温、冷冻、冷藏)</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="skuGroup3" value="${basSku.skuGroup3}" placeholder="请输入存储环境(常温、冷冻、冷藏)" nullmsg="请输入存储环境(常温、冷冻、冷藏)" errormsg="存储环境(常温、冷冻、冷藏)至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>收货单位</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="receivingUom" value="${basSku.receivingUom}" placeholder="请输入收货单位" nullmsg="请输入收货单位" errormsg="收货单位至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>发货单位</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="shipmentUom" value="${basSku.shipmentUom}" placeholder="请输入发货单位" nullmsg="请输入发货单位" errormsg="发货单位至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>库存下限</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="qtyMin" value="${basSku.qtyMin}" placeholder="请输入库存下限" nullmsg="请输入库存下限" errormsg="库存下限至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>库存上限</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="qtyMax" value="${basSku.qtyMax}" placeholder="请输入库存上限" nullmsg="请输入库存上限" errormsg="库存上限至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>货物类型</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="freightClass" value="${basSku.freightClass}" placeholder="请输入货物类型" nullmsg="请输入货物类型" errormsg="货物类型至少1个字符,最多50个字符！" />
</td>
</tr>

<tr>
<td class="info col-md-1 text-right"><span class="red">*</span>有效期</td>
<td class="col-md-11">
<input type="text" class="form-control" maxlength="50" datatype="*1-50"
name="shelfLife" value="${basSku.shelfLife}" placeholder="请输入有效期" nullmsg="请输入有效期" errormsg="有效期至少1个字符,最多50个字符！" />
</td>
</tr>
<!-- end -->
				</table>
				<div class="col-md-12 text-center">
					<button type="submit" class="btn btn-primary btn-lg">保 存</button>
				</div>
			</div>
		</div>
	</form>
</div>
<div class="cls"></div>
<script type="text/javascript">
	$("#page_aabbcc").parent().attr("class", "active");
	$(function () {
		LW.form.validate("userForm");
	});
	$("input[name='pushStatus'][value='${crmPurchacsePushLog.pushStatus}']").attr("checked",true);
</script>
<%@ include file="../foot.jsp" %>
</body>
</html>
