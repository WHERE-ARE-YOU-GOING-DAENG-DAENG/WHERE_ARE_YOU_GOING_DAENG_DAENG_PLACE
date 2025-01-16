package com.daengdaeng_eodiga.project.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
import com.daengdaeng_eodiga.project.user.dto.PetInfo;
import com.daengdaeng_eodiga.project.user.dto.UserAndPetInfo;
import com.daengdaeng_eodiga.project.user.dto.UserPets;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User findUser(int userId) {
		return userRepository.findById( userId).orElseThrow(()->new UserNotFoundException());
	}
	public User findUserByemailAndProvider(String email, OauthProvider provider) {
		return userRepository.findByEmailAndOauthProviderAndDeletedAtIsNull(email,provider).orElseThrow(()->new UserNotFoundException());
	}

	public List<UserPets> findUsersByUserIds(List<Integer> userIds) {
		return userRepository.findByIdInUserIds(userIds);
	}

	public UserAndPetInfo findUserAndPetsInfo(int userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException());
		List<PetInfo> pets = user.getPets().stream().map(pet->new PetInfo(pet.getPetId(), pet.getName(), pet.getImage())).toList();
		return new UserAndPetInfo(user.getUserId(), user.getNickname(), pets);
	}
}
