<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="EUC-KR"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URL" %>
<%
	List list = (List) request.getAttribute("list");

	URL iframe = (URL) request.getAttribute("iframe");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Open API 예제</title>
<style>
.style1 {
	margin: 0;
	padding: 0;
	line-height: 21px;
	border-top: 2px solid #D0D0CE;
	border-bottom: 2px solid #D0D0CE;
	border-collapse: collapse;
	font-family: "굴림체";
	font-size: 11pt;
	color: #333333;
	background-color:#FDFEF7;
	text-align:center;
	font-weight: bold;
}
.bg_white_c {
	FONT-FAMILY: TAHOMA, Dotum;
	font-size: 13px;
	color: #444444;
	padding-top: 3px;
	line-height: 21px;
	text-align:center;
	background-color:#FFFFFF;
}
.table02 th,table.table02 td {
	margin: 0;
	padding: 3px 0;
	/* text-align: center; */
	border: 1px solid #EAEAEA
}
.table02 td {
	padding-left: 5px;
	padding-right: 5px;
	height:50px
}
.page {border:0; cursor: pointer;}
input {cursor: pointer;}


</style>

<script>
function search(val){
	location.href="/openapi/jsonParse?page="+val;
}

function prev(){
	location.href="/openapi";
}
</script>

</head>
<body>
	<table width="1024" cellspacing="1" cellpadding="0" style="border-collapse:collapse;" align="center">
		<tr>
			<td width="50%" align="center">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table02">
					<tr>
						<td width="15%" class="style1">명칭</td>
						<td width="30%" class="style1">약국소재지(도로명)</td>
						<td width="10%" class="style1">약국우편번호</td>
						<td width="30%" class="style1">주소</td>
						<td width="15%" class="style1">연락처</td>
					</tr>
					<%
					if (list != null && list.size() > 0) {
						for (int i=0; i<list.size(); i++) {
							Map map = (Map)list.get(i);
					%>
					<tr>
						<td class="bg_white_c" style="font-weight:bold; "><%=map.get("name") %></td>
						<td class="bg_white_c"><%=map.get("streetName") %></td>
						<td class="bg_white_c"><%=map.get("zipCode") %></td>
						<td class="bg_white_c"><%=map.get("address") %></td>
						<td class="bg_white_c"><%=map.get("telNum") %></td>
					</tr>
					<%
						}
					}
					%>
				</table>
				<table>
					<tr height="100px">
						<td>
							<input class="page" type="button" size="1" value="1" onclick="search('1')"/>
							<input class="page" type="button" size="1" value="2" onclick="search('11')"/>
							<input class="page" type="button" size="1" value="3" onclick="search('21')"/>
							<input class="page" type="button" size="1" value="4" onclick="search('31')"/>
							<input class="page" type="button" size="1" value="5" onclick="search('41')"/>
							<input class="page" type="button" size="1" value="6" onclick="search('51')"/>
							<input class="page" type="button" size="1" value="7" onclick="search('61')"/>
							<input class="page" type="button" size="1" value="8" onclick="search('71')"/>
							<input class="page" type="button" size="1" value="9" onclick="search('81')"/>
							<input class="page" type="button" size="1" value="10" onclick="search('91')"/>
						</td>
					</tr>
					
				</table>
				<table align="right">
					<tr>
						<td>
							<input type="button" size="10" value="이전" onclick="prev()"/>
						</td>
					</tr>
				</table>
				<table>
					<tr height="100px">
						<td>
							<iframe src ="<%=iframe%>" width="1024" height="100px"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>