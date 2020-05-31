package edu.whut.cs.jee.mooc.upms.vo;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "upms_role")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo extends BaseEntity {

    public static final Long ROLE_ADMIN_ID = 1L;
    public static final Long ROLE_TEACHER_ID = 2L;
    public static final Long ROLE_STUDENT_ID = 3L;

    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;

    public RoleVo(Long id) {
        this.setId(id);
    }

}
