package org.mydotey.ai.site.auth.application.command;

import lombok.Data;

/**
 * 登录命令
 *
 * @author AI-Site
 */
@Data
public class LoginCommand {

    private String username;
    private String password;
}