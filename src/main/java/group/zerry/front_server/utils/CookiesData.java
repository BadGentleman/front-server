package group.zerry.front_server.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.http.HttpResponse;


public class CookiesData {
	public Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(null == cookies) {
			return null;
		}
		else {
			if(cookies.length == 0) {
				return null; // cookies被用户禁用
			} else {
				for (int i = 0; i < cookies.length; i++) 
				 { 
					Cookie c = cookies[i];       
					if(c.getName().equals(cookieName))  
					{  
				         if (c.getValue() != null || !c.getValue().equals("")) {
				        	 return c;
				         }
					}
				 }
				return null;
			}
		}
	}
	
	/**
	 * @param cookieName ： 字段名， newData ： 新的json数据，用于与cookie中数据比对 
	 * @author zhuzirui
	 *
	 */
	public boolean ifNeed_safe(HttpServletRequest request, String cookieName, String newData) {
		Cookie[] cookies = request.getCookies();
		if(null == cookies) {
			return true;
		}
		else {
			if(cookies.length == 0) {
				return false; // cookies被用户禁用
			} else {
				for (int i = 0; i < cookies.length; i++) 
				 { 
					Cookie c = cookies[i];       
					if(c.getName().equals(cookieName))  
					{  
				         if (c.getValue().equals(newData)) {
							return false;
				         } else {
							return true;
				         } // 数据更新
					}
				 }
				return true;
			}
		}
	}
	
	public void safe(HttpServletRequest request, HttpServletResponse response, String cookieName, String newData) {
		Cookie cookie = new Cookie(cookieName, newData);
		cookie.setMaxAge(1200);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
