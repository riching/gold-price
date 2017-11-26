<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="me.riching.goldprice.service.GoldPriceCrawlerService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		out.println("test");
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		GoldPriceCrawlerService priceService = context.getBean(GoldPriceCrawlerService.class);
		out.println(priceService);
		double price = 370;
		for (int i = 0; i < 10; i++) {
			priceService.saveAndAnalysisPrice(price + i * 0.1d);
		}
	%>
</body>
</html>