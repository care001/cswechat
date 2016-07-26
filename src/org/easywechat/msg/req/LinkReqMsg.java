package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class LinkReqMsg extends BaseReqMsg {

	private String title;
	private String description;
	private String url;

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public LinkReqMsg(String title, String description, String url) {
		super();
		this.title = title;
		this.description = description;
		this.url = url;
		setMsgType(ReqType.EVENT);
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", ReqType.LINK);
		mb.addData("Title", title);
		mb.addData("Description", description);
		mb.addData("Url", url);
		mb.addTag("MsgId", msgId);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "LinkReqMsg [title=" + title + ", description=" + description
				+ ", url=" + url + ", toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + ", msgId=" + msgId
				+ "]";
	}

}
