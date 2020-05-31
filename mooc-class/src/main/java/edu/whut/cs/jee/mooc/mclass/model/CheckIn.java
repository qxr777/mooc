package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 签到活动
 */
@Entity
@Table(name = "mclass_check_in")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn extends BaseEntity {

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 备课，历史课堂，上课记录
     */
    @Column(name = "lesson_id")
    private Long lessonId;

    /**
     * 签到截止时间
     */
    @Column(name = "deadline")
    private Date deadline;

    /**
     * GPS定位签到
     */
    @Column(name = "is_gps")
    private boolean gps;

    /**
     * 签到处 经度
     */
    @Column(name = "longitude", columnDefinition = "decimal(5,2)")
    private Double longitude;

    /**
     * 签到处 纬度
     */
    @Column(name = "latitude", columnDefinition = "decimal(5,2)")
    private Double latitude;

    /**
     * 正常签到数
     */
    @Column(name = "checked_count")
    private Integer checkedCount;

    /**
     * 旷课数
     */
    @Column(name = "absence_count")
    private Integer absenceCount;

    /**
     * 迟到数
     */
    @Column(name = "late_count")
    private Integer lateCount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="check_in_id")
    private List<Attendance> attendances;


}