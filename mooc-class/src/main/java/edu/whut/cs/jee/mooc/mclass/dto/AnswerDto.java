package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Answer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class AnswerDto implements Serializable {

    private Long subjectId;

    private String answer;

    private boolean right;

    public Answer convertTo(){
        Answer answer = new Answer();
        BeanUtils.copyProperties(this, answer);
        return answer;
    }

    public AnswerDto convertFor(Answer answer){
        BeanUtils.copyProperties(answer,this);
        return this;
    }

}
