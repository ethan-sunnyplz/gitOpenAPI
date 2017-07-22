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
	 * Task�� �����Ѵ�.
	 * 
	 * @param request HttpServletRequest ��ü
	 * @param response HttpServletResponse ��ü
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = Util.toString((String) request.getParameter("page"), "1");

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("168.78.105.156", 6588));
		URL url = null; // Open API ���� URL
		String serviceKey = "bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D";
		String listSize = "10";
		String parameter = "s_page=" + page + "&s_list=" + listSize + "&type=json&serviceKey=" + serviceKey;
		HttpURLConnection conn = null; // URLConnection
		BufferedReader in = null;
		String inputLine = null;
		String strText = "";

		// URL Connection�� �̿��� URL�� ȣ���Ѵ�.
		url = new URL("http://api.data.go.kr/openapi/3ed91f6a-fe5b-43fc-ad5d-df606e35a94b?" + parameter);
		conn = (HttpURLConnection) url.openConnection(proxy);
		// conn = (HttpURLConnection) url.openConnection();
		
		System.out.println(url);
		conn.connect();

		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		// ȣ�� ��� ���ڿ��� �޾ƿ´�.
		while ((inputLine = in.readLine()) != null) {
			strText = strText + inputLine;

		} // end while (inputLine = in.readLine()) != null
		in.close();

		// JSON ���ڿ��� List�� ��ȯ�Ѵ�.
		List list = new ArrayList();

		if (!"".equals(strText)) {
			for (Object object : JSONArray.fromObject(strText)) {
				Map jsonMap = (Map) object;
				Map map = new HashMap();
				
				map.put("name", Util.toString(String.valueOf(jsonMap.get("��Ī"))));
				map.put("streetName", Util.toString(String.valueOf(jsonMap.get("�౹������(���θ�)"))));
				map.put("zipCode", Util.toString(String.valueOf(jsonMap.get("�౹�����ȣ"))));
				map.put("address", Util.toString(String.valueOf(jsonMap.get("�ּ�"))));
				map.put("telNum", Util.toString(String.valueOf(jsonMap.get("����ó"))));

				list.add(map);
			} // end for JSONArray.fromObject(strText)
		} // end if !"".equals(strText)

		request.setAttribute("iframe", url);
		request.setAttribute("list", list);

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/json.jsp");
		rd.forward(request, response);
	}
}
