package edu.whut.cs.jee.mooc.upms.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class TeacherDto extends UserDto{

    @NotNull(message = "工资号不允许为空")
    @Length(max = 20, min = 6)
    private String salaryNo;

    private String title;

//    public Teacher convertTo(){
//        Teacher teacher = new Teacher();
//        BeanUtils.copyProperties(this, teacher);
//        return teacher;
//    }
//
//    public TeacherDto convertFor(Teacher teacher){
//        BeanUtils.copyProperties(teacher,this);
//        return this;
//    }

}
