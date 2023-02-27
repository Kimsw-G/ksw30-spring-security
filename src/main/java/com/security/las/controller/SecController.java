package com.security.las.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.security.access.annotation.Secured;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.security.las.config.PrincipalDetails;
import com.security.las.model.User;
import com.security.las.repository.UserRepository;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class SecController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping({ "", "/" })
	public String index() {
		return "home";
	}

	@GetMapping("/user")
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {

		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/join")
	public String join() {
		return "join";
	}

	@PostMapping("/join")
	public String joinProc(User user) {
		System.out.println("회원가입 진행 : " + user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		userRepository.save(user);
		return "redirect:/login";
	}

	@GetMapping("/test/login")
	@ResponseBody
	public String testLogin(Authentication authentication, 
		@AuthenticationPrincipal PrincipalDetails userDetails){
		log.info("/test/login");
		var principalDetails = (PrincipalDetails) authentication.getPrincipal();
		log.info(principalDetails);

		log.info(userDetails.getUsername());
		log.info(userDetails.getUser());
		return "세션정보 확인";
	}
	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String testOAuthLogin(Authentication authentication){
		log.info("/test/oauth/login");
		var oauth2User = (OAuth2User) authentication.getPrincipal();
		log.info(oauth2User.getAttributes());

		return "세션정보 확인";
	}
}

// Spring Security는 Authentication이라는 자체적인 Session을 지님
// 이때 일반 유저는 UserDetails를, OAuth유저는 OAuth2User를 Authentication에 제출하려고 함
// 이를 위해 PrincipalDetails를 생성
// PrincipalDetails에 UserDetails 및 OAuth2User를 상속
// PrincipalDetails를 Authenticaion에 제출