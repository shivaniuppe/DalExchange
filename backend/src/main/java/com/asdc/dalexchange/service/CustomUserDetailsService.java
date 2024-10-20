package com.asdc.dalexchange.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
     UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
