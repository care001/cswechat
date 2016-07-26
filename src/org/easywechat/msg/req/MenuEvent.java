package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class MenuEvent extends BaseEvent {

	private String eventKey;

	public String getEventKey() {
		return eventKey;
	}

	@Override
	public String getEvent() {
		return super.getEvent();
	}

	public MenuEvent(String eventKey) {
		super();
		this.eventKey = eventKey;
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toString());
		mb.addData("EventKey", eventKey);
		return mb.toString();
	}

	@Override
	public String toString() {
		return "MenuEvent [eventKey=" + eventKey + ", toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + "]";
	}

}
