package com.javaBorad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;
import com.javaBorad.mapper.CommentMapper;
import com.javaBorad.mapper.CommentRepository;


@Service
public class CommentService {
	@Autowired
	CommentMapper commentMapper;
	
	@Autowired
	CommentRepository commentRepository;
	
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
        return commentMapper.findByText(id,text);
    }
    
    @Transactional
    public List<Comment> findByWord(int id,String text) {
        return commentMapper.findByWord(id,text);
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
        commentMapper.update(comment);
    }

    @Transactional
    public void delete(int id) {
        commentMapper.delete(id);
    }
   
    @Transactional
	public Page<Comment> getPaginatedcomments(int id, Pageable pageable) {
        return commentRepository.findByBoards(id,pageable);
}
}
