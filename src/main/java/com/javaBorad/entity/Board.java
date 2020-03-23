package com.javaBorad.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="boards")

public class Board {
	@Id
	@GeneratedValue
	@Column(name = "board_id")
	private int board_id;
	@Column(name = "user_id")
	private int user_id;
	@Column(name = "username")
	private String username;
	@Column(name = "name")
	private String name;
	@Column(name = "created_at")
    private Date created_at;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int id) {
		this.board_id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

	public String toFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String str = sdf.format(created_at);
		return str;
	}
}
