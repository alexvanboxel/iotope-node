package org.iotope.node.apps.ttag;

import java.util.ArrayList;
import java.util.List;

public class TagEventAttachment {
	private String uri;
	private Object content;
	
	public TagEventAttachment(String uri, Object content) {
		super();
		this.uri = uri;
		this.content = content;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	public static List<TagEventAttachment> asList(String uri, Object content) {
		List<TagEventAttachment> result = new ArrayList<TagEventAttachment>();
		result.add(new TagEventAttachment(uri,content));
		return result;
	}
}
