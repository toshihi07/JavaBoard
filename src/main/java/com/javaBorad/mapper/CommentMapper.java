package com.javaBorad.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;


@Mapper
public interface CommentMapper {
	//テーブル結合、3つ。where区で条件指定。
    @Select("select * from comments INNER JOIN boards ON comments.board_id = boards.board_id INNER JOIN users ON comments.user_id = users.user_id WHERE boards.board_id = #{id}")
    List<Comment> findAll(int id);
    
    @Select("select * from comments INNER JOIN boards ON comments.board_id = boards.board_id INNER JOIN users ON comments.user_id = users.user_id WHERE boards.board_id = ${id} AND title LIKE '%${title}%'")
    List<Comment> findByTitle(int id, String title);
    
    @Select("select * from comments INNER JOIN boards ON comments.board_id = boards.board_id INNER JOIN users ON comments.user_id = users.user_id WHERE boards.board_id = ${id} AND text LIKE '%${text}%'")
    List<Comment> findByText(int id, String text);
    
    @Select("select * from comments where comment_id = #{comment_id}")
    Comment findOne(int id);
    
    @Insert("insert into comments (title,text,board_id,user_id) values (#{title},#{text},#{board_id},#{user_id})")
    void save(Comment comment);
    
    @Update("insert into s (title,text) values (#{title},#{text})")
    void update(Comment comment);
    
    @Delete("delete from comments where comment_id = #{comment_id}")
    void delete(int id);
    
}
