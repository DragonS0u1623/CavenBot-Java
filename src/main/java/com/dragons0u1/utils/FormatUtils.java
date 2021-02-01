package com.dragons0u1.utils;

import java.util.concurrent.*;

public class FormatUtils {

	public static String getLength(long length) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(length),
				TimeUnit.MILLISECONDS.toSeconds(length)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(length)));
	}
	
	public static String percentEncode(String toUrl) {
		String urlEncoded = toUrl.replaceAll("%", "%25").replaceAll(" ", "%20").replaceAll("!", "%21").replaceAll("&", "%26")
				.replaceAll("#", "%23").replaceAll("$", "%24").replaceAll("'", "%27").replaceAll("*", "%2A")
				.replaceAll("+", "%2B").replaceAll(",", "%2C").replaceAll("/", "%2F").replaceAll(":", "%3A")
				.replaceAll(";", "%3B").replaceAll("@", "%40").replaceAll("\"", "%22");
		return urlEncoded;
	}
	
}
