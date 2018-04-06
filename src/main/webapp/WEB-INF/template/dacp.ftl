<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<style>
	.dataList {
	    border-collapse: collapse;
	    font-size: 12px;
	    width: 100%;
	    line-height: 24px;
	}
	.writeLine {
	    background-color: #ffffff;
	    border: 1px solid #b7dcee;
	}
	.blueLine {
	    background-color: #e3f4fa;
	    border: 1px solid #b7dcee;
	}
	table {
		display: table;
	 	border-collapse: separate;
	 	border-spacing: 2px;
	 	border-color: grey;
	}
	tbody {
	    display: table-row-group;
	    vertical-align: middle;
	    border-color: inherit;
	}
	tr {
	    display: table-row;
	    vertical-align: inherit;
	    border-color: inherit;
	}
	td, th {
	    display: table-cell;
	    vertical-align: inherit;
	}
	th {
	    font-weight: bold;
	    background: #e3f4fa;
	    padding: 3px 5px;
	    border-right: 1px solid #b7dcee;
	    border-bottom: 1px solid #b7dcee;
	}
	td {
		border-right: 1px solid #b7dcee;
	    border-bottom: 1px solid #b7dcee;
	    padding: 3px 5px;
	    text-align: left;
	    white-space: nowrap;
	}
	.divBody {
	    width: 1020px;
	    margin: auto;
	    padding-bottom: 10px;
	}
</style>
<body  class="divBody" style="width: 90%; margin-top: 50px;">
<table class="dataList" id="baodao" align="left">
	<tr>
		<th>相关报道</th>
	</tr>
	<tr>
		<td>维权群：539685520</td>
	</tr>
	<tr>
		<td><a href="http://mp.weixin.qq.com/s?__biz=MzA4MjU5Mjg5NA==&mid=2651335768&idx=1&sn=4ab1940373bdf9f6ed25b4a6a9f5b8c7&chksm=847fd949b308505ffee135efdbde9d700c51969e080cd9675a5f2bdeb914d197293623816263&mpshare=1&scene=23&srcid=0325PcKsmEOklxCmGZ7sA4IM#rd">蒙迪欧是如何从畅销车变成滞销车的？</a></td>
	</tr>
</table>
<hr/>
<table class="dataList" id="tdRepeater" align="left"> 
	<tbody>
  		<tr class="trHead"> 
  			<th width="30%">网站</th>
  			<th>备注</th>
  		</tr>
  		<tr>
  			<td><a href="http://www.dpac.gov.cn/cpqxcj/VehicleComplaintInfo.htm" target="_blank">国家质检总局</a></td>
  			<td>需要发动机号，提前准备好行驶证。<span style="color:red">每次选择不同的总成，不然多次投诉只算一条</span></td>
  		</tr>
  		<tr>
  			<td><a href="http://www.qichemen.com/hyhm.aspx?tid=4" target="_blank">汽车门</a></td>
  			<td><a href="http://www.qichemen.com/hyhm.aspx?tid=4" target="_blank">www.qichemen.com</a></td>
  		</tr>
  		<tr>
  			<td><a href="http://www.12365auto.com/ " target="_blank">车质网</a></td>
  			<td>手机APP投诉更方便，这个网站比较负责，会反馈到厂家</td>
  		</tr>
  		<tr>
  			<td><a href="http://tousu.315che.com/tousulist/serial/556/" target="_blank">中国汽车消费网</a></td>
  			<td><a href="http://tousu.315che.com/tousulist/serial/556/" target="_blank">tousu.315che.com</a></td>
  		</tr>
  		<tr>
  			<td>新闻调查信箱</td>
  			<td>xinwendiaocha@vip.sina.com</td>
  		</tr>
  		<tr>
  			<td>长安福特投诉电话</td>
  			<td>400-887-7766</td>
  		</tr>
  		<tr>
  			<td>福特中国区客户关系中心(CRC Customer Relationship Center)</td>
  			<td>800-810-0740</td>
  		</tr>
  		<tr>
  			<td>质量技术监督局</td>
  			<td>023-12365</td>
  		</tr>
  		<tr>
  			<td><a href="http://zmhd.miit.gov.cn/consult/index1.jsp" target="_blank">工信部部长邮箱</a></td>
  			<td>近期集中投诉重点</td>
  		</tr>
  		<tr>
  			<td>长安福特投诉邮箱</td>
  			<td>
  				customerviewpoint@customerviewpointreply.com<br/>
				gam@ford.com<br/>
				cjohns12@ford.com<br/>
				lyang102@ford.com<br/>
				mhe5@ford.com<br/>
				pfinger@ford.com<br/>
				pli5@ford.com<br/>
				plu1@ford.com<br/>
				schang5@ford.com<br/>
				swake@ford.com<br/>
				expcac@ford.com<br/>
				xzhan132@ford.com<br/>
				ylan1@ford.com<br/>
				ymao5@ford.com<br/>
				zche@ford.com<br/>
				zhang24@ford.com<br/>
  			</td>
  		</tr>
  	</tbody>
</table>
<hr>
<h3>国家质检总局投诉最新列表：更新日期：${date}</h3>
<table class="dataList" id="tdRepeater"> 
	<tbody>
	<tr class="trHead"> 
	 <th> 报告人 </th> 
	 <th> 生产者名称 </th> 
	 <th> 品牌 </th> 
	 <th> 车型系列 </th> 
	 <th> 车型名称 </th> 
	 <th> 所在总成 </th> 
	 <th> 入库时间 </th> 
	</tr>
 	<#list lines as line>
 		${line?string}
 	</#list>
 	</tbody>
</table>
	</body>
</html>