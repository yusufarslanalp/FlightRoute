package com.example.flight.route.service;

import com.example.flight.route.model.PageName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class PageService {

    public List<PageName> getAllowedPages(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
        
        if (isAdmin) {
            return List.of(PageName.ROUTES, PageName.TRANSPORTATIONS, PageName.LOCATIONS);
        } else {
            return List.of(PageName.ROUTES);
        }
    }
}
