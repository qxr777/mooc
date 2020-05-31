package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExaminationDto implements Serializable {

    private Long id;

    private String name;

    private Long lessonId;

    private Integer status;

    private List<SubjectDto> subjectDtos;

}
