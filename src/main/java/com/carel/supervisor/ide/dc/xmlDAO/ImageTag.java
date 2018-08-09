/**
 * 
 */
package com.carel.supervisor.ide.dc.xmlDAO;

import java.sql.Connection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author team pvpro 20090324
 *
 */
public class ImageTag{

	private String key;
	private String ext;
	private String fileName;
	private String bytes;
	
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

	/**
	 * @return the filename
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFileName(String filename) {
		this.fileName = filename;
	}

	/**
	 * @return the bytes
	 */
	public String getBytes() {
		return bytes;
	}

	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

}
