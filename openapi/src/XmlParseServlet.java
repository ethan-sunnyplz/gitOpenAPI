import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlParseServlet extends HttpServlet {
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
		request.setCharacterEncoding("UTF-8");
		String searchSe = Util.toString((String) request.getParameter("searchSe"), "road"); // 검색구분
		String srchwrd = Util.toString((String) request.getParameter("srchwrd"), "문지로 105"); // 검색어

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("168.78.105.156", 6588));
		URL url = null; // Open API 접속 URL
		String serviceKey = "bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D";
		String parameter = "searchSe=" + searchSe + "&srchwrd=" + URLEncoder.encode(srchwrd, "UTF-8") + "&serviceKey=" + serviceKey;

		HttpURLConnection conn = null; // URLConnection
		BufferedReader in = null;

		// 모바일 인증키
		// bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D

		// URL Connection을 이용해 URL을 호출한다.
		url = new URL("http://openapi.epost.go.kr/postal/retrieveNewAdressService/retrieveNewAdressService/getNewAddressList?" + parameter);
		conn = (HttpURLConnection) url.openConnection(proxy);
		// conn = (HttpURLConnection) url.openConnection();

		conn.connect();

		// 호출 결과 문자열을 받아온다.
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		SAXBuilder saxBuilder = new SAXBuilder(); // JDom
		Document doc = null;
		Element attrEl = null;
		List list = new ArrayList();

		try {
			// XML 문자열을 Jdom을 통해 파싱한다.
			doc = saxBuilder.build(in);

			if (doc != null) {
				Element rootEl = doc.getRootElement(); // 루트 엘리먼트

				Element msgEl = rootEl.getChild("cmmMsgHeader"); // cmmMsgHeader 엘리먼트
				attrEl = msgEl.getChild("successYN"); // 성공여부
				String successYn = attrEl.getText();
				attrEl = msgEl.getChild("returnCode"); // 리턴코드
				String returnCode = attrEl.getText();

				if ("Y".equals(successYn) && "00".equals(returnCode)) { // 성공일 경우에만
					List addressList = rootEl.getChildren("newAddressList");

					for (int i = 0; i < addressList.size(); i++) {
						Element addressEl = (Element) addressList.get(i);

						attrEl = addressEl.getChild("zipNo");
						String zipNo = attrEl.getText(); // 우편번호
						attrEl = addressEl.getChild("lnmAdres");
						String lnmAdres = attrEl.getText(); // 도로명주소
						attrEl = addressEl.getChild("rnAdres");
						String rnAdres = attrEl.getText(); // 지번주소

						Map map = new HashMap();
						map.put("zipNo", zipNo);
						map.put("lnmAdres", lnmAdres);
						map.put("rAdres", rnAdres);

						list.add(map);
					} // end for i
				} // end if "Y".equals(successYn) && "00".equals(returnCode)
			} // end if doc != null
			
			request.setAttribute("iframe", url);
			request.setAttribute("list", list);
			request.setAttribute("srchwrd", srchwrd);

			RequestDispatcher rd = getServletContext().getRequestDispatcher("/xml.jsp");
			rd.forward(request, response);
		} catch (JDOMException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}
}
