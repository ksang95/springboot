package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.example.demo.bean.FileVO;

@Mapper
public interface FileMapper {
	
	@Select("SELECT * FROM files WHERE fno=#{fno}")
	FileVO selectFile(int fno);
	
	@Select("SELECT * FROM files WHERE bno=#{bno}")
	List<FileVO> selectFilesOfBoard(int bno);
	
	@Insert("INSERT INTO files(fno,bno,fileName,fileOriName,fileUrl) VALUES(#{fno},#{bno},#{fileName},#{fileOriName},#{fileUrl})")
	@SelectKey(statement="SELECT NVL(MAX(fno),0)+1 FROM files", keyProperty="fno", before=true, resultType=int.class)
	int insertFile(FileVO vo);
}
