package com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    private String phoneNo;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 描述
     */
    private String description;

    private Timestamp createTime;

    private Timestamp updateTime;

    int isDelete;
}
