package pl.edu.pjwstk.jaz.allezon.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class UserSession {
    private Long userId;
    private boolean isLogged = false;

    public boolean isLoggedIn() {
        return isLogged;
    }

    public void setLoggedIn(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public void logIn() {
        isLogged = true;
    }
}
