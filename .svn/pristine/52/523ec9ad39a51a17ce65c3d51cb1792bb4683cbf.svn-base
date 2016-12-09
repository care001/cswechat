package org.easywechat.msg.req;

import org.easywechat.util.MessageBuilder;

public final class QrCodeEvent extends BaseEvent {

	private String eventKey;
	private String ticket;

	public String getEventKey() {
		return eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public QrCodeEvent(String eventKey, String ticket) {
		super();
		this.eventKey = eventKey;
		this.ticket = ticket;
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toString());
		mb.addData("EventKey", eventKey);
		mb.addData("Ticket", ticket);
		return mb.toString();
	}

	@Override
	public String toString() {
		return "QrCodeEvent [eventKey=" + eventKey + ", ticket=" + ticket
				+ ", toUserName=" + toUserName + ", fromUserName="
				+ fromUserName + ", createTime=" + createTime + ", msgType="
				+ msgType + "]";
	}

}
