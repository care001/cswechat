package com.jc.main;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import org.easywechat.msg.BaseMsg;
import org.easywechat.msg.req.BaseEvent;
import org.easywechat.msg.req.MenuEvent;
import org.easywechat.servlet.SimpleWeixinSupport;
import org.easywechat.util.MessageFactory;

import com.jc.util.CreateMenu;

/**
 * weixin main enter
 * @author tom
 * @version 1.1
 * 2015.12.1
 */
@WebServlet("/")
public class Enter extends SimpleWeixinSupport{

	private static final long serialVersionUID = 1579329624540805207L;
	
	// handle text
	@Override
    protected BaseMsg onText(String content) {
		
		// create menu
		if(content.equals(accessToken.getToken()+"-createmenu")){
			CreateMenu createMenu = new CreateMenu();
			try {
				createMenu.createMenu();
				createMenu.createMenu2();
				return MessageFactory.createTextMsg("operation successful");
			} catch (IOException e) {
				return MessageFactory.createTextMsg("operation failure");
			}
		}
				
		return null;
    }
	
	// handle menu click
	@Override
	protected BaseMsg handleMenuClickEvent(MenuEvent event) {
		
		
		if(event.getEventKey().equals("INFO")){
			
		}
		
		// Contact customer service
		if(event.getEventKey().equals("CS")){
			String message = "您好，感谢您关注金诚无卡消费公众号。";
			return MessageFactory.createTextMsg(message);
		}
		return null;
	}

	// handle enter
	@Override
	protected BaseMsg handleSubscribe(BaseEvent event) {
		//String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx12198bbce7d872ba&redirect_uri=http%3A%2F%2Fweixinapi.gold-finance.com.cn%2Fjcwechatweb%2FgetCode.action&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
		
		// check user wether or not bind
		/*String userId = GetUserId.getUserId(fromUserName);
		if(null != userId){
			MoveGroup.moveGroupById(fromUserName, "100");
		}*/
		
		String message = "您好，感谢您关注金诚无卡消费公众号。";
		return MessageFactory.createTextMsg(message);
	}
	
	
	
}