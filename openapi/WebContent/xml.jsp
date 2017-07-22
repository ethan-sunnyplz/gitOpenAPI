<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URL" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%
	List list = (List) request.getAttribute("list");

	String searchText = (String) request.getAttribute("srchwrd");
	
	URL iframe = (URL) request.getAttribute("iframe");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
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
input[type=button]{cursor: pointer;}
</style>
<script>
function search(){
	var f = document.frm;
	var searchText = f.srchwrd.value;
	
	if(searchText == "" || searchText == null){
		alert("검색할 명을 입력하세요.");
		return;
	}
	
	f.submit();
}

function prev(){
	location.href="/openapi";
}
</script>
</head>
<body>
	<form name="frm" method="post" action="/openapi/xmlParse">
		<table width="1024" cellspacing="1" cellpadding="0" style="border-collapse:collapse;" align="center">
			<tr>
				<td width="50%" align="center">
					<table>
						<tr height="100px">
							<td>
								<input type="text" name="srchwrd" id="srchwrd" size="20" value ='<%=searchText%>'/>
								<input type="button" name="searchBtn" id="searchBtn" value="검색" onclick="search()"/>
								ex ) 불정로 6, 온천로 10, 서산로 1, 성산로 1
							</td>
						</tr>
					</table>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table02">
						<tr>
							<td width="20%" class="style1">우편번호</td>
							<td width="40%" class="style1">도로명</td>
							<td width="40%" class="style1">주소</td>
						</tr>
						<%
						if (list != null && list.size() > 0) {
							for (int i=0; i<list.size(); i++) {
								Map map = (Map)list.get(i);
						%>
						<tr>
							<td class="bg_white_c"><%=map.get("zipNo") %></td>
							<td class="bg_white_c"><%=map.get("lnmAdres") %></td>
							<td class="bg_white_c"><%=map.get("rAdres") %></td>
						</tr>
						<%
							}
						} else {
						%>
						<tr>
							<td class="bg_white_c" colspan="3" align="center">데이터가 없습니다.</td>
						</tr>
						<%
						}
						%>
					</table>
					<table align="right">
						<tr height="100px">
							<td>
								<input type="button" size="10" value="이전" onclick="prev()"/>
							</td>
						</tr>
					</table>
					<table>
						<tr height="100px">
							<td>
								<iframe src ="<%=iframe%>" width="1024" height="300px"></iframe>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>