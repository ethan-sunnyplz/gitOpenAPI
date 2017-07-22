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
	 * Task�� �����Ѵ�.
	 * 
	 * @param request HttpServletRequest ��ü
	 * @param response HttpServletResponse ��ü
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String searchSe = Util.toString((String) request.getParameter("searchSe"), "road"); // �˻�����
		String srchwrd = Util.toString((String) request.getParameter("srchwrd"), "������ 105"); // �˻���

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("168.78.105.156", 6588));
		URL url = null; // Open API ���� URL
		String serviceKey = "bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D";
		String parameter = "searchSe=" + searchSe + "&srchwrd=" + URLEncoder.encode(srchwrd, "UTF-8") + "&serviceKey=" + serviceKey;

		HttpURLConnection conn = null; // URLConnection
		BufferedReader in = null;

		// ����� ����Ű
		// bsscVpCKj4LuoY76Hb9zYzRr0kDU5Kv5D7bYxSChKvCmigcSN0ixk5CcPbyp2Z6zPby%2BcK3%2BlH0s0D1bJRLRbA%3D%3D

		// URL Connection�� �̿��� URL�� ȣ���Ѵ�.
		url = new URL("http://openapi.epost.go.kr/postal/retrieveNewAdressService/retrieveNewAdressService/getNewAddressList?" + parameter);
		conn = (HttpURLConnection) url.openConnection(proxy);
		// conn = (HttpURLConnection) url.openConnection();

		conn.connect();

		// ȣ�� ��� ���ڿ��� �޾ƿ´�.
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		SAXBuilder saxBuilder = new SAXBuilder(); // JDom
		Document doc = null;
		Element attrEl = null;
		List list = new ArrayList();

		try {
			// XML ���ڿ��� Jdom�� ���� �Ľ��Ѵ�.
			doc = saxBuilder.build(in);

			if (doc != null) {
				Element rootEl = doc.getRootElement(); // ��Ʈ ������Ʈ

				Element msgEl = rootEl.getChild("cmmMsgHeader"); // cmmMsgHeader ������Ʈ
				attrEl = msgEl.getChild("successYN"); // ��������
				String successYn = attrEl.getText();
				attrEl = msgEl.getChild("returnCode"); // �����ڵ�
				String returnCode = attrEl.getText();

				if ("Y".equals(successYn) && "00".equals(returnCode)) { // ������ ��쿡��
					List addressList = rootEl.getChildren("newAddressList");

					for (int i = 0; i < addressList.size(); i++) {
						Element addressEl = (Element) addressList.get(i);

						attrEl = addressEl.getChild("zipNo");
						String zipNo = attrEl.getText(); // �����ȣ
						attrEl = addressEl.getChild("lnmAdres");
						String lnmAdres = attrEl.getText(); // ���θ��ּ�
						attrEl = addressEl.getChild("rnAdres");
						String rnAdres = attrEl.getText(); // �����ּ�

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
