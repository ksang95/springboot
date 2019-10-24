package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Member;
import com.example.demo.dao.MemberMapper;

@Service("MemberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberMapper mapper;
	
	@Override
	public List<Member> getMemberList() {
		// TODO Auto-generated method stub
		return mapper.getMemberList();
	}

	@Override
	public Member getMember(int no) {
		// TODO Auto-generated method stub
		return mapper.getMember(no);
	}

	@Override
	public int insertMember(Member member) {
		// TODO Auto-generated method stub
		return mapper.insertMember(member);
	}

}
