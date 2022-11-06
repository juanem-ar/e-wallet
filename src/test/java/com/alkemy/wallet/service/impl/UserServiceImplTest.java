package com.alkemy.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.mapper.IuserMapper;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {
	@Mock
	private IUserRepository userRepository;

	@Autowired
	@Spy
	private IuserMapper userMapper;

	@InjectMocks
	private UserServiceImpl userService;


	@Test
	void findAllUsers_ListWithTwoUsers_ListWithTwoDtos() {
		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());

		when(userRepository.findAll()).thenReturn(users);

		List<ResponseUserDto> result = userService.findAllUsers();

		assertEquals(2, result.size());
	}

}