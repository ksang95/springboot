package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.vo.Customer;
import com.example.demo.vo.Product;

@Controller
public class SimpleFormatUseController {
	@GetMapping("test1")
	public void test1(@ModelAttribute Product product, Model model) {
		product.setDescription("검정 구두");
		product.setPrice(200);
		product.setAvailableFrom(new Date(191023));
		model.addAttribute("html", "<h2>Product information</h2>");
	}

	@GetMapping("test2")
	public void test2(@ModelAttribute ArrayList<Product> productList) {
		Product product1 = new Product();
		product1.setDescription("검정 구두");
		product1.setPrice(200);
		product1.setAvailableFrom(new Date(191023));
		Product product2 = new Product();
		product2.setDescription("아이보리 원피스");
		product2.setPrice(250);
		product2.setAvailableFrom(new Date(191020));

		productList.add(product1);
		productList.add(product2);

	}

	@GetMapping("test3")
	public void test3(@ModelAttribute ArrayList<Product> productList) {
		Product product1 = new Product();
		product1.setDescription("검정 구두");
		product1.setPrice(99);
		product1.setAvailableFrom(new Date(191023));
		Product product2 = new Product();
		product2.setDescription("아이보리 원피스");
		product2.setPrice(150);
		product2.setAvailableFrom(new Date(191020));

		productList.add(product1);
		productList.add(product2);

	}

	@GetMapping("test4")
	public void test4(@ModelAttribute ArrayList<Customer> customerList) {
		Customer[] customers= {
		new Customer("상희", "김", "FEMALE", "CREDIT_CARD", 1000),
		new Customer("민희", "강", "FEMALE", "CREDIT_CARD", 20000),
		new Customer("윤주", "성", "FEMALE", "CASH", 20000),
		new Customer("은석", "신", "MALE", "CASH", 1000)};
		for(Customer c:customers) {
			customerList.add(c);
		}
	}
	@GetMapping("test5")
	public void test5(@ModelAttribute ArrayList<Customer> customerList) {
		Customer[] customers= {
				new Customer("상희", "김", "FEMALE", "CREDIT_CARD", 1000),
				new Customer("민희", "강", "FEMALE", "CREDIT_CARD", 20000),
				new Customer("윤주", "성", "FEMALE", "CASH", 20000),
				new Customer("은석", "신", "MALE", "CASH", 1000)};
		for(Customer c:customers) {
			customerList.add(c);
		}
	}
	
	@GetMapping("test6")
	public void test6() {
		//필요한게 복잡해서 미완성
	}
	
	@GetMapping("test7")
	public void test7(Model model) {
		model.addAttribute("customerName","PETER");
	}
}
