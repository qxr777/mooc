package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * 习题
 */
@Entity
@Table(name = "mclass_subject")
@Inheritance(strategy = InheritanceType.JOINED)
//@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Subject extends BaseEntity implements Cloneable {

    /**
     * 练习库中练习
     */
    @Column(name = "exercise_id")
    private Long exerciseId;

    /**
     * 随堂练习
     */
    @Column(name = "examination_id")
    private Long examinationId;

    /**
     * 题干内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 分数
     */
    @Column(name = "score", columnDefinition = "decimal(5,2)")
    private Double score;

    /**
     * 正确人数
     */
    @Column(name = "right_count")
    private Integer rightCount = 0;

    /**
     * 正确百分比
     */
    @Column(name = "right_percent")
    private Integer rightPercent;

    /**
     * 错误人数
     */
    @Column(name = "error_count")
    private Integer errorCount = 0;

    /**
     * 错误百分比
     */
    @Column(name = "error_percent")
    private Integer errorPercent;


    public abstract boolean check(String answer);

    protected void calculatePercent() {
        int sum = rightCount + errorCount;
        this.rightPercent = 100 * rightCount / sum;
        this.errorPercent = 100 * errorCount / sum;
    }

    @Override
    public Object clone() {
        Subject subject = null;
        try{
            subject = (Subject)super.clone();
            subject.setId(null);
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return subject;
    }

}