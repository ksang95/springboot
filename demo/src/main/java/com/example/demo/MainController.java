package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.vo.Board;
import com.example.demo.vo.User;

@Controller
public class MainController {
	
	@RequestMapping(value= {"/","/index"})
	public String index(Model model) {
		model.addAttribute("msg","msg");
		return "index";
	}
	
	@RequestMapping("/test")
	public void test(){}
	
	@RequestMapping("/login")
	public void login(@ModelAttribute User user){
		
	}
	
	@RequestMapping("/loginResult")
	public void loginResult(User user, Model model) {
		model.addAttribute("login",user.getId()+"/"+user.getPassword());
	}
	
	@RequestMapping("/board")
	public void board(Model model) {
		List<Board> list=new ArrayList<Board>();
		list.add(new Board(1,"sang","날씨가 좋다","나가고 싶은 날씨"));
		list.add(new Board(2,"jang","시간이 간다","나가고 싶은 시간"));
		list.add(new Board(3,"jang","가야해","빨리 가야해"));
		list.add(new Board(4,"sang","끝내야해","빨리 끝내야해"));
		model.addAttribute("boardList",list);
	}
}
