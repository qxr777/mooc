package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.model.Choice;
import edu.whut.cs.jee.mooc.mclass.model.Examination;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import edu.whut.cs.jee.mooc.mclass.vo.OptionVo;
import edu.whut.cs.jee.mooc.mclass.vo.SubjectVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExaminationDto implements Serializable {

    private Long id;

    private String name;

    private Long lessonId;

    private List<SubjectVo> subjectVos;

    public Examination convertTo(){
        Examination examination = new Examination();
        BeanUtils.copyProperties(this, examination);
        return examination;
    }

    public ExaminationDto convertFor(Examination examination){
        BeanUtils.copyProperties(examination,this);
        List<Subject> subjects = examination.getSubjects();
        subjectVos = new ArrayList<>();
        SubjectVo subjectVo = null;
        for(Subject subject : subjects) {
            subjectVo = BeanConvertUtils.convertTo(subject, SubjectVo::new);
            subjectVo.setType(subject.getClass().getSimpleName());
            if (subject instanceof Choice) {
                Choice choice = (Choice)subject;
                subjectVo.setOptionVos(BeanConvertUtils.convertListTo(choice.getOptions(), OptionVo::new));
            }
            subjectVos.add(subjectVo);
        }
        return this;
    }

}
