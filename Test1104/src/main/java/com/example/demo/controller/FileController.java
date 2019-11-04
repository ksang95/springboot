package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.bean.FileVO;
import com.example.demo.mapper.FileMapper;

@Controller
public class FileController {

	@Autowired
	FileMapper mapper;

	@RequestMapping("/fileDown/{fno}")
	private void fileDown(@PathVariable int fno, HttpServletRequest request, HttpServletResponse response) {
		FileVO fileVO = mapper.selectFile(fno);
		String savePath = request.getServletContext().getRealPath(fileVO.getFileUrl());
		String fileName = fileVO.getFileName();
		String oriFileName = fileVO.getFileOriName();
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		File file = null;

		file = new File(savePath, fileName);

		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String client = request.getHeader("User-Agent"); // 사용자의 브라우저 확인

		// 파일 다운로드 헤더 지정
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Description", "HTML Generated Data"); // name 헤더에 원하는 value 담기
		try {
			//System.out.println(java.net.URLEncoder.encode(oriFileName, "UTF-8"));
			//System.out.println(java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ "));
			if (client.indexOf("MSIE") != -1) {// IE
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");

			} else if (client.indexOf("Trident") != -1) {// IE 11 이상.
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
			} else {// 한글 파일명 처리
				//System.out.println(new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
				response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-Length", "" + file.length());

		try {
			os = new BufferedOutputStream(response.getOutputStream());
			int i = 0;
			while ((i = is.read()) != -1) {
				os.write(i);
			}
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
					os=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
