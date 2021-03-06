package group.zerry.front_server.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import group.zerry.front_server.annotation.AuthPass;
import group.zerry.front_server.service.MessageService;
import group.zerry.front_server.utils.CookiesData;

@Controller
@RequestMapping(value="/message")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	CookiesData cookiesData;
	
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/send", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String post_message(String username, String userToken, String content, int type) {
		if(messageService.send_message(username, userToken, content, type))
			return "{\"msg\" : 1}";
		else 
			return "{\"msg\" : 0}";
	}
	
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String delete_message(String username, String userToken, int id) {
		if(messageService.delete_message(username, userToken, id))
			return "{\"msg\" : 1}";
		else 
			return "{\"msg\" : 0}";
	}
	
	/**
	 * @return 1:点赞成功 0：操作异常 2：已点过赞
	 */
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/support", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String addSupportTimes(String username, String userToken, int id) {
		StringBuilder regMsg = new StringBuilder("{\"msg\": ");
		regMsg.append(messageService.addSupportTimes(username, userToken, id) + "}");
		return regMsg.toString();
	}
	
	/**
	 * @return 1:取消点赞成功 0：操作异常 2：没有点过赞
	 */
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/_support", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String decreaseSupportTimes(String username, String userToken, int id) {
		StringBuilder regMsg = new StringBuilder("{\"msg\": ");
		regMsg.append(messageService.decreaseSupportTimes(username, userToken, id) + "}");
		return regMsg.toString();
	}
	
	/**
	 * 有缓存show
	 * @return
	 */
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/show", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String show_messages(HttpServletRequest request, HttpServletResponse response, String username, String userToken, int page, int type) {
		Cookie cookie;
		if (null == (cookie = cookiesData.getCookie(request, "message"))) {
			String returnMsg = messageService.show_messages(username, userToken, page, type);
			cookiesData.safe(request, response, "message", returnMsg);
			return returnMsg;
		} else {
			System.out.println(cookie.getValue());
			return cookie.getValue();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/show_announcements", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String show_announcements() {
		return messageService.show_announcement();
	}
	
	@ResponseBody
	@RequestMapping(value = "/show_ownmessages", produces = "text/html;charset=UTF-8")
	public String show_ownMessages(String nickname, int page) {
		return messageService.show_userOwnMessages(nickname, page);
	}
	
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/judge_ifsupport", produces = "text/html;charset=UTF-8")
	public String judgeIfSupported(String username, int message_id, String userToken) {
		return messageService.judgeIfSupport(username, message_id, userToken);
	}
	
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/comment", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String send_comment(String username, String userToken, int id, String content) {
		if(messageService.send_comment(username, userToken, id, content))
			return "{\"msg\" : 1}";
		else 
			return "{\"msg\" : 0}";
	}
	
	@AuthPass
	@ResponseBody
	@RequestMapping(value = "/repost", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String repost_message(String username, String userToken, int id) {
		if(messageService.add_repost(username, userToken, id))
			return "{\"msg\" : 1}";
		else 
			return "{\"msg\" : 0}";
	}
}
