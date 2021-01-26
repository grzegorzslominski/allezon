package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.allezon.entity.UserEntity;
import pl.edu.pjwstk.jaz.allezon.repository.UserRepository;
import pl.edu.pjwstk.jaz.allezon.service.UserService;

@RequiredArgsConstructor
@RestController
public class RegisterController {
    private final UserRepository userRepository;
    private final UserService userService;


    @PostMapping("allezon/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password is empty.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Such an email exists in the database.", HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        return new ResponseEntity<>("Registered.", HttpStatus.CREATED);
    }
}
