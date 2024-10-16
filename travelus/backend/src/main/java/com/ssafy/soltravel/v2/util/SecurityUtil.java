package com.ssafy.soltravel.v2.util;

import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.exception.auth.InvalidTokenException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new InvalidTokenException("No authentication found in context");
        }

        return (Long) authentication.getPrincipal();
    }

    /*
     * 토큰 파싱 후 유저 엔티티 반환
     */
    public User getUserByToken() {
        Long userId = getCurrentUserId();
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
