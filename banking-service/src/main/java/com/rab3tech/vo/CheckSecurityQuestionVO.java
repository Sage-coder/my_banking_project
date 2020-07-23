package com.rab3tech.vo;

public class CheckSecurityQuestionVO {
	private String currentSecurityQuestion1;
	private String currentSecurityQuestion2;
	private String confirmSecurityQuestion1;
	private String confirmSecurityQuestion2;
	private String loginid;
	
	public String getCurrentSecurityQuestion1() {
		return currentSecurityQuestion1;
	}
	public void setCurrentSecurityQuestion1(String currentSecurityQuestion1) {
		this.currentSecurityQuestion1 = currentSecurityQuestion1;
	}
	public String getCurrentSecurityQuestion2() {
		return currentSecurityQuestion2;
	}
	public void setCurrentSecurityQuestion2(String currentSecurityQuestion2) {
		this.currentSecurityQuestion2 = currentSecurityQuestion2;
	}
	public String getConfirmSecurityQuestion1() {
		return confirmSecurityQuestion1;
	}
	public void setConfirmSecurityQuestion1(String confirmSecurityQuestion1) {
		this.confirmSecurityQuestion1 = confirmSecurityQuestion1;
	}
	public String getConfirmSecurityQuestion2() {
		return confirmSecurityQuestion2;
	}
	public void setConfirmSecurityQuestion2(String confirmSecurityQuestion2) {
		this.confirmSecurityQuestion2 = confirmSecurityQuestion2;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	@Override
	public String toString() {
		return "checkSecurityQuestionVO [currentSecurityQuestion1=" + currentSecurityQuestion1
				+ ", currentSecurityQuestion2=" + currentSecurityQuestion2 + ", confirmSecurityQuestion1="
				+ confirmSecurityQuestion1 + ", confirmSecurityQuestion2=" + confirmSecurityQuestion2 + ", loginid="
				+ loginid + "]";
	}
	
	
}
