package com.javaBorad.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.javaBorad.LoginUserData;
import com.javaBorad.entity.Board;
import com.javaBorad.entity.Comment;
import com.javaBorad.entity.User;
import com.javaBorad.service.BoardService;
import com.javaBorad.service.CommentService;

@Controller
@RequestMapping("/")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping
	public String index(Model model,ModelMap modelMap) {
		model.addAttribute("boards", boardService.findAll());
		String name = "ゲスト";
	    modelMap.addAttribute("username", name);
		return "boards/index";
	}
	
// ぺージネーションの実装。@PageableDefault アノテーションを使用してデフォルト値を指定する事も出来る。
	 @GetMapping("/boards/index")
	  public String login(ModelMap modelMap, Principal principal,Model model) {
	      String name = principal.getName();
	      modelMap.addAttribute("username", name);

//	      Page<Board> page = boardService.findAllBoard(pageable);
//	      model.addAttribute("page", page);
//	      model.addAttribute("boards", page.getContent());
//	      model.addAttribute("url", "/boards/index");

	      List<Board> boards = boardService.findAll();
	      model.addAttribute("boards",boards);
	      return "boards/index";

	  }
	 
	 
	 
		@GetMapping("/boards/search")
		public String serch(@RequestParam String name,Model model,ModelMap modelMap, Principal principal) {
		    String username = principal.getName();//get logged in username
		    modelMap.addAttribute("username", username);
			List<Board> boards = boardService.findByKeyword(name);
			model.addAttribute("boards",boards);
			return "/boards/index";
		}

	@GetMapping("/boards/new")
	public String newBoard(Model model) {
		return "boards/new";
	}
//
	@GetMapping("/boards/{id}")
	// URLのうちパラメータにしたい部分を{}で囲む。囲んだ文字列は@PathVariableの引数で参照できる
	public String show(@PathVariable int id, Model model) {
		Board board = boardService.findOne(id);
 		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());
		//これだと指定した掲示板以外も含めた全ての一覧が表示されてしまう。board_idを指定する必要がある。
//		List<Comment> comments = commentService.findAll();		
		model.addAttribute("comments",commentService.findAll(id));
		//show画面で使う変数を書き足し。
		int board_id = board.getBoard_id();
		model.addAttribute("board_id",board_id);
		return "boards/show";
	}

	@PostMapping
	public String create(@ModelAttribute Board board,Model model,ModelMap modelMap, Principal principal){
		//認証されたユーザーの情報が格納されたAuthenticationクラス。現在のリクエストに紐づく Authentication インスタンスを取得するには SecurityContextHolder.getContext().getAuthentication()
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUserData subject = (LoginUserData) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
        //上記の流れでuse_idを取り出して、強引にセット。もっといいやり方絶対ありそう。
	    board.setUser_id(user_id);
	    //保存。
	    boardService.save(board);
	    //登録後にユーザーの名前をセットして表示。
	    String name = principal.getName();
        //登録後に一覧画面を表示
		model.addAttribute("boards", boardService.findAll());
		//登録後にユーザーの名前をセットして表示。
	    modelMap.addAttribute("username", name);
		return "boards/index";
	}
}
