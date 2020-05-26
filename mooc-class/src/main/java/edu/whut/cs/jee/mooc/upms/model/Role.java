package edu.whut.cs.jee.mooc.upms.model;

import edu.whut.cs.jee.mooc.common.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "upms_role")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    public static final Integer ROLE_ADMIN_ID = 1;
    public static final Integer ROLE_TEACHER_ID = 2;
    public static final Integer ROLE_STUDENT_ID = 3;

    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;

}
