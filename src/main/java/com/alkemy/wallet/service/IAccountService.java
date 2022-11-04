package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.ResponseAccountDto;
import com.alkemy.wallet.model.Account;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<ResponseAccountDto> findAllByUser(Long id);

    Optional<Account> findById(Long id);

    ResponseAccountDto updateAccount(Long id, Double transactionLimit, Authentication authentication);
}
