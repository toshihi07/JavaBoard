package com.javaBorad.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entityが必要。@Entityを付けたら、@idも必要。
@Entity
public class Comment {
	@Id
	@GeneratedValue
	@Column(name = "comment_id")
	private int comment_id;
	@Column(name = "board_id")
	private int board_id;
	@Column(name = "user_id")
	private int user_id;
	@Column(name = "username")
	private String username;
	@Column(name = "title")
	private String title;
	@Column(name = "text")
	private String text;
	@Column(name = "created_at")
    private Date created_at;
	@Column(name = "updated_at")
    private Date updated_at;
	@Column(name = "image")
	private String image;
	 
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
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
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
