package com.javaBorad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.javaBorad.entity.User;
import com.javaBorad.mapper.UserRepository;


@Controller
public class UserController{
	
	@Autowired
	PasswordEncoder passwdEncoder;
	
	//この2つのコードの違いがわからない。
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/signup" ,method = RequestMethod.GET)
	//@ModelAttributeは自作したmodeを引数として受け取る場合に使用する。@ModelAttributeはリクエストのたびに@RequestMappingの前に呼ばれ、対応するモデルとオブジェクトがバインドされます。
	public ModelAndView add(@ModelAttribute("formModel")User user,ModelAndView mav){
		mav.setViewName("signup.html");
		return mav;
	}
//addからリクエストpostメソッドでリクエストされた際の処理
//value=でurlのパスを指定。このurlにマッチする場合にメソッドが呼ばれる。
	@RequestMapping(value = "/signup" , method = RequestMethod.POST)
//例外が起こった時に自動でロールバックしてくれる便利なアノテーション。
	@Transactional(readOnly = false)
	//ModelAndViewを返すformメソッド。引数には
	public ModelAndView form(@ModelAttribute("formModel")User user, ModelAndView mav){
		if(userRepository.countByUsername(user.getUsername()) > 0){
			mav.addObject("obj", user.getUsername() + "は既に使用されてるユーザ名です。");
			mav.setViewName("signup");
		} else {
			user.setPassword(passwdEncoder.encode(user.getPassword()));
			user.setEmail(user.getEmail());
			//値取れてはいる
			repository.saveAndFlush(user);
			mav.addObject("user", user.getUsername() + "さん登録完了しました。");
			mav.setViewName("login");
		}
		return mav;
	}
}
