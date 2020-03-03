package com.javaBorad.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import com.javaBorad.entity.Board;


@Mapper
public interface BoardMapper {
	//テーブルの結合。
    @Select("select * from boards INNER JOIN users ON boards.user_id = users.user_id")
    List<Board> findAll();
    
    @Select("select * from boards INNER JOIN users ON boards.user_id = users.user_id WHERE name LIKE '%${name}%'")
    List<Board> findByKeyword(String name);
    
    @Select("select * from boards where board_id = #{board_id}")
    Board findOne(int id);
    
    @Insert("insert into boards (name,user_id) values (#{name},#{user_id})")
    void save(Board board);
    
    @Update("insert into boards (name) values (#{name})")
    void update(Board board);
}
