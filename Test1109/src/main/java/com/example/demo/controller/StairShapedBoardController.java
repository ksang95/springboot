package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import com.example.demo.bean.FileVO;
import com.example.demo.bean.StairPagingVO;
import com.example.demo.bean.StairShapedBoardVO;
import com.example.demo.mapper.FileMapper;
import com.example.demo.repository.StairShapedBoardRepostitory;
import com.example.demo.service.StairShapedBoardService;

@Controller
public class StairShapedBoardController {
	@Autowired
	StairShapedBoardRepostitory repository;
	@Autowired
	StairShapedBoardService service;
	@Autowired
	FileMapper mapper;
	private static final List<String> EXTENSIONS_IMAGE = Arrays.asList("bmp", "gif", "jpg", "png", "jpeg");


	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect();
	}
	
	@GetMapping("stairShapedBoardList")
	public void stairShapedBoardList(@RequestParam(defaultValue = "1") int nowPage, String category, String search,
			Model model, @PageableDefault(direction = Direction.DESC, size = 10) Pageable pageable) {
		StairPagingVO vo;
		if (category != null) {
			vo = new StairPagingVO(nowPage, service.countBoardLk(category, search));
			model.addAttribute("boardList", service.getBoardListLk(vo.getStart(), vo.getEnd(), category, search));
			model.addAttribute("searching", "searching");
		} else {
			vo = new StairPagingVO(nowPage, service.countBoard());
			model.addAttribute("boardList", repository.findAll(pageable));
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
		StairShapedBoardVO board = repository.findById(no).toVO();
		System.out.println(board);
		String grpno = Integer.toString(board.getGrpno());
		StairPagingVO vo = new StairPagingVO(smallNowPage, service.countBoardEq("grpno", grpno));
		vo.setCntPerPage(5);
		vo.pageSetting();
		List<FileVO> files = mapper.selectFilesOfBoard(no);
		List<String> imgs=new ArrayList<String>();
		for (FileVO file : files) {
			// String url = request.getServletContext().getRealPath(file.getFileUrl());
			String fileName = file.getFileName();
			if (EXTENSIONS_IMAGE.contains(FilenameUtils.getExtension(fileName))) {
				imgs.add(file.getFileUrl() + fileName);
			}
		}
		if (!files.isEmpty()) {
			model.addAttribute("imgs", imgs);
			model.addAttribute("files", files);
		}
		;
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

	@PostMapping("stairShapedBoardInsert1")
	public String stairShapedBoardInsert1(@ModelAttribute("board") StairShapedBoardVO board,
			@RequestPart(required = false) MultipartFile[] files, HttpServletRequest request, Model model) {
		int no = service.insertBoard(board);
		// System.out.println(request.getServletContext().getRealPath("/upload/"));
		for (MultipartFile file : files) {
			String url = request.getServletContext().getRealPath("/upload/");
			String fileName = file.getOriginalFilename();
			String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase(); // 확장자명 가져오기
			String destinationFileName = null;
			File destinationFile = null;
			do {
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension;// 파일명 중복 처리
				destinationFile = new File(url, destinationFileName);
			} while (destinationFile.exists()); // 같은 이름의 파일 있으면 이름 새로 만들기

			destinationFile.getParentFile().mkdirs(); // 파일의 경로 만들기

			try {
				file.transferTo(destinationFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			FileVO fileVO = new FileVO();
			fileVO.setBno(no);
			fileVO.setFileName(destinationFileName);
			fileVO.setFileOriName(file.getOriginalFilename());
			fileVO.setFileUrl("/upload/");
			mapper.insertFile(fileVO);

		}

		return "redirect:stairShapedBoard?no=" + no;
	}

	@PostMapping("stairShapedBoardInsert")
	public String stairShapedBoardInsert(@ModelAttribute("board") StairShapedBoardVO board, MultipartHttpServletRequest request, Model model) {
		int no = service.insertBoard(board);
		// System.out.println(request.getServletContext().getRealPath("/upload/"));
		for (MultipartFile file : request.getFiles("files")) {
			String url = request.getServletContext().getRealPath("/upload/");
			String fileName = file.getOriginalFilename();
			String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase(); // 확장자명 가져오기
			String destinationFileName = null;
			File destinationFile = null;
			do {
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension;// 파일명 중복 처리
				destinationFile = new File(url, destinationFileName);
			} while (destinationFile.exists()); // 같은 이름의 파일 있으면 이름 새로 만들기

			destinationFile.getParentFile().mkdirs(); // 파일의 경로 만들기

			try {
				file.transferTo(destinationFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			FileVO fileVO = new FileVO();
			fileVO.setBno(no);
			fileVO.setFileName(destinationFileName);
			fileVO.setFileOriName(file.getOriginalFilename());
			fileVO.setFileUrl("/upload/");
			mapper.insertFile(fileVO);

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
