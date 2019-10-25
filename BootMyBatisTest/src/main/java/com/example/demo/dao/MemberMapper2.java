package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.bean.Member;

@Mapper
public interface MemberMapper2 {
	@Select("SELECT * FROM member")
	List<Member> selectMembers();

	@Insert("INSERT INTO member VALUES(#{no},#{name})")
	int insertMember(Member member);
}
