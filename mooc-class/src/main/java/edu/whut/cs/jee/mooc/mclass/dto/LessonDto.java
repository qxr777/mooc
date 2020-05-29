package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {

    private Long id;

    private Integer status;

    private String statusCh;

    private Integer checkInCount;

    private Integer examinationCount;

    @NotNull
    private Long moocClassId;

    @NotNull
    private Date serviceDate;

    private Date startTime;

    private Date endTime;

    public Lesson convertTo(){
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(this, lesson);
        return lesson;
    }

    public LessonDto convertFor(Lesson lesson){
        LessonDto lessonDto = new LessonDto();
        BeanUtils.copyProperties(lesson,lessonDto);
        lessonDto.setStatusCh(Lesson.STATUS_STRING_CH[lessonDto.getStatus()]);
        return lessonDto;
    }

}
