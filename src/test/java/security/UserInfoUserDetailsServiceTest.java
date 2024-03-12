package security;

import com.edstem.taxiBookingAndBillingSystem.model.User;
import com.edstem.taxiBookingAndBillingSystem.repository.UserRepository;
import com.edstem.taxiBookingAndBillingSystem.security.UserInfoUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserInfoUserDetailsService.class} )
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
public class UserInfoUserDetailsServiceTest {
    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setAccountBalance(10.0d);
        user.setEmail("a@gmail.com");
        user.setId(1L);
        user.setName("Name");
        user.setPassword("hey");
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);
        UserDetails actualLoadUserByUsernameResult = userInfoUserDetailsService.loadUserByUsername("a@gmail.com");

        verify(userRepository).findByEmail(eq("a@gmail.com"));
        assertEquals("hey", actualLoadUserByUsernameResult.getPassword());
        assertEquals("a@gmail.com", actualLoadUserByUsernameResult.getUsername());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(userRepository.findByEmail(Mockito.<String>any())).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(UsernameNotFoundException.class,
                () -> userInfoUserDetailsService.loadUserByUsername("a@gmail.com"));
        verify(userRepository).findByEmail(eq("a@gmail.com"));
    }
}
