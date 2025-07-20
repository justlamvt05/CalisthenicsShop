    package lamthoncoding.shop.security;

    import jakarta.servlet.http.HttpSession;
    import lamthoncoding.shop.entity.User;
    import lamthoncoding.shop.repository.UserRepository;
    import org.springframework.security.authentication.DisabledException;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;

    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;


    @Service
    public class CustomUserDetails implements UserDetailsService {
        private UserRepository userRepository;

        public CustomUserDetails(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("Invalid email or password");
            }
            if (!user.getStatus()) {
                throw new DisabledException("This account is locked");
            }

            String role = "ROLE_" + user.getRole().getName().toUpperCase();
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(role)));

        }
    }
