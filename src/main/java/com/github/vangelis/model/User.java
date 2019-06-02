package com.github.vangelis.model;

import lombok.*;

import java.io.Serializable;

/**
 * 没错这就是一个User对象
 *
 * @author Vanglies
 * @date 2019-06-02 11:11
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String sex;
    private String introduction;
}
