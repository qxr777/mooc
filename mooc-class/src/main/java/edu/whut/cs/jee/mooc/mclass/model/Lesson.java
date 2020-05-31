package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    public Lesson(Long id) {
        super();
        this.setId(id);
    }

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

    @Column(name = "examination_count")
    private Integer examinationCount;

    /**
     * 添加的签到
     */
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "check_in_id")
    private CheckIn checkIn;


}