package com.javaBorad.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.javaBorad.LoginUserData;
import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;
import com.javaBorad.entity.ExcelBuilder;
import com.javaBorad.entity.ImageForm;
import com.javaBorad.entity.User;
import com.javaBorad.service.BoardService;
import com.javaBorad.service.CommentService;


@Controller
@RequestMapping("/boards/{id}/comments")
public class CommentController {
	
@Autowired
private CommentService commentService;

@Autowired
private BoardService boardService;

@GetMapping("new") 
public String newComment(@PathVariable ("id") int board_id,Model model) {
	//board_idからインスタンスを特定して、そのインスタンスのboardNameを取得したい。
	int id = board_id;
	model.addAttribute("board_id",id);
	return "comments/new";
}

@GetMapping("{id}")
public String show(@PathVariable int id, Model model) {
	Comment comment = commentService.findOne(id);
	int board_id = comment.getBoard_id();
	//画像データ処理
	StringBuffer data = new StringBuffer();
	String base64 = comment.getImage();
    comment.setImage(base64);
    data.append("data:image/jpeg;base64,");
    data.append(base64);
    model.addAttribute("base64",base64);
    model.addAttribute("base64image",data.toString());
    model.addAttribute("comment", comment);
    model.addAttribute("board_id", board_id);
    model.addAttribute("comment_id", id);
    return "comments/show";
}

@GetMapping("{id}/edit")
public String edit(@PathVariable int id, Model model) {
	Comment comment = commentService.findOne(id);
	int board_id = comment.getBoard_id();
	
	StringBuffer data = new StringBuffer();
	String base64 = comment.getImage();
    comment.setImage(base64);
    data.append("data:image/jpeg;base64,");
    data.append(base64);
    model.addAttribute("base64",base64);
    model.addAttribute("base64image",data.toString());
	//投稿した内容をmodelにセット。
	model.addAttribute("title",comment.getTitle());
	model.addAttribute("text",comment.getText());
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

@GetMapping("searchWord")
public String searchWord(@PathVariable ("id") int board_id,@RequestParam String word,Model model,ModelMap modelMap, Principal principal) {
    String username = principal.getName();
    modelMap.addAttribute("username", username);
	List<Comment> comments = commentService.findByWord(board_id,word);	
	
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
public String create(@PathVariable ("id") int board_id, @ModelAttribute Comment comment,BindingResult result1,Model model,@ModelAttribute ImageForm imageForm,BindingResult result2) throws Exception {
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
	//画像の保存処理
	//formから送られてきた画像を保存サイズの出力。
    System.out.println(imageForm.getImage());
    //StringBufferクラスをnewする。StringBufferクラスは、Stringと違って文字列操作が可能。
	StringBuffer data = new StringBuffer();
	//
	String base64 = new String(Base64.encodeBase64(imageForm.getImage().getBytes()),"ASCII");
    comment.setImage(base64);
    data.append("data:image/jpeg;base64,");
    data.append(base64);
    model.addAttribute("base64",base64);
    model.addAttribute("base64image",data.toString());
	//投稿した内容をmodelにセット。
	model.addAttribute("title",comment.getTitle());
	model.addAttribute("text",comment.getText());
	model.addAttribute("comment",comment);
	model.addAttribute("id",board_id);
	commentService.save(comment);
	//掲示板のshow画面、コメントの一覧画面に戻る。
//    String sId = String.valueOf(board_id);
    return "/comments/commentResult";
}

//ユーザーIDのセット
@PostMapping("{comment_id}/update")
public String update(@PathVariable("comment_id") int comment_id, @PathVariable("id") int id,@ModelAttribute Comment comment,BindingResult result1,@ModelAttribute ImageForm imageForm,BindingResult result2) throws UnsupportedEncodingException, IOException {
    comment.setComment_id(comment_id);
	//画像の保存処理
    System.out.println(imageForm.getImage());
	String base64 = new String(Base64.encodeBase64(imageForm.getImage().getBytes()),"ASCII");
    comment.setImage(base64);
    commentService.update(comment);
    String sId = String.valueOf(id);
    return "redirect:/boards/" + sId;
}

@PostMapping("/download")
public ModelAndView download(@PathVariable ("id") int board_id) throws Exception {
List<Comment> comments = commentService.findAll(board_id);
return new ModelAndView(new ExcelBuilder(), "comments", comments);
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
