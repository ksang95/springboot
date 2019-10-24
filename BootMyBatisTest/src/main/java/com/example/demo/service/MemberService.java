package com.example.demo.service;

import java.util.List;

import com.example.demo.bean.Member;

public interface MemberService {
	List<Member> getMemberList();
	Member getMember(int no);
	int insertMember(Member member);
}
