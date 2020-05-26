package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import java.util.List;

@Data
public class SubjectVo {

    private Long id;
    private String content;
    private Double score;
    private String type;

    private List<OptionVo> optionVos;
}
