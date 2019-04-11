package com.gzh.library.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by MaoLJ on 2018/7/27.
 * 用户对应钱包金额
 */

@Data
public class Income implements Serializable {

    private boolean add;

    private double avalBalance;

    private double balance;

    private String coinImgUrl;

    private String coinName;

    private String coinUpDownNum;

    private String hexAddress;

    private int id;

    private String privateKeyPath;

    private double rmbBalance;

    private double seviceFee;

    private boolean show;

    private String status;

    private String userId;

    private String userWalletAddress;

    private String userWalletType;

    private String createDate;

    private String modifyDate;

}
