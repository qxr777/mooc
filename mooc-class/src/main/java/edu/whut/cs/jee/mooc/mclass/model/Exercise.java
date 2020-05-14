package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 练习库中的练习
 */
@Entity
@Table(name = "mclass_exercise")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseEntity {

    /**
     * 课程
     */
    @Column(name = "course_id")
    private Long courseId;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_id")
    @OrderBy("id ASC")
    private List<Subject> subjects;
}