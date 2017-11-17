<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gold Price Page</title>
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
<script src="js/Chart.bundle.min.js"></script>
<script src="js/Chart.min.js"></script>
<script src="js/DateUtils.js"></script>
<script src="js/gold-price.js"></script>
</head>
<body>
	<h1>Let's get start, go! go! go!</h1>
	<div style="width: 75% ; margin-bottom: 30px">
		<a href="?page=price_stat">价格统计页面</a>
	</div>
	<div style="width: 75% ">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="button" class="dayRange" value="最近一天" data_range="1"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近一周" data_range="7"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近一月" data_range="30"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近三个月" data_range="90"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div style="width: 75%; align-content: center">
		<canvas id="price_line" width="800" height="400"></canvas>
	</div>
</body>
</html>
<script language="javascript">
	var data = {
		labels : [ "start" ],
		datasets : [ {
			fillColor : "rgba(220,220,220,0.5)",
			strokeColor : "rgba(220,220,220,1)",
			pointColor : "rgba(220,220,220,1)",
			pointStrokeColor : "#fff",
			data : [ 1 ],
			label : 'gold price'
		} ]
	};
	window.onload = function() {
		var ctx = document.getElementById("price_line").getContext("2d");
		price_line_config['data'] = data;
		window.myLine = new Chart(ctx, price_line_config);
		renderPrice(1);
	};

	$('.dayRange').click(function() {
		renderPrice($(this).attr('data_range'));
	});

	function renderPrice(days) {
		var now = new Date();
		var end = now.Format('yyyy-MM-dd HH:mm:ss');
		now.setDate(now.getDate() - days);
		var start = now.Format('yyyy-MM-dd HH:mm:ss');
		$.ajax({
			url : 'pricedata',
			dataType : 'json',
			data : {
				start : start,
				end : end
			},
			success : function(result) {
				console.log(result);
				data.labels = result.labels;
				data.datasets[0].data = result.values;
				window.myLine.update();
			}
		});
	}
</script>