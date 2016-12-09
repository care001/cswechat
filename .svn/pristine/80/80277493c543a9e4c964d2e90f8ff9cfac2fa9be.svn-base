package org.easywechat.util;

import org.easywechat.msg.ImageMsg;
import org.easywechat.msg.MusicMsg;
import org.easywechat.msg.NewsMsg;
import org.easywechat.msg.TextMsg;
import org.easywechat.msg.VideoMsg;
import org.easywechat.msg.VoiceMsg;

public class MessageFactory {

	public static TextMsg createTextMsg() {
		return new TextMsg();
	}

	public static TextMsg createTextMsg(String content) {
		return new TextMsg(content);
	}

	public static ImageMsg createImageMsg(String mediaId) {
		return new ImageMsg(mediaId);
	}

	public static VoiceMsg createVoiceMsg(String mediaId) {
		return new VoiceMsg(mediaId);
	}

	public static VideoMsg createVideoMsg(String mediaId, String title,
			String description) {
		return new VideoMsg(mediaId, title, description);
	}

	public static MusicMsg createMusicMsg(String thumbMediaId, String title,
			String description, String musicUrl, String hqMusicUrl) {
		return new MusicMsg(thumbMediaId, title, description, musicUrl,
				hqMusicUrl);
	}

	public static NewsMsg createNewsMsg() {
		return new NewsMsg();
	}

	public static NewsMsg createNewsMsg(String title, String description,
			String picUrl, String url) {
		return new NewsMsg().add(title, description, picUrl, url);
	}

}
