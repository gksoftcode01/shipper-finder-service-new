package ai.yarmook.shipperfinder.core.controller;

import ai.yarmook.shipperfinder.core.dto.vm.LoginOTP;
import ai.yarmook.shipperfinder.core.model.YPreferredRequest;
import ai.yarmook.shipperfinder.core.model.YSearchRequest;
import ai.yarmook.shipperfinder.core.security.CurrentUser;
import ai.yarmook.shipperfinder.core.security.OtpAuthenticationToken;
import ai.yarmook.shipperfinder.core.service.SearchService;
import ai.yarmook.shipperfinder.core.service.YarmookService;
import ai.yarmook.shipperfinder.domain.User;
import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import ai.yarmook.shipperfinder.security.AuthoritiesConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/yarmook")
public class YarmookController {

    private final Logger log = LoggerFactory.getLogger(YarmookController.class);

    private final SearchService searchService;
    private final YarmookService yarmookService;

    public YarmookController(SearchService searchService, YarmookService yarmookService) {
        this.searchService = searchService;
        this.yarmookService = yarmookService;
    }

    @GetMapping("/printUser")
    public String printUser(@CurrentUser Object user) {
        log.info(user.toString());
        log.info(((Jwt) user).getClaims().toString());
        return user.toString();
    }

    @PostMapping("/preferred")
    public ResponseEntity<List<ObjectNode>> preferredByUser(@CurrentUser Object user, @RequestBody YPreferredRequest request)
        throws IOException {
        return ResponseEntity.ok(searchService.preferred(request));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ObjectNode>> searchTripByUser(@CurrentUser Object user, @RequestBody YSearchRequest request)
        throws IOException {
        return ResponseEntity.ok(searchService.search(request));
    }

    @GetMapping("/util/statusList")
    public ResponseEntity<List<String>> statusList(@CurrentUser Object user) {
        return ResponseEntity.ok(Arrays.stream(TripStatus.values()).map(item -> item.toString()).toList());
    }

    @GetMapping("/autoComplete/{query}")
    public ResponseEntity<List<Map<String, Object>>> listAutoComplete(@CurrentUser Object user, @PathVariable String query) {
        return ResponseEntity.ok(yarmookService.itemAutoCompleteList(query));
    }
}
