
public class Util {
	public static String toString(String str) {
		if (str == null) return "";
		if ("null".equals(str.trim())) return "";
		else return str;
	}

	public static String toString(String str, String defaultStr) {
		if (str == null) return defaultStr;
		else if ("null".equals(str)) return defaultStr;
		else if ("".equals(str)) return defaultStr;
		else return str;
	}
}
