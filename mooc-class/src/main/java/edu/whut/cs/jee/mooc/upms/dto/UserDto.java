package edu.whut.cs.jee.mooc.upms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
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

    private List<RoleDto> roles = new ArrayList<>();

    public void addRole(RoleDto role) {
        roles.add(role);
    }

    public UserDto() {
    }

    public UserDto(Long id, @NotNull(message = "用户名不能为空") @Size(min = 6, max = 11, message = "账号长度必须是6-11个字符") String name, @NotNull(message = "用户昵称不允许为空") String nickname, @NotNull(message = "用户密码不能为空") @Size(min = 6, max = 16, message = "密码长度必须是6-16个字符") String password, @Email(message = "邮箱格式不正确") String email, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
    public List<String> getRoleNames() {
        List<String> roleStrings = new ArrayList<>();
        roles.stream().forEach(role ->{
            roleStrings.add(role.getName());
        });
        return roleStrings;
    }
}
