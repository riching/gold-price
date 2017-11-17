var price_line_config = {
		type : 'line',
		options : {
			responsive : true,
			title : {
				display : true,
				text : '黄金价格曲线'
			},
			tooltips : {
				mode : 'index',
				intersect : false,
			},
			hover : {
				mode : 'nearest',
				intersect : true
			},
			scales : {
				xAxes : [ {
					display : true,
					scaleLabel : {
						display : true,
						labelString : '日期'
					}
				} ],
				yAxes : [ {
					display : true,
					scaleLabel : {
						display : true,
						labelString : '价格'
					}
				} ]
			}
		}
	};
window.chartColors = {
		red: 'rgb(255, 99, 132)',
		orange: 'rgb(255, 159, 64)',
		yellow: 'rgb(255, 205, 86)',
		green: 'rgb(75, 192, 192)',
		blue: 'rgb(54, 162, 235)',
		purple: 'rgb(153, 102, 255)',
		grey: 'rgb(201, 203, 207)'
	};