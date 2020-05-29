package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import edu.whut.cs.jee.mooc.upms.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    private static Long newUserId;

    @Resource
    UserService userService;

//    @Test
    public void testSaveThreeUsers() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TeacherDto teacher = new TeacherDto();
        teacher.setName("whut_UNIT_TEST");
        teacher.setNickname("李工大");
        teacher.setTitle("教师");
        teacher.setEmail("123@qq.com");
        teacher.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        teacher.setSalaryNo("0099937");
        Role teacherRole = new Role(Role.ROLE_TEACHER_ID);
        Role adminRole = new Role(Role.ROLE_ADMIN_ID);
        teacher.addRole(teacherRole);
        teacher.addRole(adminRole);
        userService.saveUser(teacher);
        StudentDto student1 = new StudentDto();
        student1.setName("xes1");
        student1.setNickname("薛而思");
        student1.setStudentNo("201912345601");
        student1.setEmail("321@qq.com");
        student1.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        Role studentRole = new Role(Role.ROLE_STUDENT_ID);
        student1.addRole(studentRole);
        userService.saveUser(student1);
        StudentDto student2 = new StudentDto();
        student2.setName("xdf1");
        student2.setNickname("信东方");
        student2.setStudentNo("201912345602");
        student2.setEmail("322@qq.com");
        student2.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        student2.addRole(studentRole);
        userService.saveUser(student2);
    }

    @Test
    public void testSaveUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TeacherDto teacher = new TeacherDto();
        teacher.setName("whut_UNIT_TEST");
        teacher.setNickname("李工大");
        teacher.setTitle("教师");
        teacher.setEmail("123@qq.com");
        teacher.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        teacher.setSalaryNo("0099937");
        Role teacherRole = new Role(Role.ROLE_TEACHER_ID);
        Role adminRole = new Role(Role.ROLE_ADMIN_ID);
        teacher.addRole(teacherRole);
        teacher.addRole(adminRole);
        UserDto userDto = userService.saveUser(teacher);
        newUserId = userDto.getId();
        Assert.isTrue(newUserId > 0);
    }

    @Test
    public void testRemoveUser() {
        userService.removeUser(newUserId);
    }
}
