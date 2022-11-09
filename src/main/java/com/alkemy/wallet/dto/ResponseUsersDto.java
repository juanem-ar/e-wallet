package com.alkemy.wallet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUsersDto {
	List<ResponseUserDto> userDtos;
	String previousPage;
	String nextPage;
}