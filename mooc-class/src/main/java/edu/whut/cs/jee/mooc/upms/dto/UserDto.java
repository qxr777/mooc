package edu.whut.cs.jee.mooc.upms.dto;

import edu.whut.cs.jee.mooc.upms.model.Role;
import edu.whut.cs.jee.mooc.upms.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDto {

    public static final String ACTOR_TEACHER  = "教师";
    public static final String ACTOR_STUDENT  = "学生";

    private Long id;

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

    private List<Role> roles;

    public User convertTo(){
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public UserDto convertFor(User user){
        BeanUtils.copyProperties(user,this);
        return this;
    }
}
