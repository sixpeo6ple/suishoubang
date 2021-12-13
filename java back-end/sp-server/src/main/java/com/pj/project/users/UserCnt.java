package com.pj.project.users;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author WhiteRunner
 * @create 2021-12-11 16:39
 */
@Data
public class UserCnt {
    @TableField("is_student")
    private boolean isStudent;
    private Integer cnt;
}
