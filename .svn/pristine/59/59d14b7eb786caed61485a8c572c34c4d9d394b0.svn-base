package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class LocationReqMsg extends BaseReqMsg {

	private double locationX;
	private double locationY;
	private int scale;
	private String label;

	public double getLocationX() {
		return locationX;
	}

	public double getLocationY() {
		return locationY;
	}

	public int getScale() {
		return scale;
	}

	public String getLabel() {
		return label;
	}

	public LocationReqMsg(double locationX, double locationY, int scale,
			String label) {
		super();
		this.locationX = locationX;
		this.locationY = locationY;
		this.scale = scale;
		this.label = label;
		setMsgType(ReqType.LOCATION);
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", ReqType.LOCATION);
		mb.addTag("Location_X", String.valueOf(locationY));
		mb.addTag("Location_Y", String.valueOf(locationX));
		mb.addTag("Scale", String.valueOf(scale));
		mb.addData("Label", label);
		mb.addTag("MsgId", msgId);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "LocationReqMsg [locationX=" + locationX + ", locationY="
				+ locationY + ", scale=" + scale + ", label=" + label
				+ ", toUserName=" + toUserName + ", fromUserName="
				+ fromUserName + ", createTime=" + createTime + ", msgType="
				+ msgType + ", msgId=" + msgId + "]";
	}

}
