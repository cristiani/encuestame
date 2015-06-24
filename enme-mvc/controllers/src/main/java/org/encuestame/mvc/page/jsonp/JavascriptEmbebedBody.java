package org.encuestame.mvc.page.jsonp;

import java.io.Serializable;

public class JavascriptEmbebedBody implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4135597482148246813L;

	/**
	 *
	 */
	private String body;

	/**
	 *
	 */
	private String config;

	/**
	 *
	 */
	private String header;

	private Object aditionalInfo;

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 *
	 * @return
	 */
	public Object getAditionalInfo() {
		return aditionalInfo;
	}

	/**
	 *
	 * @param aditionalInfo
	 */
	public void setAditionalInfo(Object aditionalInfo) {
		this.aditionalInfo = aditionalInfo;
	}


}
