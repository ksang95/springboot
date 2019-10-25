package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.bean.Member;

@Service
public interface MemberMapper {
	List<Member> getMemberList();
	Member getMember(int no);
	int insertMember(Member member);
}

