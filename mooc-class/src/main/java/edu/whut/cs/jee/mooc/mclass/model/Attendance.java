package edu.whut.cs.jee.mooc.mclass.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.*;

import javax.persistence.*;

/**
 * 学生的签到记录
 */
@Entity
@Table(name = "mclass_attendance")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity {

    public static final int STATUS_CHECKED = 1; // 已签到
    public static final int STATUS_LATE = 2;  // 迟到
    public static final int STATUS_ABSENCE = 3; // 缺课

    public static final String[] STATUS_STRING_CH={"未签到", "已签到", "迟到", "缺课"};

    /**
     * 签到活动
     */
    @Column(name = "check_in_id")
    private Long checkInId;

    /**
     * 已签到 | 迟到 | 缺课
     */
    @Column(name = "status" )
    private Integer status = STATUS_ABSENCE;

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
     * 
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}