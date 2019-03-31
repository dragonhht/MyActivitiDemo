package com.github.dragonhht.activiti.model

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

/**
 * 文件主Model.
 *
 * @author: huang
 * @Date: 2019-3-29
 */
@Getter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class FileMain implements Serializable {

    private static final long serialVersionUID = 1L

    private String id
    private String name
    private File files
    private String key
    private Date createTime
    private String upload

}
