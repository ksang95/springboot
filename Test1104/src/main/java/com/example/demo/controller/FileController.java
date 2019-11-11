package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.o7planning.sbdownload.utils.MediaTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.bean.FileVO;
import com.example.demo.mapper.FileMapper;

@Controller
public class FileController {

	@Autowired
	FileMapper mapper;
	@Autowired
	private ServletContext servletContext;

	@RequestMapping("/fileDown2/{fno}")
	private void fileDown2(@PathVariable int fno, HttpServletRequest request, HttpServletResponse response) {
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
				// System.out.println(new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") +
				// "\"");
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
					is = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping("/fileDown/{fno}")
	private ResponseEntity<InputStreamResource> fileDown(@PathVariable int fno, HttpServletRequest request) {
		FileVO fileVO = mapper.selectFile(fno);
		String fileName = fileVO.getFileName();
		String fileOriName = fileVO.getFileOriName();
		String encodedFileName=getEncodedFileName(request, fileOriName);
		String savePath = request.getServletContext().getRealPath(fileVO.getFileUrl());
		MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(servletContext, fileName);
		System.out.println(mediaType);
		File file = new File(savePath, fileName);
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedFileName)
				.contentType(mediaType).contentLength(file.length()).body(resource);
	}

	public static String getEncodedFileName(HttpServletRequest request, String fileName) {
		String userAgent = request.getHeader("User-Agent");
		String encodedFileName = null;
		try {
			if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1) {
				// IE 버전 별 체크 >> Trident/7.0(IE 11), Trident/6.0(IE 10) , Trident/5.0(IE 9) ,
				// Trident/4.0(IE 8)
				// 한글 파일명 깨짐현상을 해결하기 위해 URLEncoder.encode 메소드를 사용하는데,
				// 파일명에 공백이 존재할 경우 URLEncoder.encode 메소드에의해 공백이 '+' 로 변환됩니다.
				// 변환된 '+' 값을 다시 공백으로(%20)으로 replace처리하시면 됩니다.

				encodedFileName = "\"" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + "\"";
				System.out.println(encodedFileName);
			} else if (userAgent.indexOf("Chrome") > -1) {
				encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (userAgent.indexOf("Opera") > -1) {
				encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else if (userAgent.indexOf("Firefox") > -1) {
				encodedFileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			} else {
				System.out.println("Not supported browser");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedFileName;
	}

}
