package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {
	private int fno;
	private int bno;
	private String fileName;
	private String fileOriName;
	private String fileUrl;
	
	
}
