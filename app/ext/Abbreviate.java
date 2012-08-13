package ext;

import play.templates.JavaExtensions;

public class Abbreviate extends JavaExtensions {

	public static String abbreviate(String str, int maxWidth) {
		if (str == null) {
			return null;
		}
		if (str.length() <= maxWidth) {
			return str;
		} else {
			return str.substring(0, maxWidth) + " ...";
		}
	}

}
