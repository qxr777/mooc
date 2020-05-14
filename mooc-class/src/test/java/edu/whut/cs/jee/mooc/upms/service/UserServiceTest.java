package edu.whut.cs.jee.mooc.upms.service;

import edu.whut.cs.jee.mooc.upms.model.Student;
import edu.whut.cs.jee.mooc.upms.model.Teacher;
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
        Teacher teacher = new Teacher();
        teacher.setName("whut");
        teacher.setNickname("李工大");
        teacher.setTitle("教师");
        userService.saveUser(teacher);
        Student student1 = new Student();
        student1.setName("xes1");
        student1.setNickname("薛而思");
        student1.setStudentNo("123456");
        userService.saveUser(student1);
        Student student2 = new Student();
        student2.setName("xdf1");
        student2.setNickname("信东方");
        student2.setStudentNo("123456");
        userService.saveUser(student2);
    }
}
