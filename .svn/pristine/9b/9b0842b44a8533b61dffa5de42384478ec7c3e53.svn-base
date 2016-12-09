package org.easywechat.servlet;

import org.easywechat.msg.BaseMsg;
import org.easywechat.msg.TextMsg;
import org.easywechat.msg.req.BaseEvent;
import org.easywechat.msg.req.BaseReqMsg;
import org.easywechat.msg.req.ImageReqMsg;
import org.easywechat.msg.req.LocationReqMsg;
import org.easywechat.msg.req.MenuEvent;
import org.easywechat.msg.req.TextReqMsg;
import org.easywechat.msg.req.VideoReqMsg;
import org.easywechat.msg.req.VoiceReqMsg;

@SuppressWarnings("serial")
public abstract class SimpleWeixinSupport extends WeixinServletSupport {

	protected String handleTextWithText(String content) {
		return null;
	}

	protected BaseMsg onText(String content) {
		return null;
	}

	protected BaseMsg onImage(String mediaId, String picUrl) {
		return null;
	}

	protected BaseMsg onVoice(String mediaId, String format) {
		return null;
	}

	protected BaseMsg onVideo(String mediaId, String thumbMediaId) {
		return null;
	}

	protected BaseMsg onLocation(double locationX, double locationY, int scale,
			String label) {
		return null;
	}

	protected BaseMsg onLink(String title, String description, String url) {
		return null;
	}

	private BaseMsg onMenuClick(String eventKey) {
		return null;
	}

	@Override
	protected final BaseMsg handleTextMsg(TextReqMsg msg) {

		String reqText = msg.getContent();

		String respText = handleTextWithText(reqText);
		if (respText != null) {// handleTextWithText��������д
			return new TextMsg(respText);
		}

		BaseMsg respMsg = onText(reqText);
		return responseMsgOrDefault(respMsg, msg);
	}

	@Override
	protected final BaseMsg handleImageMsg(ImageReqMsg msg) {
		BaseMsg respMsg = onImage(msg.getMediaId(), msg.getPicUrl());
		return responseMsgOrDefault(respMsg, msg);
	}

	@Override
	protected final BaseMsg handleVoiceMsg(VoiceReqMsg msg) {
		BaseMsg respMsg = onVoice(msg.getMediaId(), msg.getFormat());
		return responseMsgOrDefault(respMsg, msg);
	}

	@Override
	protected final BaseMsg handleVideoMsg(VideoReqMsg msg) {
		BaseMsg respMsg = onVideo(msg.getMediaId(), msg.getThumbMediaId());
		return responseMsgOrDefault(respMsg, msg);
	}

	@Override
	protected final BaseMsg handleLocationMsg(LocationReqMsg msg) {
		BaseMsg respMsg = onLocation(msg.getLocationX(), msg.getLocationY(),
				msg.getScale(), msg.getLabel());
		return responseMsgOrDefault(respMsg, msg);
	}

	@Override
	protected BaseMsg handleMenuClickEvent(MenuEvent event) {
		BaseMsg respMsg = onMenuClick(event.getEventKey());
		return responseMsgOrDefault(respMsg, event);
	}

	private BaseMsg responseMsgOrDefault(BaseMsg respMsg, BaseReqMsg reqMsg) {
		if (respMsg != null) {
			return respMsg;
		}

		return handleDefaultMsg(reqMsg);
	}

	private BaseMsg responseMsgOrDefault(BaseMsg respMsg, BaseEvent reqEvent) {
		if (respMsg != null) {
			return respMsg;
		}

		return handleDefaultEvent(reqEvent);
	}
}
