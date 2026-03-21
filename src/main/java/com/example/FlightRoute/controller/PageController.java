package com.example.FlightRoute.controller;

import com.example.FlightRoute.model.PageName;
import com.example.FlightRoute.service.PageService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping("allowed-page-names")
    @PreAuthorize("hasAnyRole('ADMIN','AGENCY')")
    public List<PageName> getAllowedPages(Authentication authentication) {
        return pageService.getAllowedPages(authentication);
    }
}
