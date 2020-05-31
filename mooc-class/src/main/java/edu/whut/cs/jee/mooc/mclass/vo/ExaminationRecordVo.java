package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExaminationRecordVo implements Serializable {

    private Long id;

    private Long examinationId;

    private Long userId;

    private String userName;

    private String userNickname;

    private Double score = 0.0;

    private Integer correctCount = 0;

    private Date submitTime;


}
