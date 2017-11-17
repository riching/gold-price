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
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="button" class="dayRange" value="最近一天" data_range="1"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近一周" data_range="7"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近一月" data_range="30"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="dayRange" value="最近三个月" data_range="90"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div style="width: 75%">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="radio" name="hourRange" start="0" end="24" checked="checked"/>0-24&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" name="hourRange" start="7" end="16" />7-16&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" name="hourRange" start="16" end="23" />16-23&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" name="hourRange" start="23" end="7" />23-7&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div style="width: 75%; align-content: center">
		<canvas id="price_stat" width="800" height="400"></canvas>
	</div>
</body>
</html>
<script language="javascript">
	var data = {
		labels : [ "start" ],
		datasets : [ {
			fill:false,
			backgroundColor: window.chartColors.red,
			borderColor: window.chartColors.red,
			data : [ 1 ],
			label : 'max price'
		}, {
			fill:false,
			backgroundColor: window.chartColors.green,
			borderColor: window.chartColors.green,
			data : [ 1 ],
			label : 'min price'
		}, {
			fill:false,
			backgroundColor: window.chartColors.blue,
			borderColor: window.chartColors.blue,
			data : [ 1 ],
			label : 'avg price'
		} ]
	};
	window.onload = function() {
		var ctx = document.getElementById("price_stat").getContext("2d");
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
		
		hourRange = $('input:radio[name="hourRange"]:checked');
		
		$.ajax({
			url : 'pricestat',
			dataType : 'json',
			data : {
				start : start,
				end : end,
				startHour: hourRange.attr('start'),
				endHour: hourRange.attr('end')
			},
			success : function(result) {
				console.log(result);
				data.labels = result.labels;
				data.datasets[0].data = result.maxPrices;
				data.datasets[1].data = result.minPrices;
				data.datasets[2].data = result.avgPrices;
				window.myLine.update();
			}
		});
	}
</script>