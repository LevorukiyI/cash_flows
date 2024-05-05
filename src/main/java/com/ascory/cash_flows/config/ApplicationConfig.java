package com.ascory.authservice.config;

import com.ascory.authservice.auditing.ApplicationAuditAware;
import com.ascory.authservice.models.CargoTariffCoefficients;
import com.ascory.authservice.models.TariffCoefficients;
import com.ascory.authservice.models.TransportTariffCoefficients;
import com.ascory.authservice.models.TransportType;
import com.ascory.authservice.repositories.TariffCoefficientsRepository;
import com.ascory.authservice.repositories.TransportTariffCoefficientsRepository;
import com.ascory.authservice.services.DefaultUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {

    private final DefaultUserDetailsService defaultUserDetailsService;
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new ApplicationAuditAware();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setForcePrincipalAsString(true);
        daoAuthenticationProvider.setUserDetailsService(defaultUserDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public TariffCoefficients tariffCoefficients(
            TariffCoefficientsRepository tariffCoefficientsRepository){




        TariffCoefficients tariffCoefficients =
                TariffCoefficients.builder()
                        .dangerousCargoCoefficient(1.0)
                        .specialCargoCoefficient(1.0)
                        .plainTransportTypeCoefficient(1.0)
                        .plainCargoMassCoefficient(1.0)
                        .plainCargoVolumeCoefficient(1.0)
                        .shipTransportTypeCoefficient(1.0)
                        .shipCargoMassCoefficient(1.0)
                        .shipCargoVolumeCoefficient(1.0)
                        .truckTransportTypeCoefficient(1.0)
                        .truckCargoMassCoefficient(1.0)
                        .truckCargoVolumeCoefficient(1.0)
                        .build();
        tariffCoefficientsRepository.save(tariffCoefficients);
        return tariffCoefficients;
    }

}

