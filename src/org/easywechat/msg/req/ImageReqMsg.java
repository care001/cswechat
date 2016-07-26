package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class ImageReqMsg extends BaseReqMsg {

	private String picUrl;
	private String mediaId;

	public String getPicUrl() {
		return picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public ImageReqMsg(String picUrl, String mediaId) {
		super();
		this.picUrl = picUrl;
		this.mediaId = mediaId;
		setMsgType(ReqType.IMAGE);
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", ReqType.IMAGE);
		mb.addData("PicUrl", picUrl);
		mb.addData("MediaId", mediaId);
		mb.addTag("MsgId", msgId);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "ImageReqMsg [picUrl=" + picUrl + ", mediaId=" + mediaId
				+ ", toUserName=" + toUserName + ", fromUserName="
				+ fromUserName + ", createTime=" + createTime + ", msgType="
				+ msgType + ", msgId=" + msgId + "]";
	}

}
