package by.dytni.innoviseproject.controller;

import static by.dytni.innoviseproject.InnoviseProjectConstants.DEFAULT_PAGE;
import static by.dytni.innoviseproject.InnoviseProjectConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.innoviseproject.api.UserControllerApi;
import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.dto.user.UserUpdater;
import by.dytni.innoviseproject.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserMaker userMaker) {
        return ResponseEntity.status(CREATED).body(userService.createUser(userMaker));
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserUpdater userUpdater,@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.updateUser(userUpdater, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.deleteUser(id));
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.status(OK).body(userService.getAllUsers(UserFilter.builder()
                                                                              .firstName(name)
                                                                              .lastName(lastName)
                                                                              .page(page)
                                                                              .size(size).build()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.getUserById(id));
    }


    @PutMapping("/active/{id}")
    public ResponseEntity<User> changeStatus(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.changeStatus(id));
    }


}
