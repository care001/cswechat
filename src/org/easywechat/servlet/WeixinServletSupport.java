package org.easywechat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easywechat.msg.BaseMsg;
import org.easywechat.msg.req.BaseEvent;
import org.easywechat.msg.req.BaseReq;
import org.easywechat.msg.req.BaseReqMsg;
import org.easywechat.msg.req.EventType;
import org.easywechat.msg.req.ImageReqMsg;
import org.easywechat.msg.req.LinkReqMsg;
import org.easywechat.msg.req.LocationEvent;
import org.easywechat.msg.req.LocationReqMsg;
import org.easywechat.msg.req.MenuEvent;
import org.easywechat.msg.req.QrCodeEvent;
import org.easywechat.msg.req.ReqType;
import org.easywechat.msg.req.TextReqMsg;
import org.easywechat.msg.req.VideoReqMsg;
import org.easywechat.msg.req.VoiceReqMsg;
import org.easywechat.util.MessageUtil;
import org.easywechat.util.SignUtil;

import com.jc.entity.AccessToken;
import com.jc.entity.AesKey;
import com.jc.listeners.InitializeListerner;

public abstract class WeixinServletSupport extends HttpServlet {

	private static final long serialVersionUID = 9066131895965698996L;
	
	protected AesKey aesKey = InitializeListerner.aesKey;
	protected AccessToken accessToken = InitializeListerner.accessToken;
	protected String fromUserName;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get init servlet..");
		if (isLegal(request)) {
			PrintWriter out = response.getWriter();
			out.print(request.getParameter("echostr"));
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("post init servlet..");
		if (!isLegal(request)) {
			return;
		}
		
		String resp = processRequest(request);
		
		resp = InitializeListerner.aesKey.encryptAseKey(resp);
		
		PrintWriter out = response.getWriter();
		out.print(resp);
		out.close();
	}
	
	private String processRequest(HttpServletRequest request) {
		Map<String, String> reqMap = MessageUtil.parseXml(request);	
		this.fromUserName = reqMap.get("FromUserName");
		String toUserName = reqMap.get("ToUserName");
		String msgType = reqMap.get("MsgType");

		BaseMsg msg = null;

		if (msgType.equals(ReqType.EVENT)) {
			String eventType = reqMap.get("Event");

			String ticket = reqMap.get("Ticket");
			if (ticket != null) {
				String eventKey = reqMap.get("EventKey");
				QrCodeEvent event = new QrCodeEvent(eventKey, ticket);
				buildBasicEvent(reqMap, event);
				msg = handleQrCodeEvent(event);
			}
			if (eventType.equals(EventType.SUBSCRIBE)) {
				BaseEvent event = new BaseEvent();
				buildBasicEvent(reqMap, event);
				msg = handleSubscribe(event);
			}
			else if (eventType.equals(EventType.UNSUBSCRIBE)) {
				BaseEvent event = new BaseEvent();
				buildBasicEvent(reqMap, event);
				msg = handleUnsubscribe(event);
			}
			else if (eventType.equals(EventType.CLICK)) {
				String eventKey = reqMap.get("EventKey");
				MenuEvent event = new MenuEvent(eventKey);
				buildBasicEvent(reqMap, event);
				msg = handleMenuClickEvent(event);
			}
			else if (eventType.equals(EventType.VIEW)) {
				String eventKey = reqMap.get("EventKey");
				MenuEvent event = new MenuEvent(eventKey);
				buildBasicEvent(reqMap, event);
				msg = handleMenuViewEvent(event);
			}
			else if (eventType.equals(EventType.LOCATION)) {
				double latitude = Double.parseDouble(reqMap.get("Latitude"));
				double longitude = Double.parseDouble(reqMap.get("Longitude"));
				double precision = Double.parseDouble(reqMap.get("Precision"));
				LocationEvent event = new LocationEvent(latitude, longitude,
						precision);
				buildBasicEvent(reqMap, event);
				msg = handleLocationEvent(event);
			}
		} else {
			if (msgType.equals(ReqType.TEXT)) {
				String content = reqMap.get("Content");
				TextReqMsg textReqMsg = new TextReqMsg(content);
				buildBasicReqMsg(reqMap, textReqMsg);
				msg = handleTextMsg(textReqMsg);
			}
			else if (msgType.equals(ReqType.IMAGE)) {
				String picUrl = reqMap.get("PicUrl");
				String mediaId = reqMap.get("MediaId");
				ImageReqMsg imageReqMsg = new ImageReqMsg(picUrl, mediaId);
				buildBasicReqMsg(reqMap, imageReqMsg);
				msg = handleImageMsg(imageReqMsg);
			}
			else if (msgType.equals(ReqType.VOICE)) {
				String format = reqMap.get("Format");
				String mediaId = reqMap.get("MediaId");
				String recognition = reqMap.get("Recognition");
				VoiceReqMsg voiceReqMsg = new VoiceReqMsg(mediaId, format,
						recognition);
				buildBasicReqMsg(reqMap, voiceReqMsg);
				msg = handleVoiceMsg(voiceReqMsg);
			}
			else if (msgType.equals(ReqType.VIDEO)) {
				String thumbMediaId = reqMap.get("ThumbMediaId");
				String mediaId = reqMap.get("MediaId");
				VideoReqMsg videoReqMsg = new VideoReqMsg(mediaId, thumbMediaId);
				buildBasicReqMsg(reqMap, videoReqMsg);
				msg = handleVideoMsg(videoReqMsg);
			}
			else if (msgType.equals(ReqType.LOCATION)) {
				double locationX = Double.parseDouble(reqMap.get("Location_X"));
				double locationY = Double.parseDouble(reqMap.get("Location_Y"));
				int scale = Integer.parseInt(reqMap.get("Scale"));
				String label = reqMap.get("Label");
				LocationReqMsg locationReqMsg = new LocationReqMsg(locationX,
						locationY, scale, label);
				buildBasicReqMsg(reqMap, locationReqMsg);
				msg = handleLocationMsg(locationReqMsg);
			}
			else if (msgType.equals(ReqType.LINK)) {
				String title = reqMap.get("Title");
				String description = reqMap.get("Description");
				String url = reqMap.get("Url");
				LinkReqMsg linkReqMsg = new LinkReqMsg(title, description, url);
				buildBasicReqMsg(reqMap, linkReqMsg);
				msg = handleLinkMsg(linkReqMsg);
			}

		}

		if (msg == null) {
			return "";
		}

		msg.setFromUserName(toUserName);
		msg.setToUserName(fromUserName);
		return msg.toXml();
	}

