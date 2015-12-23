package de.grundid.drinker.utils;

import java.util.Date;

public class DatedResponse<T> {

	private Date date;
	private T content;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
}
