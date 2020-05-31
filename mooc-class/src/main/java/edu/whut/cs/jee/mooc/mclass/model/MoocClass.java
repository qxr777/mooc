package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 慕课堂
 */
@Entity
@Table(name = "mclass_mooc_class")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MoocClass extends BaseEntity {

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 学年
     */
    @Column(name = "year")
    private String year;

    /**
     * 学期
     */
    @Column(name = "semester")
    private String semester;

    /**
     * 关联课程
     */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * 周几上课
     */
    @Column(name = "weekday")
    private String weekday;

    /**
     * 课堂码
     */
    @Column(name = "code", unique = true, nullable = false, length = 6, updatable = false)
    private String code;

    /**
     * 加入此慕课堂的用户
     */
    @ManyToMany
    @JoinTable(name = "mclass_class_user",
            joinColumns = {@JoinColumn(name = "mooc_class_id")},
            inverseJoinColumns = {@JoinColumn(name = "users_id")})
    private List<User> users;

}