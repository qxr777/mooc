package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Subject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExaminationDto implements Serializable {

    private Long exerciseId;

    private Long lessonId;

    private List<Subject> subjects;

}
