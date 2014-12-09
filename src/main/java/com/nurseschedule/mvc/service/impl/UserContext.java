package com.nurseschedule.mvc.service.impl;

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.service.INurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to support the user's session
 * @author Tomasz Morek
 */
public class UserContext implements UserDetailsService {

    /**
     * Nurse service
     */
    @Autowired
    INurseService nurseService;

    /**
     * Load user to authentication
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NurseDto nurse = this.nurseService.findByEmail(email);
        List<GrantedAuthority> grants = new ArrayList<>();
        grants.add(new SimpleGrantedAuthority(nurse.getType()));
        return new User(nurse.getEmail(), nurse.getPassword(), grants);
    }
}
