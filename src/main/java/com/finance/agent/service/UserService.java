package com.finance.agent.service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finance.agent.Enum.Roles;
import com.finance.agent.Enum.Status;
import com.finance.agent.dtos.UserDto;
import com.finance.agent.entity.UserInfo;
import com.finance.agent.repository.UserRepo;


@Service 
public class UserService implements UserDetailsService{

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserInfo> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserInfo saveUser(UserDto user) {
        String encodedPassword = passwordEncoder.encode(user.password());
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.User);
       Status statusSet = (Status.Pending);        
        UserInfo userInfo = new UserInfo(user.email(), encodedPassword, roles , statusSet);
        return userRepository.save(userInfo);
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        UserInfo usr = userRepository.findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + mail));

                UserDetails user = User.withUsername(mail).
                        password(usr.getPassword()).
                        roles(usr.getRoles().stream().map(Enum::name).toArray(String[]::new)).
                        disabled(usr.getStatus() != Status.Active).
                        build();
        return user;
     
    }

}
