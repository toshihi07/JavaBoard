package com.javaBorad.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.javaBorad.entity.Board;


@Mapper
public interface BoardMapper {
	//テーブルの結合。
    @Select("select * from boards INNER JOIN user ON boards.user_id = user.user_id")
    List<Board> findAll();
    
    //全掲示板検索。page
    @Select("select * from boards INNER JOIN user ON boards.user_id = user.user_id")
    Page<Board> findAllBoard(Pageable pageable);
    
    //page検索
    @Query("SELECT a FROM boards a WHERE a.name LIKE %:freeWord% ESCAPE '~' OR a.overview LIKE %:freeWord% ESCAPE '~'")
    Page<Board> findPageByFreeWord(@Param("freeWord") String word, Pageable pageable); // (4)ページ検索に必要な情報( Pageable )をRepositoryのQueryメソッドの引数として受け取る。返り値の型は、Page<Entity> とする。
    
    @Select("select * from boards INNER JOIN user ON boards.user_id = user.user_id WHERE name LIKE '%${name}%'")
    List<Board> findByKeyword(String name);
    
    @Select("select * from boards INNER JOIN user ON boards.user_id = user.user_id WHERE name LIKE '%${name}%'")
    Page<Board> findByName(String name,Pageable pageable);
    
    @Select("select * from boards where board_id = #{board_id}")
    Board findOne(int id);
    
    @Insert("insert into boards (name,user_id,username) values (#{name},#{user_id},#{username})")
    void save(Board board);
    
    @Update("insert into boards (name) values (#{name})")
    void update(Board board);
}
