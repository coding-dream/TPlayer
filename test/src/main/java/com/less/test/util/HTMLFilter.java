package com.less.test.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFilter {

	static String regEx_space ="&nbsp;";

	static String regEx_commons = "<!--.*?-->"; // 定义 html_commons的正则表达式

	static String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式

	static String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

	static String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

	static Pattern p_commons = Pattern.compile(regEx_commons, Pattern.CASE_INSENSITIVE);
	static Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	static Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	static Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	static Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);

	public static String filter(String htmlStr){
		Matcher m_commons = p_commons.matcher(htmlStr);
		htmlStr = m_commons.replaceAll(""); // 过滤html_commons标签

		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤html_commons标签

		htmlStr = htmlStr.replaceAll("\\s+", "  ");
		return htmlStr.trim(); // 返回文本字符串
	}
}
