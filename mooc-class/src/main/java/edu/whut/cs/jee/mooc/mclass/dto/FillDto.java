package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.common.constant.SubjectConstants;
import lombok.Data;

/**
 * 填空题
 */
@Data
public class FillDto extends SubjectDto {

    /**
     * 数值格式 | 文字格式
     */
    private Integer keyType;

    /**
     * 数值格式答案
     */
    private String decimalKey;

    /**
     * 文字格式答案
     */
    private String textKey;

    /**
     * 匹配类型：精确 | 模糊
     */
    private Integer matchType;

    /**
     * 是否唯一答案
     */
    private boolean unique;

    public String getKey() {
        String key = null;
        if (this.getKeyType() == SubjectConstants.FILL_KEY_TYPE_DECIMAL) {
            key = SubjectConstants.FILL_MATCH_TYPE_STRING_CH[this.getMatchType()] + " " + this.getDecimalKey();
        } else if (this.getKeyType() == SubjectConstants.FILL_KEY_TYPE_TEXT) {
            key = SubjectConstants.FILL_MATCH_TYPE_STRING_CH[this.getMatchType()] + " " + this.getTextKey();
        }
        return key;
    }

}