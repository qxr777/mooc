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

    public static final int STATUS_PRIVATE = 1; // 未发布
    public static final int STATUS_OPEN = 2;  // 进行中
    public static final int STATUS_CLOSED = 3;  // 已关闭

    /**
     * 启动随堂练习的备课
     */
    @Column(name = "lesson_id")
    private Long lessonId;

    /**
     * 随堂练习的名称
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
    private Integer submitCount;

    /**
     * 习题
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="examination_id")
    private List<Subject> subjects;

}