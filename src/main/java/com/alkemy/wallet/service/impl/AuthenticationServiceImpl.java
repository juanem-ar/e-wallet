package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.dto.ResponseUserDto;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.mapper.UserMapper;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.config.PasswordEncoder;
import com.alkemy.wallet.security.dto.AuthenticationRequestDto;
import com.alkemy.wallet.security.dto.AuthenticationResponseDto;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    private IUserRepository iUserRepository;
    private IAccountService iAccountService;
    private IUserService iUserService;
    private UserMapper userMapper;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl( IUserService iUserService,IAccountService iAccountService, IUserRepository iUserRepository, UserMapper userMapper, JwtUtils jwtUtils,
                                      AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.iUserService = iUserService;
        this.userMapper = userMapper;
        this.iAccountService = iAccountService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public ResponseUserDto saveUser(ResponseUserDto dto) throws Exception {
        if (!iUserService.existsByEmail(dto.getEmail())) {
            if(dto.getPassword().isEmpty())
                throw new BadRequestException("The Password is empty!");
            User entity = userMapper.toEntity(dto);
            User entitySaved = iUserRepository.save(entity);
            iAccountService.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.ARS));
            iAccountService.addAccount(entitySaved.getEmail(), new CurrencyDto(ECurrency.USD));
            ResponseUserDto responseDto = userMapper.toDto(entitySaved);
            AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(dto.getEmail(), dto.getPassword());
            AuthenticationResponseDto login = login(authenticationRequestDto);
            responseDto.setJwt(login.getJwt());
            return responseDto;
        }else {
            throw new BadRequestException("There is an account with that email adress: " + dto.getEmail());
        }
    }

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto authRequest) throws Exception {
        final String username = authRequest.getEmail();
        final String password = authRequest.getPassword();
        if (username.isEmpty() && password.isEmpty())
            throw new BadRequestException("Insert email and password");
        if (!iUserService.existsByEmail(username))
            throw new BadRequestException("There isn't an account with that email " + authRequest.getEmail());
        if(passwordEncoder.bCryptPasswordEncoder().matches(password, iUserRepository.findByEmail(username).getPassword())){
            final UserDetails userDetails = iUserService.loadUserByUsername(username);
            final String jwt = jwtUtils.generateToken(userDetails);
            return new AuthenticationResponseDto(username,jwt);
            }else{
                    throw new BadRequestException("Incorrect password");
            }
    }
}