	protected BaseMsg handleTextMsg(TextReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleImageMsg(ImageReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleVoiceMsg(VoiceReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleVideoMsg(VideoReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleLocationMsg(LocationReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleLinkMsg(LinkReqMsg msg) {
		return handleDefaultMsg(msg);
	}

	protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
		return handleDefaultEvent(event);
	}

	protected BaseMsg handleLocationEvent(LocationEvent event) {
		return handleDefaultEvent(event);
	}

	protected BaseMsg handleMenuClickEvent(MenuEvent event) {
		return handleDefaultEvent(event);
	}

	protected BaseMsg handleMenuViewEvent(MenuEvent event) {
		return handleDefaultEvent(event);
	}

	protected BaseMsg handleSubscribe(BaseEvent event) {
		return null;
	}

	protected BaseMsg handleUnsubscribe(BaseEvent event) {
		return null;
	}

	protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
		return null;
	}

	protected BaseMsg handleDefaultEvent(BaseEvent event) {
		return null;
	}

	private void buildBasicReqMsg(Map<String, String> reqMap, BaseReqMsg reqMsg) {
		addBasicReqParams(reqMap, reqMsg);
		reqMsg.setMsgId(reqMap.get("MsgId"));
	}

	private void buildBasicEvent(Map<String, String> reqMap, BaseEvent event) {
		addBasicReqParams(reqMap, event);
		event.setEvent(reqMap.get("Event"));
	}

	private void addBasicReqParams(Map<String, String> reqMap, BaseReq req) {
		req.setMsgType(reqMap.get("MsgType"));
		req.setFromUserName(reqMap.get("FromUserName"));
		req.setToUserName(reqMap.get("ToUserName"));
		req.setCreateTime(Long.parseLong(reqMap.get("CreateTime")));
	}

	private boolean isLegal(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		return SignUtil.checkSignature(InitializeListerner.accessToken.getToken(), signature, timestamp, nonce);
	}
}
