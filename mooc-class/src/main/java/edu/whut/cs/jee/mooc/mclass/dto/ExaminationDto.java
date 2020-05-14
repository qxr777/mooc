package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Examination;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class ExaminationDto implements Serializable {

    private String name;

    private Long lessonId;

    private List<Subject> subjects;

    public Examination convertTo(){
        Examination examination = new Examination();
        BeanUtils.copyProperties(this, examination);
        return examination;
    }

    public ExaminationDto convertFor(Examination examination){
        BeanUtils.copyProperties(examination,this);
        this.subjects = examination.getSubjects();
        return this;
    }

}
