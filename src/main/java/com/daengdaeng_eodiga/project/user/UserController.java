package com.daengdaeng_eodiga.project.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.user.dto.UserAndPetInfo;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	@GetMapping("/auth")
	public Integer checkAuth(@AuthenticationPrincipal CustomOAuth2User principal) {
		if(principal.getUserDTO() !=null){
			return principal.getUserDTO().getUserid();
		}
		return 0;
	}

	@GetMapping("/user-service/user-pets")
	public UserAndPetInfo fetchUserAndPetsInfo(@PathVariable int userId) {
		UserAndPetInfo response = userService.findUserAndPetsInfo(userId);
		return response;
	}
}
