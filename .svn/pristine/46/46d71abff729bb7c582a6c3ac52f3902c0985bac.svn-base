package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class VideoReqMsg extends BaseReqMsg {

	private String mediaId;
	private String thumbMediaId;

	public String getMediaId() {
		return mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public VideoReqMsg(String mediaId, String thumbMediaId) {
		super();
		this.mediaId = mediaId;
		this.thumbMediaId = thumbMediaId;
		setMsgType(ReqType.VIDEO);
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", ReqType.VIDEO);
		mb.addData("MediaId", mediaId);
		mb.addData("ThumbMediaId", thumbMediaId);
		mb.addTag("MsgId", msgId);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "VideoReqMsg [mediaId=" + mediaId + ", thumbMediaId="
				+ thumbMediaId + ", toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + ", msgId=" + msgId
				+ "]";
	}

}
