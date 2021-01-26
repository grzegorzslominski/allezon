package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.allezon.DTO.UserDTO;
import pl.edu.pjwstk.jaz.allezon.repository.UserRepository;

@RestController
public class RegisterController {
    private final UserRepository userRepository;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("allezon/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email or password is empty.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.emailExists(user.getEmail())) {
            return new ResponseEntity<>("Such an email exists in the database.", HttpStatus.CONFLICT);
        }
        userRepository.saveUser(user);
        return new ResponseEntity<>("Registered.", HttpStatus.CREATED);
    }
}
