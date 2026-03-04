package org.mydotey.ai.site.auth.command;

import lombok.Data;

/**
 * 注册命令
 *
 * @author AI-Site
 */
@Data
public class RegisterCommand {

    private String username;
    private String password;
    private String email;
    private String nickname;
}