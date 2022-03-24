package org.lee.study.jdbc;


public class Content {
	String id;
	String content;

	public Content(String id, String content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public String toString() {
		return "Content{" +
				"id='" + id + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
