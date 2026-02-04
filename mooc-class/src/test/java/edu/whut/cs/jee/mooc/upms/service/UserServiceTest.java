package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.upms.dto.RoleDto;
import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
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
        RoleDto teacherRole = new RoleDto(AppConstants.ROLE_TEACHER_ID);
        RoleDto adminRole = new RoleDto(AppConstants.ROLE_ADMIN_ID);
        teacher.addRole(teacherRole);
        teacher.addRole(adminRole);
        userService.saveUser(teacher);
        StudentDto student1 = new StudentDto();
        student1.setName("xes1");
        student1.setNickname("薛而思");
        student1.setStudentNo("201912345601");
        student1.setEmail("321@qq.com");
        student1.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        RoleDto studentRole = new RoleDto(AppConstants.ROLE_STUDENT_ID);
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

//    @Test
    public void testSaveRoles() {
        Role role1 = new Role();
        role1.setId(AppConstants.ROLE_ADMIN_ID);
        role1.setName("ROLE_ADMIN");
        userService.saveRole(role1);
        Role role2 = new Role();
        role2.setId(AppConstants.ROLE_TEACHER_ID);
        role2.setName("ROLE_TEACHER");
        userService.saveRole(role2);
        Role role3 = new Role();
        role3.setId(AppConstants.ROLE_STUDENT_ID);
        role3.setName("ROLE_STUDENT");
        userService.saveRole(role3);

    }

    @Test
    public void testSaveAndRemoveUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TeacherDto teacher = new TeacherDto();
        teacher.setName("whut_UNIT_TEST");
        teacher.setNickname("李工大");
        teacher.setTitle("教师");
        teacher.setEmail("123@qq.com");
        teacher.setPassword(encoder.encode(AppConstants.DEFAULT_PASSWORD));
        teacher.setSalaryNo("0099937");
        RoleDto teacherRole = new RoleDto(AppConstants.ROLE_TEACHER_ID);
        RoleDto adminRole = new RoleDto(AppConstants.ROLE_ADMIN_ID);
        teacher.addRole(teacherRole);
        teacher.addRole(adminRole);
        newUserId = userService.saveUser(teacher);
        Assert.isTrue(newUserId > 0);

        userService.removeUser(newUserId);
    }
}
