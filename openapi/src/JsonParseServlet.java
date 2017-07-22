import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

public class JsonParseServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		performTask(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		performTask(req, resp);
	}

	/**
	 * Task를 수행한다.
	 * 
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = Util.toString((String) request.getParameter("page"), "1");

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("168.78.105.156", 6588));
		URL url = null; // Open API 접속 URL
		String serviceKey = "bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D";
		String listSize = "10";
		String parameter = "s_page=" + page + "&s_list=" + listSize + "&type=json&serviceKey=" + serviceKey;
		HttpURLConnection conn = null; // URLConnection
		BufferedReader in = null;
		String inputLine = null;
		String strText = "";

		// URL Connection을 이용해 URL을 호출한다.
		url = new URL("http://api.data.go.kr/openapi/3ed91f6a-fe5b-43fc-ad5d-df606e35a94b?" + parameter);
		conn = (HttpURLConnection) url.openConnection(proxy);
		// conn = (HttpURLConnection) url.openConnection();
		
		System.out.println(url);
		conn.connect();

		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		// 호출 결과 문자열을 받아온다.
		while ((inputLine = in.readLine()) != null) {
			strText = strText + inputLine;

		} // end while (inputLine = in.readLine()) != null
		in.close();

		// JSON 문자열을 List로 변환한다.
		List list = new ArrayList();

		if (!"".equals(strText)) {
			for (Object object : JSONArray.fromObject(strText)) {
				Map jsonMap = (Map) object;
				Map map = new HashMap();
				
				map.put("name", Util.toString(String.valueOf(jsonMap.get("명칭"))));
				map.put("streetName", Util.toString(String.valueOf(jsonMap.get("약국소재지(도로명)"))));
				map.put("zipCode", Util.toString(String.valueOf(jsonMap.get("약국우편번호"))));
				map.put("address", Util.toString(String.valueOf(jsonMap.get("주소"))));
				map.put("telNum", Util.toString(String.valueOf(jsonMap.get("연락처"))));

				list.add(map);
			} // end for JSONArray.fromObject(strText)
		} // end if !"".equals(strText)

		request.setAttribute("iframe", url);
		request.setAttribute("list", list);

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/json.jsp");
		rd.forward(request, response);
	}
}
