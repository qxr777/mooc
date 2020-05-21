package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import java.util.List;

@Data
public class SubjectVo {
//    public static final Integer TYPE_JUDGMENT = 1;
//    public static final Integer TYPE_FILL = 2;
//    public static final Integer TYPE_CHOICE = 3;

    private Long id;
    private String content;
    private Double score;
    private String type;
//    private Integer type;

    private List<OptionVo> optionVos;
}
