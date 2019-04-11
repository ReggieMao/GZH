package com.gzh.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/9/29.
 * 国家短信代码
 */

@Data
public class Country implements Serializable {

    private String addressCode;

    private String addressName;

    private String continent;

    private int addressPhone;

    private int id;

}
