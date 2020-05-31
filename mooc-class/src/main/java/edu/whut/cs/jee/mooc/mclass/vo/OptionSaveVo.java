package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

@Data
public class OptionSaveVo {
    private Long id;
    private String name;
    private String content;
    private boolean correct;
}
