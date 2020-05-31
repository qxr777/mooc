package edu.whut.cs.jee.mooc.upms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDetailVo {

    private Long id;

    @NotNull(message = "用户名不能为空")
    @Size(min = 6, max = 11, message = "账号长度必须是6-11个字符")
    @ApiModelProperty(value = "名称")
    private String name;

    @NotNull(message = "用户昵称不允许为空")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    private List<RoleVo> roles = new ArrayList<>();




}
