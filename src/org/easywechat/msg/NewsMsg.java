package org.easywechat.msg;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.easywechat.util.MessageBuilder;

public class NewsMsg extends BaseMsg {
	private static Logger logger = Logger.getLogger(NewsMsg.class);

	private static final int WX_MAX_SIZE = 10;
	private int maxSize = WX_MAX_SIZE;

	List<Article> articles;

	public NewsMsg() {
		this.articles = new ArrayList<Article>(maxSize);
	}

	public NewsMsg(int maxSize) {
		setMaxSize(maxSize);
		this.articles = new ArrayList<Article>(maxSize);
	}

	public NewsMsg(List<Article> articles) {
		setArticles(articles);
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		if (maxSize > WX_MAX_SIZE || maxSize < 1) {
			maxSize = WX_MAX_SIZE;
		}
		this.maxSize = maxSize;
		if (articles != null && articles.size() > maxSize) {
			articles = articles.subList(0, maxSize);
		}
	}

	public List<Article> getArticles() {
		return articles;
	}

	public NewsMsg setArticles(List<Article> articles) {
		if (articles.size() > maxSize) {
			this.articles = articles.subList(0, maxSize);
		} else {
			this.articles = articles;
		}
		return this;
	}

	public NewsMsg add(String title) {
		return add(title, null, null, null);
	}

	public NewsMsg add(String title, String url) {
		return add(title, null, null, url);
	}

	public NewsMsg add(String title, String picUrl, String url) {
		return add(new Article(title, null, picUrl, url));
	}

	public NewsMsg add(String title, String description, String picUrl,
			String url) {
		return add(new Article(title, description, picUrl, url));
	}

	public NewsMsg add(Article article) {
		if (this.articles.size() < maxSize) {
			this.articles.add(article);
		} else {
			logger.warn("Article��������");
			// ���Article�����������������
		}
		return this;
	}

	@Override
	public String toXml() {
		MessageBuilder mb = new MessageBuilder(super.toXml());
		mb.addData("MsgType", RespType.NEWS);
		mb.addTag("ArticleCount", String.valueOf(articles.size()));
		mb.append("<Articles>\n");
		for (Article article : articles) {
			mb.append(article.toXml());
		}
		mb.append("</Articles>\n");
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("NewsMsg [articles=\n");
		for (int i = 0; i < articles.size(); i++) {
			sb.append("  Article ").append(i).append(": ")
					.append(articles.get(i)).append("\n");
		}
		sb.append("]");
		return sb.toString();
	}

}
