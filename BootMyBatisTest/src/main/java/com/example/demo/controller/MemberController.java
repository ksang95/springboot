package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.bean.Member;
import com.example.demo.dao.MemberMapper2;
import com.example.demo.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService service;
	//@Autowired
	MemberMapper2 mapper;
	
	@GetMapping({"/","home"})
	public String home() {
		return "home";
	}
	
	@GetMapping("memberList")
	public void memberList(Model model) {
		model.addAttribute("memberList",service.getMemberList());
		//model.addAttribute("memberList",mapper.selectMembers());
	}
	
	@GetMapping("searchMember")
	public void searchMember(@ModelAttribute Member member) {
		
	}
	@GetMapping("searchMemberResult")
	public void searchMemberResult(int no, Model model) {
		model.addAttribute("member",service.getMember(no));
	}
	@GetMapping("joinMember")
	public void joinMember(@ModelAttribute Member member) {
	}
	@GetMapping("joinMemberResult")
	public void joinMemberResult(@ModelAttribute Member member, Model model) {
		service.insertMember(member);
	}
	
}
