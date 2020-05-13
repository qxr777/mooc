package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
