package com.xyy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WhiteRunner
 * @create 2021-11-29 12:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicUploadArgs {
    private String pic;
    private String name;
    //private String type;
}
