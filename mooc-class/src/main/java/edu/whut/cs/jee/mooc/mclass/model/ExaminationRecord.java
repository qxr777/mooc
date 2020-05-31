package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 学生的随堂练习记录
 */
@Entity
@Table(name = "mclass_examination_record")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationRecord extends BaseEntity {

    /**
     * 得分
     */
    @Column(name = "score", columnDefinition = "decimal(5,2)")
    private Double score = 0.0;

    /**
     * 答对数量
     */
    @Column(name = "correct_count")
    private Integer correctCount = 0;

    /**
     * 完成时间
     */
    @Column(name = "submit_time")
    private Date submitTime;

    /**
     * 发布的随堂练习
     */
    @Column(name = "examination_id")
    private Long examinationId;

    /**
     * 参加的学生
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="examination_record_id")
    private List<Answer> answers;

}