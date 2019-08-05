package com.alcatel.wifilink.root.bean;

public class SystemInfo extends BaseResult{
	private String DeviceName = new String();
	private String IMEI = new String();
	private String HwVersion = new String();
	private String SwVersion = new String();
	private String HttpApiVersion = new String();
	private String WebUiVersion = new String();
	private String MacAddress = new String();
	
	@Override
	public void clear(){
		MacAddress = "";
		DeviceName = "";
		IMEI = "";
		HwVersion = "";
		SwVersion = "";
		HttpApiVersion = "";
		WebUiVersion = "";
	}
	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String strMacAddress) {
		MacAddress = strMacAddress;
	}
	public String getDeviceName() {
		return DeviceName;
	}
	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getHwVersion() {
		return HwVersion;
	}
	public void setHwVersion(String hwVersion) {
		HwVersion = hwVersion;
	}
	public String getSwVersion() {
		return SwVersion;
	}
	public void setSwVersion(String swVersion) {
		SwVersion = swVersion;
	}
	public String getHttpApiVersion() {
		return HttpApiVersion;
	}
	public void setHttpApiVersion(String httpApiVersion) {
		HttpApiVersion = httpApiVersion;
	}
	public String getWebUiVersion() {
		return WebUiVersion;
	}
	public void setWebUiVersion(String webUiVersion) {
		WebUiVersion = webUiVersion;
	}
}
