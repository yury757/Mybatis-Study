package org.yuyr757.pojo;

import lombok.*;

import java.util.Date;

/**
 * 虽然方便，但是不推荐使用
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lombok {
    private User user;
    private int age;
    private Date birthday;
    private String address;
}
