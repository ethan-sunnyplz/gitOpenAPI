<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Open API 예제</title>
<style>
.table 		{border-collapse:collapse; border-spacing:0;border-top:1px solid #cbd0d5;border-bottom:1px solid #cbd0d5; }
.table td	{padding:4px;border-left:1px solid #cbd0d5; border-right:1px solid #cbd0d5;border-bottom:1px solid #cbd0d5;}
</style>
<script>
function move(name){
	if(name == "json"){
		location.href="/openapi/jsonParse";
	} else {
		location.href="/openapi/xmlParse";
	}
}
</script>
</head>
<body>
	<table class="table" align="center">
		<tr>
			<td>
				<input type="button" value="JSON 예제" style="border: 0; width:100px" onclick="move('json')"/>
			</td>
			<td>
				<input type="button" value="XML 예제" style="border: 0; width:100px" onclick="move('xml')"/>
			</td>
		</tr>
	</table>
</body>
</html>