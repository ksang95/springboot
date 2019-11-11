package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.example.demo.bean.StairShapedBoardVO;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name="stair_shaped_board")
public class StairShapedBoardEntity {
	@Id
	int no;
	int grpno;
	@ManyToOne
	@JoinColumn(name="prntno")
	StairShapedBoardEntity stairShapedBoardEntity;
	String title;
	String writer;
	String content;
	@Temporal(TemporalType.DATE)
	Date regdate;
	int hits;
	@Transient
	int level;
	
	public StairShapedBoardEntity() {}
	
	@Builder
	public StairShapedBoardEntity(int no, int grpno, StairShapedBoardEntity stairShapedBoardEntity, String title,
			String writer, String content, Date regdate, int hits) {
		super();
		this.no = no;
		this.grpno = grpno;
		this.stairShapedBoardEntity = stairShapedBoardEntity;
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.hits = hits;
	}

	public StairShapedBoardEntity(String title, String writer, String content) {
		super();
		this.title = title;
		this.writer = writer;
		this.content = content;
	}
	
	 @Transient
	 public StairShapedBoardVO toVO() {
		 return new StairShapedBoardVO(level, grpno, grpno, stairShapedBoardEntity.getNo(), title, writer, content, regdate, hits);
	 }
	
}
