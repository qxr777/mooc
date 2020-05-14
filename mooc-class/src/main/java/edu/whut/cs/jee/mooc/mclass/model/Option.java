package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 选择题候选项
 */
@Entity
@Table(name = "mclass_option")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Option extends BaseEntity implements Cloneable{

    /**
     * 选项名
     */
    @Column(name = "name")
    private String name;

    /**
     * 选项内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 选择人数
     */
    @Column(name = "count")
    private Integer count = 0;

    /**
     *  是否正确选项
     * 【强制】POJO类中布尔类型变量都不要加is前缀，否则部分框架解析会引起序列化错误。
     */
    @Column(name = "is_correct", columnDefinition="TINYINT(3) UNSIGNED default '0'")
    private boolean correct;

    @Override
    public Object clone() {
        Option option = null;
        try{
            option = (Option)super.clone();
            option.setId(null);
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return option;
    }

}