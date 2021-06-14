package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import edu.whut.cs.jee.mooc.upms.model.Teacher;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * 课程
 */
@Entity
@Table(name = "mclass_course")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

    /**
     * 课程名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 课程类型：在线课程 | 独立线下课程
     */
    @Column(name = "type")
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    /**
     * 慕课堂
     */
    @OneToMany
    @JoinColumn(name="course_id")
    private Set<MoocClass> moocClasses;


}