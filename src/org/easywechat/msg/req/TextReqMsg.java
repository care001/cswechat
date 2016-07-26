package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class TextReqMsg extends BaseReqMsg {

	private String content;
	
	public String getContent() {
		return content;
	}

	public TextReqMsg(String content) {
		super();
		this.content = content;
		setMsgType(ReqType.TEXT);
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", ReqType.TEXT);
		mb.addData("Content", content);
		mb.addTag("MsgId", msgId);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "TextReqMsg [content=" + content + ", toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + ", msgId=" + msgId
				+ "]";
	}

}
