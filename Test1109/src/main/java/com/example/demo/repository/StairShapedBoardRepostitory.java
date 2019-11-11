package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.StairShapedBoardEntity;

public interface StairShapedBoardRepostitory extends JpaRepository<StairShapedBoardEntity, Integer>{
	StairShapedBoardEntity findById(int id);
	@Query(value="SELECT * FROM (SELECT LEVEL, s.* " + 
			"FROM stair_shaped_board s " +
			"START WITH prntno IS NULL " + 
			"CONNECT BY PRIOR no=prntno " + 
			"ORDER SIBLINGS BY grpno desc) s ", countQuery = "SELECT COUNT(*) FROM stair_shaped_board", nativeQuery = true)
	Page<StairShapedBoardEntity> findAll(Pageable pageable);
}
