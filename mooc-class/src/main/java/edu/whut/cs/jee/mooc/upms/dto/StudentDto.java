package edu.whut.cs.jee.mooc.upms.dto;

import edu.whut.cs.jee.mooc.upms.model.Student;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Data
public class StudentDto extends UserDto{

    @NotNull(message = "学号不允许为空")
    @Length(max = 20, min = 6)
    private String studentNo;

    public Student convertTo(){
        Student student = new Student();
        BeanUtils.copyProperties(this, student);
        return student;
    }

    public StudentDto convertFor(Student student){
        BeanUtils.copyProperties(student,this);
        return this;
    }

}
