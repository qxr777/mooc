package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoocClassDto {

    private Long id;

    private Long teacherId;

    private String teacherName;
    /**
     * 名称
     */
    @NotNull(message = "慕课堂名称不允许为空")
    private String name;

    /**
     * 学年
     */
    @NotNull(message = "慕课堂所属学年不允许为空")
    private String year;

    /**
     * 学期
     */
    @NotNull(message = "慕课堂所属学期不允许为空")
    private String semester;

    /**
     * 关联课程ID
     */
    private Long courseId;

    private String courseName;

    /**
     * 独立线下课程名称
     */
    private String offlineCourse;

    /**
     * 周几上课
     */
    private String weekday;

    /**
     * 课堂码
     */
    private String code;

    public MoocClass convertTo(){
        MoocClass moocClass = new MoocClass();
        BeanUtils.copyProperties(this,moocClass);
        return moocClass;
    }

    public MoocClassDto convertFor(MoocClass moocClass){
        if (moocClass.getCourse() != null) {
            teacherName = moocClass.getCourse().getTeacher().getName();
            courseName = moocClass.getCourse().getName();
            courseId = moocClass.getCourse().getId();
        }
        BeanUtils.copyProperties(moocClass,this);
        return this;
    }

}
