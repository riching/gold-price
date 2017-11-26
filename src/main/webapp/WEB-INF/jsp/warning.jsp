<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gold Price Page</title>
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
</head>
<body>
	<h1>Let's get start, go! go! go!</h1>
	<div style="width: 75% ; margin: 50px">
		<h1>添加警告条件</h1>
		<div style="margin: 30px">
			<form action="add">
				<span>开始时间（小时）</span><input type="text" name="startHour"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>截止时间（小时）</span><input type="text" name="endHour"/><br/>
				<span>比值类型</span>
				<select name="valueType">
					<option value="1">最大值</option>
					<option value="2">最小值</option>
					<option value="3">均值</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>操作符</span>
				<select name="operator">
					<option value="1">大于</option>
					<option value="2">小于</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span>差值</span><input type="text" name="differ"/><br/>
				<input type="submit" value="添加" />
			</form>
		</div>
	</div>
	<div style="width: 75% ; margin: 50px">
		<form action="del">
			<table border="1">
				<tr>
					<th>选择</th>
					<th>ID</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>比值类型</th>
					<th>操作符</th>
					<th>差值</th>
				</tr>
				<c:forEach var="con" items="${list}">
					<tr>
						<td><input type="checkbox" name="ids" value="${con.id }"/></td>
						<td>${con.id }</td>
						<td>${con.startHour }</td>
						<td>${con.endHour }</td>
						<td>${con.valueType == 1 ? '最大值' : '' }${con.valueType == 2 ? '最小值' : '' }${con.valueType == 3 ? '均值' : '' }</td>
						<td>${con.operator == 1 ? '大于' : '' }${con.operator == 2 ? '小于' : '' }</td>
						<td>${con.differ }</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="7"><input type="submit" value="删除" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>