package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void testSaveUser() {
        TeacherDto teacher = new TeacherDto();
        teacher.setName("whut");
        teacher.setNickname("李工大");
        teacher.setTitle("教师");
        teacher.setEmail("123@qq.com");
        userService.saveUser(teacher);
        StudentDto student1 = new StudentDto();
        student1.setName("xes1");
        student1.setNickname("薛而思");
        student1.setStudentNo("123456");
        student1.setEmail("321@qq.com");
        userService.saveUser(student1);
        StudentDto student2 = new StudentDto();
        student2.setName("xdf1");
        student2.setNickname("信东方");
        student2.setStudentNo("123456");
        student2.setEmail("322@qq.com");
        userService.saveUser(student2);
    }
}
