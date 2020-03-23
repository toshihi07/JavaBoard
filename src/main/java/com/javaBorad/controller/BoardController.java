package com.javaBorad.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	    
	    PageRequest pageable = PageRequest.of(0, 5);
	    Page<Board> boardPage = boardService.getPaginatedboards(pageable);
	      int totalPages = boardPage.getTotalPages();
	      if(totalPages > 0) {
	    	  //rangeClosedは「開始値以上、終了値以下の連続する整数を返す」
	            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }    
        model.addAttribute("activeBoardList", true);
        model.addAttribute("boardList", boardPage.getContent());
        return "boards/index";
	}
	
	@GetMapping("{page}")
	public String boardIndex(Model model,ModelMap modelMap,@PathVariable("page") int page) {
		model.addAttribute("boards", boardService.findAll());
		String name = "ゲスト";
	    modelMap.addAttribute("username", name);
	    
	    PageRequest pageable = PageRequest.of(page - 1, 5);
	    Page<Board> boardPage = boardService.getPaginatedboards(pageable);
	      int totalPages = boardPage.getTotalPages();
	      if(totalPages > 0) {
	    	  //rangeClosedは「開始値以上、終了値以下の連続する整数を返す」
	            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }    
        model.addAttribute("activeBoardList", true);
        model.addAttribute("boardList", boardPage.getContent());
        return "boards/index";
	}
	
	 @GetMapping("/boards/index/{page}")
	  public String login(ModelMap modelMap, Principal principal,Model model,@PathVariable("page") int page) {
	      String name = principal.getName();
	      modelMap.addAttribute("username", name);
	      
	      
	      
	      PageRequest pageable = PageRequest.of(page - 1, 5,Sort.by("name").descending());
	      Page<Board> boardPage = boardService.getPaginatedboards(pageable);
	      
	      int totalPages = boardPage.getTotalPages();
	      if(totalPages > 0) {
	    	  //rangeClosedは「開始値以上、終了値以下の連続する整数を返す」
	            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	      	int indexNumber = (page - 1) * 5 + 1;
	      	int nextPage = page + 1;
	      	int previousPage = page - 1;

	        model.addAttribute("activeBoardList", true);
	        model.addAttribute("boardList", boardPage.getContent());
	        model.addAttribute("lastPage", totalPages);
	        model.addAttribute("indexNumber", indexNumber);
	        model.addAttribute("pageCreteria", page);
	        model.addAttribute("nextPage", nextPage);
	        model.addAttribute("previousPage", previousPage);


	      List<Board> boards = boardService.findAll();
	      
	      model.addAttribute("boards",boards);
	      
	      
	      return "boards/loginIndex";
	  }
	 
	 
	 
		@GetMapping("/boards/search/1")
		public String search(@RequestParam String name,Model model,ModelMap modelMap, Principal principal) {
		    String username = principal.getName();//get logged in username
		    modelMap.addAttribute("username", username);
		    
		      PageRequest pageable = PageRequest.of(0, 5);
		      
		      //ここで値を取れていない
		      Page<Board> boardPage = boardService.findByName(name, pageable);
		      int totalPages = boardPage.getTotalPages();
		      if(totalPages > 0) {
		    	  
		    	  //rangeClosedは「開始値以上、終了値以下の連続する整数を返す」。boxedはStreamに対してintプリミティブ特殊化を行う。
		            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
		            model.addAttribute("pageNumbers", pageNumbers);
		        }
		        model.addAttribute("activeBoardList", true);
		        model.addAttribute("boardList", boardPage.getContent());
		        
			return "/boards/loginIndex";
		}

	@GetMapping("/boards/new")
	public String newBoard(Model model) {
		return "boards/new";
	}
//
	@GetMapping("/boards/{id}")
	// URLのうちパラメータにしたい部分を{}で囲む。囲んだ文字列は@PathVariableの引数で参照できる
	public String show(@PathVariable int id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUserData subject = (LoginUserData) auth.getPrincipal();
        User user = subject.getUser();
        int user_id = user.getUserId();
 		model.addAttribute("loginUser_id", user_id);
		Board board = boardService.findOne(id);
 		model.addAttribute("board", board);
		model.addAttribute("boardName", board.getName());		
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
        String username = user.getUsername();
        //上記の流れでuse_idを取り出して、強引にセット。もっといいやり方絶対ありそう。
	    board.setUser_id(user_id);
	    board.setUsername(username);
	    //保存。
	    boardService.save(board);
	    //登録後にユーザーの名前をセットして表示。
	    String name = principal.getName();
        //登録後に一覧画面を表示
		model.addAttribute("boards", boardService.findAll());
		//登録後にユーザーの名前をセットして表示。
	    modelMap.addAttribute("username", name);
	    
	    //ぺージネーションの処理
	      PageRequest pageable = PageRequest.of(0, 5);
	      Page<Board> boardPage = boardService.getPaginatedboards(pageable);
	      int totalPages = boardPage.getTotalPages();
	      if(totalPages > 0) {
	    	  //rangeClosedは「開始値以上、終了値以下の連続する整数を返す」
	            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        model.addAttribute("activeBoardList", true);
	        model.addAttribute("boardList", boardPage.getContent());
		return "redirect:/boards/index/1";
	}
}
