package edu.whut.cs.jee.mooc.mclass.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 判断题
 */
@Entity
@Table(name = "mclass_judgment")
@PrimaryKeyJoinColumn(name = "judgment_id")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Judgment extends Subject {

    /**
     * 正确判断结果
     */
    @Column(name = "result", columnDefinition="TINYINT(3) UNSIGNED default '0'")
    private boolean result;

    /**
     * 选T的人数
     */
    @Column(name = "true_count")
    private Integer trueCount = 0;

    /**
     * 选F的人数
     */
    @Column(name = "false_count")
    private Integer falseCount = 0;

    @Override
    public boolean check(String answer) {
        boolean answerBoolean = Boolean.parseBoolean(answer);
        if (answerBoolean) {
            ++trueCount;
        } else {
            ++falseCount;
        }
        boolean isRight = answerBoolean == this.result;
        if (isRight) {
            this.setRightCount(this.getRightCount() + 1);
        } else {
            this.setErrorCount(this.getErrorCount() + 1);
        }
        calculatePercent();   // 计算百分比
        return  isRight;
    }
}