package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseDto {

    private Long id;

    private Long teacherId;

    @NotNull(message = "课程名称不允许为空")
    private String name;

}
