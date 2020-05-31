package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 学生的答题记录
 */
@Entity
@Table(name = "mclass_answer")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity {

    /**
     * 学生的随堂练习记录
     */
    @Column(name = "examination_record_id")
    private Long examinationRecordId;

    /**
     * 学生
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 是否答对
     * 【强制】POJO类中布尔类型变量都不要加is前缀，否则部分框架解析会引起序列化错误。
     */
    @Column(name = "is_right", columnDefinition="TINYINT(3) UNSIGNED default '0'")
    private boolean right;

    /**
     * 批改状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 学生的回答内容
     */
    @Column(name = "answer")
    private String answer;

    @Column(name = "subject_id")
    private Long subjectId;

}