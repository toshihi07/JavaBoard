package com.javaBorad.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaBorad.LoginUserData;
import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;
import com.javaBorad.entity.User;
import com.javaBorad.service.BoardService;
import com.javaBorad.service.CommentService;

@Controller
@RequestMapping("/boards/{id}/comments")
public class CommentController {
	
@Autowired
CommentService commentService;

@Autowired
private BoardService boardService;

@GetMapping("new") 
public String newComment(@PathVariable ("id") int board_id,Model model) {
	//board_idからインスタンスを特定して、そのインスタンスのboardNameを取得したい。
	int id = board_id;
	model.addAttribute("board_id",id);
	return "comments/new";
}

@GetMapping("{id}/edit")
public String edit(@PathVariable int id, Model model) {
	Comment comment = commentService.findOne(id);
	int board_id = comment.getBoard_id();
    model.addAttribute("comment", comment);
    model.addAttribute("board_id", board_id);
    model.addAttribute("comment_id", id);
    return "comments/edit";
}

@GetMapping("searchTitle")
public String searchTitle(@PathVariable ("id") int board_id,@RequestParam String title,Model model,ModelMap modelMap, Principal principal) {
	
    String username = principal.getName();//get logged in username
    modelMap.addAttribute("username", username);
	List<Comment> comments = commentService.findByTitle(board_id,title);
	
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LoginUserData subject = (LoginUserData) auth.getPrincipal();
    User user = subject.getUser();
    int user_id = user.getUserId();
	model.addAttribute("loginUser_id",user_id);
	
	Board board = boardService.findOne(board_id);
	model.addAttribute("board", board);
	model.addAttribute("boardName", board.getName());
	model.addAttribute("comments",comments);
	model.addAttribute("board_id",board_id);
	return "boards/show";
}

@GetMapping("searchText")
public String searchText(@PathVariable ("id") int board_id,@RequestParam String text,Model model,ModelMap modelMap, Principal principal) {
    String username = principal.getName();
    modelMap.addAttribute("username", username);
	List<Comment> comments = commentService.findByText(board_id,text);	
	
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LoginUserData subject = (LoginUserData) auth.getPrincipal();
    User user = subject.getUser();
    int user_id = user.getUserId();
	model.addAttribute("loginUser_id",user_id);
	
	Board board = boardService.findOne(board_id);
	model.addAttribute("board", board);
	model.addAttribute("boardName", board.getName());
	model.addAttribute("comments",comments);
	model.addAttribute("board_id",board_id);
	return "boards/show";
}

//th:actionからの送信はgetとpostの両方のmappingを書いておかないとエラーになる。
@PostMapping("create") 
//PathVariableで囲むことで、リクエストされた値をメソッド内で引数として参照できる。
public String create(@PathVariable ("id") int board_id, @ModelAttribute Comment comment, Model model) {
	//ここはあとでリファクタリング。重複だらけでコードが見にくすぎる。	
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    LoginUserData subject = (LoginUserData) auth.getPrincipal();
    User user = subject.getUser();
    int user_id = user.getUserId();	
    String userName = user.getUsername();
    comment.setUser_id(user_id);
    //ここも重複コード。
	Board board = boardService.findOne(board_id);
    comment.setUser_id(user_id);
    comment.setUsername(userName);
    comment.setBoard_id(board_id);
	model.addAttribute("board", board);
	model.addAttribute("boardName", board.getName());
	model.addAttribute("comments",commentService.findAll(board_id));
	//投稿した内容をmodelにセット。
	model.addAttribute("comment",comment);
	model.addAttribute("id",board_id);
	commentService.save(comment);
	//掲示板のshow画面、コメントの一覧画面に戻る。
//	return "redirect: /";
    String sId = String.valueOf(board_id);
    return "redirect:/boards/" + sId;
}

//ユーザーIDのセット
@PostMapping("{comment_id}/update")
public String update(@PathVariable("comment_id") int comment_id, @PathVariable("id") int id,@ModelAttribute Comment comment) {
    comment.setComment_id(comment_id);
    commentService.update(comment);
    String sId = String.valueOf(id);
    return "redirect:/boards/" + sId;
}

@GetMapping("{comment_id}/update")
public String update() {
    return "boards/index";
}

@PostMapping("{comment_id}")
public String destroy(@PathVariable("comment_id") int comment_id,@PathVariable("id") int id) {
    commentService.delete(comment_id);
    String sId = String.valueOf(id);
    //文字列に変換する
    return "redirect:/boards/" + sId;
}
	
}
