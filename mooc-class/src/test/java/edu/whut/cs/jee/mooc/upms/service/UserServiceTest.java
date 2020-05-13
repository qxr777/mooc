package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.upms.model.Student;
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
    public void saveUser() {
        Student student = new Student();
        student.setName("xes1");
        student.setNickname("薛而思");
        student.setStudentNo("123456");
        userService.saveUser(student);
    }
}
