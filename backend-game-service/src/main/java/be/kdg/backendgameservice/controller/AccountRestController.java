package be.kdg.backendgameservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountRestController {

    @GetMapping
    @PreAuthorize("hasAuthority('player')")
    public String getAccount() {
        return "Account";
    }
}
