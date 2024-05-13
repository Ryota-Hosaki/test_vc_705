package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Customer;
import com.example.demo.model.Account;
import com.example.demo.repository.CustomerRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	@Autowired
	HttpSession session;

	@Autowired
	Account account;

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping({ "/", "/login", "logout" })
	public String index(
			@RequestParam(name = "error", defaultValue = "") String error,
			Model model) {
		session.invalidate();

		if (error.equals("notLoggedIn")) {
			model.addAttribute("message", "ログインしてください");
		}

		return "login";
	}

	@PostMapping("/login")
	public String login(
			@RequestParam("name") String name,
			Model model) {
		if (name == null || name.length() == 0) {
			model.addAttribute("message", "名前を入力してください");
			return "login";
		}

		account.setName(name);

		return "redirect:/items";
	}

	@GetMapping("/account")
	public String create() {
		return "accountForm";
	}

	@PostMapping("/account")
	public String store(@RequestParam("name") String name,
			@RequestParam("address") String address,
			@RequestParam("tel") String tel,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			Model model) {
		List<String> errorList = new ArrayList<>();
		if (name.length() == 0) {
			errorList.add("名前は必須です");
		}
		if (address.length() == 0) {
			errorList.add("住所は必須です");
		}
		if (tel.length() == 0) {
			errorList.add("電話番号は必須です");
		}
		if (email.length() == 0) {
			errorList.add("メールアドレスは必須です");
		} else if (email.equals(customerRepository.findByEmail(email).get())) {

		}

		Customer customer = new Customer(name, address, tel, email, password);
		customerRepository.save(customer);
		return "redirect:/";
	}
}
