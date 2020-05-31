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

    @Column(name = "name", unique = true, nullable = false, length = 20)
    private String name;

    public Role(Long id) {
        this.setId(id);
    }

}
