package lamthoncoding.shop.config;

import lamthoncoding.shop.security.CustomAuthenticationFailureHandler;
import lamthoncoding.shop.security.CustomAuthenticationSuccessHandler;
import lamthoncoding.shop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/home","resources/**","static/**", "images/**", "/register/**", "/css/**", "/js/**", "/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/saler/**").hasRole("SALER")
                        .anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")//doGet
                                .loginProcessingUrl("/login")//doPost
                                .defaultSuccessUrl("/home", true)
                                .failureHandler(new CustomAuthenticationFailureHandler())

                                .successHandler(new CustomAuthenticationSuccessHandler(userService))
                                .permitAll()
                );
        http.logout(
                logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
        );
        return http.build();
    }

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
        System.out.println("$2a$10$u9g9lCx3TrVWG/lKbWIzCOY16/tVox3Sel5hJdvSgzVqDjSILwFZm");
    }
}
