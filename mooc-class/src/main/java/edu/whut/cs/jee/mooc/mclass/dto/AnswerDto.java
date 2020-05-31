package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDto implements Serializable {

    private Long subjectId;

    private String answer;

    private boolean right;

}
