package edu.whut.cs.jee.mooc.mclass.dto;

import com.sun.istack.internal.NotNull;
import lombok.Data;

@Data
public class CourseDto {

    private Long id;

    @NotNull
    private String name;

}
