package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.upms.dto.RoleDto;
import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import edu.whut.cs.jee.mooc.upms.model.Role;
import edu.whut.cs.jee.mooc.upms.model.Student;
import edu.whut.cs.jee.mooc.upms.model.Teacher;
import edu.whut.cs.jee.mooc.upms.model.User;
import edu.whut.cs.jee.mooc.upms.repository.RoleRepository;
import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CacheConfig(cacheNames = "users")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Long saveUser(UserDto userDto) {
        User user = null;
        try {
            user = userDto instanceof TeacherDto ? BeanConvertUtils.convertTo(userDto, Teacher::new) : BeanConvertUtils.convertTo(userDto, Student::new) ;
            user.setRoles(BeanConvertUtils.convertListTo(userDto.getRoles(), Role::new));
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new APIException(AppCode.USERNAME_DUPLICATE_ERROR, userDto.getName() + AppCode.USERNAME_DUPLICATE_ERROR.getMsg());
        }

        return user.getId();
    }

    public Long saveRole(Role role) {
        role = roleRepository.save(role);
        return role.getId();
    }

    @Cacheable(cacheNames = "user" ,key="#id")
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).get();
        List<RoleDto> roleDtos = BeanConvertUtils.convertListTo(user.getRoles(), RoleDto::new);
        UserDto userDto = user instanceof Teacher ? BeanConvertUtils.convertTo(user, TeacherDto::new) : BeanConvertUtils.convertTo(user, StudentDto::new);
        userDto.setRoles(roleDtos);
        return userDto;
    }

    @Cacheable
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = new ArrayList<User>((Collection<? extends User>) userRepository.findAll());
        List<UserDto> userDtos = new ArrayList<>();
        List<RoleDto> roleDtos = null;
        UserDto userDto = null;
        for(User user : users) {
            userDto = BeanConvertUtils.convertTo(user, UserDto::new);
            roleDtos = BeanConvertUtils.convertListTo(user.getRoles(), RoleDto::new);
            userDto.setRoles(roleDtos);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public void removeUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new APIException(AppCode.NO_USER_ERROR, AppCode.NO_USER_ERROR.getMsg() + userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByPage(UserDto userDto, Pageable pageable) {
        User exampleUser = BeanConvertUtils.convertTo(userDto, User::new);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", m -> m.contains())
                .withMatcher("nickname", m -> m.contains());

        Example<User> ex = Example.of(exampleUser, matcher);
        Page<User> userPage = userRepository.findAll(ex, pageable);
        int totalElements = (int) userPage.getTotalElements();
        return new PageImpl<UserDto>(userPage
                .stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getNickname(),
                        user.getPassword(),
                        user.getEmail(),
                        BeanConvertUtils.convertListTo(user.getRoles(), RoleDto::new)))
                .collect(Collectors.toList()), pageable, totalElements);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#p0")
    public List<UserDto> getUserByUsername(String username) {
        List<User> users = userRepository.findByName(username);
        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto = null;
        List<RoleDto> roleDtos = null;
        for(User user : users) {
            userDto = BeanConvertUtils.convertTo(user, UserDto::new);
            roleDtos = BeanConvertUtils.convertListTo(user.getRoles(), RoleDto::new);
            userDto.setRoles(roleDtos);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public Long register(UserDto userToAdd) {
        final String username = userToAdd.getName();
        if(userRepository.findByName(username).size() > 0) {
            throw new APIException(AppCode.USERNAME_DUPLICATE_ERROR, username + AppCode.USERNAME_DUPLICATE_ERROR.getMsg());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));
        User user = null;
        if (userToAdd instanceof TeacherDto) {
            user = BeanConvertUtils.convertTo(userToAdd, Teacher::new);
        } else if (userToAdd instanceof StudentDto) {
            user = BeanConvertUtils.convertTo(userToAdd, Student::new);
        }
        List<Role> roles = BeanConvertUtils.convertListTo(userToAdd.getRoles(), Role::new);
        user.setRoles(roles);
        return userRepository.save(user).getId();
    }
}
