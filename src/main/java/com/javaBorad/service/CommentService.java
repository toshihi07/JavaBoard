package com.javaBorad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;
import com.javaBorad.mapper.CommentMapper;


@Service
public class CommentService {
	@Autowired
	CommentMapper commentMapper;
	
    @Transactional
    public List<Comment> findAll(int id) {
        return commentMapper.findAll(id);
    }
    
    @Transactional
    public List<Comment> findByTitle(int id,String title) {
        return commentMapper.findByTitle(id,title);
    }
    
    @Transactional
    public List<Comment> findByText(int id,String text) {
        return commentMapper.findByTitle(id,text);
    }
    @Transactional
    public Comment findOne(int id) {
        return commentMapper.findOne(id);
    }
    
    @Transactional
    public void save(Comment comment) {
        commentMapper.save(comment);
    } 

    @Transactional
    public void update(Comment comment) {
        commentMapper.save(comment);
    }

    @Transactional
    public void delete(int id) {
        commentMapper.delete(id);
    }
}
