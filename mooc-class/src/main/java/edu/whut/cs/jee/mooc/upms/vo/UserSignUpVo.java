package edu.whut.cs.jee.mooc.upms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserSignUpVo {

    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty(value = "教师：2，学生：3")
    private Long role;

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

    private String salaryNo;

    private String title;

    private String studentNo;

}
