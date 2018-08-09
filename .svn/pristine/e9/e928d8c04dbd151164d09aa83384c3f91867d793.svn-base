/**
 * Manages the xml -> db model conversion
 */
package com.carel.supervisor.ide.dc.DbModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author pvpro team 20090422
 */

public class DeviceDb
{
	private int idSite;
	private String code;
	private String manufacturer;
	private String hdVersion;
	private String swVersion;
	private String image;
	private boolean littleEndian;
	private boolean ide;
	private String descriptionKey;
	private DeviceModbusInfoDb modbusInfo;
	
	public void fillInformations(int idSite, String code, String manufacturer, String hdVersion,String swVersion, String image, boolean littleEndian, boolean ide, String descriptionKey, DeviceModbusInfoDb modbusDevice)
	{
		this.idSite = idSite;
		this.code = code;
		this.manufacturer = manufacturer;
		this.hdVersion = hdVersion;
		this.swVersion = swVersion;
		this.image = image;
		this.littleEndian = littleEndian;
		this.ide = ide;
		this.descriptionKey = descriptionKey;
		this.modbusInfo = modbusDevice;
		return;
	}

	public int getIdSite() {
		return idSite;
	}

	public void setIdSite(int idSite) {
		this.idSite = idSite;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getHdVersion() {
		return hdVersion;
	}

	public void setHdVersion(String hdVersion) {
		this.hdVersion = hdVersion;
	}

	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isLittleEndian() {
		return littleEndian;
	}

	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
	}

	public boolean isIde() {
		return ide;
	}

	public void setIde(boolean ide) {
		this.ide = ide;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}

	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}

	public DeviceModbusInfoDb getModbusInfo() {
		return modbusInfo;
	}

	public void setModbusInfo(DeviceModbusInfoDb modbusInfo) {
		this.modbusInfo = modbusInfo;
	}
}