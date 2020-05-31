package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.common.constant.MoocClassConstatnts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {

    private Long id;

    private Integer status = MoocClassConstatnts.LESSON_STATUS_READY;

    private String statusCh;

    private Integer checkInCount;

    private Integer examinationCount;

    @NotNull
    private Long moocClassId;

    @NotNull
    private Date serviceDate;

    private Date startTime;

    private Date endTime;

}
