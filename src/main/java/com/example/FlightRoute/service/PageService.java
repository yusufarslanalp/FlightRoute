package com.example.FlightRoute.service;

import com.example.FlightRoute.model.PageName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PageService {

    public List<PageName> getAllowedPages(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            return List.of(PageName.ROUTES, PageName.TRANSPORTATIONS, PageName.LOCATIONS);
        } else {
            return List.of(PageName.ROUTES);
        }
    }
}
