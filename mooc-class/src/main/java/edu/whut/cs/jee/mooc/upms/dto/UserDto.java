package edu.whut.cs.jee.mooc.upms.dto;

import edu.whut.cs.jee.mooc.upms.model.Teacher;
import edu.whut.cs.jee.mooc.upms.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    public static final String ACTOR_TEACHER  = "教师";
    public static final String ACTOR_STUDENT  = "学生";

    private Long id;

    private String actor;   // 角色

    @NotNull(message = "用户名不能为空")
    @Size(min = 6, max = 11, message = "账号长度必须是6-11个字符")
    @ApiModelProperty(value = "名称")
    private String name;

    @NotNull(message = "用户昵称不允许为空")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须是6-16个字符")
    private String password;

    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotNull(message = "学号不允许为空")
    @Length(max = 20, min = 6)
    private String studentNo;

    @NotNull(message = "工资号不允许为空")
    @Length(max = 20, min = 6)
    private String salaryNo;

    private String title;

    public User convertTo(){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public UserDto convertFor(User user){
        if (user instanceof Teacher) {
            this.actor = ACTOR_TEACHER;
        } else {
            this.actor = ACTOR_STUDENT;
        }
        BeanUtils.copyProperties(user,this);
        return this;
    }
}
