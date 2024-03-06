package com.edstem.taxiBookingAndBillingSystem.security;

import com.edstem.taxiBookingAndBillingSystem.model.User;
import com.edstem.taxiBookingAndBillingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetailsService;

@Component
@RequiredArgsConstructor
public class UserInfoUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

   @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return new UserInfoUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
