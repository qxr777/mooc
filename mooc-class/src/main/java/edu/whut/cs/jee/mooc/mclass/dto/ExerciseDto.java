package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    private Long id;

    private Long courseId;

    @NotNull(message = "练习名称不允许为空")
    private String name;

}
