package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 随堂练习
 */
@Entity
@Table(name = "mclass_examination")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Examination extends BaseEntity {

    /**
     * 发布随堂测试的上课记录
     */
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    /**
     * 随堂测试的名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 发布状态：未发布 | 进行中 | 已关闭
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 提交人数
     */
    @Column(name = "submit_count")
    private Integer submitCount = 0;

    /**
     * 习题
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="examination_id")
    @OrderBy("id ASC")
    private List<Subject> subjects;

}