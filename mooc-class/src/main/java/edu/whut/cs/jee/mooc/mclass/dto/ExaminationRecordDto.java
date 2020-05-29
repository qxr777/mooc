package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.ExaminationRecord;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ExaminationRecordDto implements Serializable {

    private Long id;

    private Long examinationId;

    private Long userId;

    private String userName;

    private Double score = 0.0;

    private Integer correctCount = 0;

    private Date submitTime;

    private List<AnswerDto> answerDtos = new ArrayList<>();

    public void addAnswer(AnswerDto answerDto) {
        answerDtos.add(answerDto);
    }

    public ExaminationRecord convertTo(){
        ExaminationRecord examinationRecord = new ExaminationRecord();
        BeanUtils.copyProperties(this, examinationRecord);
        User user = new User();
        user.setId(this.userId);
        examinationRecord.setUser(user);
        return examinationRecord;
    }

    public ExaminationRecordDto convertFor(ExaminationRecord examinationRecord){
        BeanUtils.copyProperties(examinationRecord,this);
        this.setUserId(examinationRecord.getUser().getId());
        this.setUserName(examinationRecord.getUser().getName());
        return this;
    }

}
