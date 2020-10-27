package com.example.demo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageExtract {

	static String extractPage(String page) {
		String pattern = "\\/m(.*)\\/";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(page);
		if (m.find()) {
			return m.group(0);
		}
		return null;
	}
}
