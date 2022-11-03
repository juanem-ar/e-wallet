package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.EType;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.dto.ResponseTransactionDto;
import com.alkemy.wallet.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("transactions")
@RestController
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/sendArs")
    public ResponseEntity<TransactionDto> sendArs(@RequestHeader("Authorization") String token, @PathVariable Long accountId, Long amount, EType type) {
        return ResponseEntity.ok().body(transactionService.sendArs(token,accountId,amount,type));
    }


    @PostMapping("/deposit")
    public ResponseEntity<ResponseTransactionDto> saveDeposit(
            @RequestBody ResponseTransactionDto deposit){
        ResponseTransactionDto depositCreated = transactionService.save(deposit);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositCreated);
    }
}
