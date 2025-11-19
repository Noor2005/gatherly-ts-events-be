package com.techsisters.gatherly.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.techsisters.gatherly.config.CustomUserDetails;
import com.techsisters.gatherly.entity.User;

public class SecurityUtil {

    /**
     * Gets the currently authenticated user entity.
     * @return User entity or null if not authenticated
     */
    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        
        return null;
    }

    /**
     * Gets the currently authenticated user's email.
     * @return Email or "anonymous" if not authenticated
     */
    public static String getCurrentUserEmail() {
        User user = getCurrentUser();
        return user != null ? user.getEmail() : "anonymous";
    }
}