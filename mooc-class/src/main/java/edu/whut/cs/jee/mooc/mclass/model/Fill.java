package edu.whut.cs.jee.mooc.mclass.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 填空题
 */
@Entity
@Table(name = "mclass_fill")
@PrimaryKeyJoinColumn(name = "fill_id")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Fill extends Subject {

    public static final int KEY_TYPE_DECIMAL = 1; // 数值格式
    public static final int KEY_TYPE_TEXT = 2;  // 文字格式

    public static final int MATCH_TYPE_EXACT = 1; // 精确匹配
    public static final int MATCH_TYP_FUZZY = 2;   // 模糊匹配

    /**
     * 数值格式 | 文字格式
     */
    @Column(name = "key_type")
    private Integer keyType;

    /**
     * 数值格式答案
     */
    @Column(name = "decimal_key")
    private String decimalKey;

    /**
     * 文字格式答案
     */
    @Column(name = "text_key")
    private String textKey;

    /**
     * 匹配类型：精确 | 模糊
     */
    @Column(name = "match_type")
    private Integer matchType;



    /**
     * 是否唯一答案
     */
    @Column(name = "is_unique", columnDefinition="TINYINT(3) UNSIGNED default '0'")
    private boolean unique;

    @Override
    public boolean check(String answer) {
        boolean result = false;
        if (matchType == this.MATCH_TYPE_EXACT) {
            result = answer.equals(textKey) || answer.equals(decimalKey);
        } else {
            if (keyType == KEY_TYPE_TEXT) {
                result = answer.indexOf(textKey) >= 0;
            }
        }
        if (result) {
            this.setRightCount(this.getRightCount() + 1);
        } else {
            this.setErrorCount(this.getErrorCount() + 1);
        }
        calculatePercent();   // 计算百分比
        return result;
    }
}