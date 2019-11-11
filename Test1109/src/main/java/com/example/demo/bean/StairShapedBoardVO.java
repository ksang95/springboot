package com.example.demo.bean;

import java.util.Date;

import com.example.demo.entity.StairShapedBoardEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StairShapedBoardVO {
	int level;
	int no;
	int grpno;
	int prntno;
	String title;
	String writer;
	String content;
	Date regdate;
	int hits;
	
	public StairShapedBoardVO(String title, String writer, String content) {
		super();
		this.title = title;
		this.writer = writer;
		this.content = content;
	}

	public StairShapedBoardEntity toEntity() {
		StairShapedBoardEntity entity=new StairShapedBoardEntity();
		entity.setNo(prntno);
		return StairShapedBoardEntity.builder().no(no).grpno(grpno).hits(hits).regdate(regdate).title(title).writer(writer).content(content).stairShapedBoardEntity(entity).build();
	}

	
	
	
	
}
