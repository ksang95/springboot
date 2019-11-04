package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bean.FileVO;
import com.example.demo.bean.StairPagingVO;
import com.example.demo.bean.StairShapedBoardVO;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.StairShapedBoardService;

@Controller
public class StairShapedBoardController {

	@Autowired
	StairShapedBoardService service;
	@Autowired
	FileMapper mapper;
	private static final List<String> EXTENSIONS_IMAGE = Arrays.asList("bmp", "gif", "jpg", "png", "jpeg");

	@GetMapping("stairShapedBoardList")
	public void stairShapedBoardList(@RequestParam(defaultValue = "1") int nowPage, String category, String search,
			Model model) {
		StairPagingVO vo;
		if (category != null) {
			vo = new StairPagingVO(nowPage, service.countBoardLk(category, search));
			model.addAttribute("boardList", service.getBoardListLk(vo.getStart(), vo.getEnd(), category, search));
			model.addAttribute("searching", "searching");
		} else {
			vo = new StairPagingVO(nowPage, service.countBoard());
			model.addAttribute("boardList", service.getBoardList(vo.getStart(), vo.getEnd()));
		}
		model.addAttribute("paging", vo);
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("category", category);
		model.addAttribute("search", search);
	}

	@RequestMapping("stairShapedBoard")
	public void stairShapedBoard(@RequestParam(defaultValue = "1") int nowPage,
			@RequestParam(defaultValue = "1") int smallNowPage, String category, String search, int no,
			HttpServletRequest request, Model model) {
		service.increaseHit(no);
		StairShapedBoardVO board = service.getBoard(no);
		String grpno = Integer.toString(board.getGrpno());
		StairPagingVO vo = new StairPagingVO(smallNowPage, service.countBoardEq("grpno", grpno));
		vo.setCntPerPage(5);
		vo.pageSetting();
		FileVO file = mapper.selectFilesOfBoard(no);
		if (file != null) {
			// String url = request.getServletContext().getRealPath(file.getFileUrl());
			String fileName = file.getFileName();
			if (EXTENSIONS_IMAGE.contains(FilenameUtils.getExtension(fileName)))
				model.addAttribute("img", file.getFileUrl() + fileName);
			model.addAttribute("file", file);
		}
		model.addAttribute("boardList", service.getBoardListEq(vo.getStart(), vo.getEnd(), "grpno", grpno));
		model.addAttribute("paging", vo);
		model.addAttribute("grpno", board.getGrpno());
		model.addAttribute("thisBoard", board);
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("smallNowPage", smallNowPage);
		model.addAttribute("category", category);
		model.addAttribute("search", search);
		model.addAttribute("beside", service.besideBoard(no));
		model.addAttribute("total", service.countBoard());
	}

	@GetMapping("stairShapedBoardInsertForm")
	public void stairShapedBoardInsertForm(@ModelAttribute("board") StairShapedBoardVO board,
			@RequestParam(defaultValue = "0") int prntno) {
		board.setPrntno(prntno);
	}

	@PostMapping("stairShapedBoardInsert")
	public String stairShapedBoardInsert(@ModelAttribute("board") StairShapedBoardVO board,
			@RequestPart(required = false) MultipartFile files, HttpServletRequest request, Model model) {
		// 업로드 파일 하나일때
		int no = service.insertBoard(board);
		//System.out.println(request.getServletContext().getRealPath("/upload/"));
		if (!files.isEmpty()) {
			String url = request.getServletContext().getRealPath("/upload/");
			String fileName = files.getOriginalFilename();
			String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase(); // 확장자명 가져오기
			String destinationFileName = null;
			File destinationFile = null;
			do {
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension;// 파일명 중복 처리
				destinationFile = new File(url, destinationFileName);
			} while (destinationFile.exists()); // 같은 이름의 파일 있으면 이름 새로 만들기

			destinationFile.getParentFile().mkdirs(); // 파일의 경로 만들기

			try {
				files.transferTo(destinationFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			FileVO file = new FileVO();
			file.setBno(no);
			file.setFileName(destinationFileName);
			file.setFileOriName(files.getOriginalFilename());
			file.setFileUrl("/upload/");
			mapper.insertFile(file);
		}
		return "redirect:stairShapedBoard?no=" + no;
	}

	@GetMapping("stairShapedBoardUpdateForm")
	public void stairShapedBoardUpdateForm(int no, Model model) {
		model.addAttribute("board", service.getBoard(no));
	}

	@PostMapping("stairShapedBoardUpdate")
	public String stairShapedBoardUpdate(@ModelAttribute("board") StairShapedBoardVO board, Model model) {
		service.updateBoard(board);
		return "redirect:stairShapedBoard?no=" + board.getNo();
	}

	@GetMapping("stairShapedBoardDelete")
	public String stairShapedBoardDelete(int no, Model model) {
		service.deleteBoard(no);
		return "redirect:stairShapedBoardList";
	}

	@GetMapping("stairShapedBoardSmallList")
	public String stairShapedBoardSmallList(@RequestParam(defaultValue = "1") int nowPage,
			@RequestParam(defaultValue = "1") int smallNowPage, String grpno, int no, Model model) {
		StairPagingVO vo = new StairPagingVO(smallNowPage, service.countBoardEq("grpno", grpno));
		vo.setCntPerPage(5);
		vo.pageSetting();
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("no", no);
		model.addAttribute("boardList", service.getBoardListEq(vo.getStart(), vo.getEnd(), "grpno", grpno));
		model.addAttribute("paging", vo);
		return "stairShapedBoardListFrag :: boardListTable";
	}

}
