package com.rab3tech.vo;

import java.sql.Timestamp;

import com.rab3tech.dao.entity.Login;

public class CustomerQuestionAnswerVO {
	private int id;
	private String question;
	private String answer;
	private Login login;
	private Timestamp doe;
	private Timestamp dom;
	
	
	public CustomerQuestionAnswerVO() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	public Timestamp getDoe() {
		return doe;
	}
	public void setDoe(Timestamp doe) {
		this.doe = doe;
	}
	public Timestamp getDom() {
		return dom;
	}
	public void setDom(Timestamp dom) {
		this.dom = dom;
	}
	public CustomerQuestionAnswerVO(int id, String question, String answer, Login login, Timestamp doe, Timestamp dom) {
		super();
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.login = login;
		this.doe = doe;
		this.dom = dom;
	}
	
	
}
