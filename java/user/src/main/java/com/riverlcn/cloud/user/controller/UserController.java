package com.riverlcn.cloud.user.controller;

import com.riverlcn.cloud.user.dao.UserRepository;
import com.riverlcn.cloud.user.enity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author river
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        return this.userRepository.findById(id);
    }

}
