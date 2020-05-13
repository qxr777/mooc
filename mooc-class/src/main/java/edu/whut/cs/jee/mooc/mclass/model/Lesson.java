package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 备课/历史课堂/上课记录
 */
@Entity
@Table(name = "mclass_lesson")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends BaseEntity {

    public static final int STATUS_READY = 1;
    public static final int STATUS_SERVICING = 2;
    public static final int STATUS_END = 3;

    /**
     * 上课状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 备课使用时间
     */
    @Column(name = "service_date")
    private Date serviceDate;

    /**
     * 开始上课时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 下课时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 慕课堂
     */
    @Column(name = "mooc_class_id")
    private Long moocClassId;

    /**
     * 添加的练习
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id")
    private Set<Examination> examinations;

    /**
     * 添加的签到
     */
    @OneToOne
    @JoinColumn(name = "check_in_id")
    private CheckIn checkIn;


}