package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import edu.whut.cs.jee.mooc.upms.model.User;
import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto saveUser(UserDto userDto) {
        User user = userRepository.save(userDto.convertTo());
        return userDto.convertFor(user);
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).get();
        UserDto userDto = new UserDto();
        return userDto.convertFor(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = new ArrayList<User>((Collection<? extends User>) userRepository.findAll());
        return BeanConvertUtils.convertListTo(users,UserDto::new);
    }

    public void removeUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new APIException(AppCode.NO_USER_ERROR, AppCode.NO_USER_ERROR.getMsg() + userId);
        }
        userRepository.deleteById(userId);
    }

    public Page<User> getUsersByPage(UserDto userDto, Pageable pageable) {
        User exampleUser = BeanConvertUtils.convertTo(userDto, User::new);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", m -> m.contains())
                .withMatcher("nickname", m -> m.contains());

        Example<User> ex = Example.of(exampleUser, matcher);
        Page<User> userPage = userRepository.findAll(ex, pageable);
        return userPage;
    }
}
