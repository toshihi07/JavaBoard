package com.javaBorad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaBorad.mapper.BoardMapper;
import com.javaBorad.entity.Board;

@Service
public class BoardService {
	@Autowired
	BoardMapper boardMapper;

	@Transactional
	public List<Board> findAll() {
		return boardMapper.findAll();
	}

	@Transactional
	public Board findOne(int id) {
		return boardMapper.findOne(id);
	}

	@Transactional
	public void save(Board board) {
		boardMapper.save(board);
	}

	@Transactional
	public List<Board> findByKeyword(String name) {
		return boardMapper.findByKeyword(name);
	}

	public Page<Board> findAllBoard(Pageable pageable){
		return boardMapper.findAllBoard(pageable);
    	}
	}



