package org.mydotey.ai.site.auth.interfaces.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 用户请求
 *
 * @author AI-Site
 */
@Data
public class UserRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度3-50字符")
    private String username;

    /**
     * 密码
     */
    @Size(min = 6, max = 100, message = "密码长度6-100字符")
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称最多50字符")
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;
}
