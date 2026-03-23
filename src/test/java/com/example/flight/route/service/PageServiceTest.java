package com.example.flight.route.service;

import com.example.flight.route.model.PageName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PageServiceTest {

    private PageService pageService;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        pageService = new PageService();
        authentication = mock(Authentication.class);
    }

    @Test
    void testGetAllowedPagesForAdmin() {
        GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
        List<GrantedAuthority> authorities = List.of(adminAuthority);
        when(authentication.getAuthorities()).thenReturn((List) authorities);

        List<PageName> pages = pageService.getAllowedPages(authentication);

        assertEquals(3, pages.size());
        assertEquals(List.of(PageName.ROUTES, PageName.TRANSPORTATIONS, PageName.LOCATIONS), pages);
    }

    @Test
    void testGetAllowedPagesForNonAdmin() {
        GrantedAuthority userAuthority = () -> "ROLE_USER";
        List<GrantedAuthority> authorities = List.of(userAuthority);
        when(authentication.getAuthorities()).thenReturn((List) authorities);

        List<PageName> pages = pageService.getAllowedPages(authentication);

        assertEquals(1, pages.size());
        assertEquals(List.of(PageName.ROUTES), pages);
    }

    @Test
    void testGetAllowedPagesForEmptyAuthorities() {
        List<GrantedAuthority> authorities = List.of();
        when(authentication.getAuthorities()).thenReturn((List) authorities);

        List<PageName> pages = pageService.getAllowedPages(authentication);

        assertEquals(1, pages.size());
        assertEquals(List.of(PageName.ROUTES), pages);
    }
}