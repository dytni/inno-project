package by.dytni.userservice.controller;

import static by.dytni.userservice.UserServiceConstants.DEFAULT_PAGE;
import static by.dytni.userservice.UserServiceConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.userservice.api.UserControllerApi;
import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserFilter;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;
import by.dytni.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserMaker userMaker) {
        return ResponseEntity.status(CREATED).body(userService.createUser(userMaker));
    }


    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserUpdater userUpdater,@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.updateUser(userUpdater, id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.deleteUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
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


    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/active/{id}")
    public ResponseEntity<User> changeStatus(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(userService.changeStatus(id));
    }


}
