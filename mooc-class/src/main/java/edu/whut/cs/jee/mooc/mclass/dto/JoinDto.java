package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JoinDto implements Serializable {

    private Long userId;

    private String moocClassCode;

}
