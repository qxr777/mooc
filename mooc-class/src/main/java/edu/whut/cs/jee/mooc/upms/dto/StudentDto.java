package edu.whut.cs.jee.mooc.upms.dto;

import edu.whut.cs.jee.mooc.upms.model.Role;
import edu.whut.cs.jee.mooc.upms.model.Student;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class StudentDto extends UserDto{

    @NotNull(message = "学号不允许为空")
    @Length(max = 20, min = 6, message = "学号长度必须是6-20个字符")
    private String studentNo;

    public Student convertTo(){
        Student student = new Student();
        BeanUtils.copyProperties(this, student);
        Role studentRole = new Role();
        studentRole.setId(Role.ROLE_STUDENT_ID.longValue());
        List<Role> roles = new ArrayList<>();
        roles.add(studentRole);
        student.setRoles(roles);
        return student;
    }

    public StudentDto convertFor(Student student){
        BeanUtils.copyProperties(student,this);
        return this;
    }

}
