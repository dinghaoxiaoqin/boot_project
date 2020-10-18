package com.rrk.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Setter
@Getter
public class UserRed implements Serializable {

    private Long userId;

    private String nickName;

    private Long redId;
}
