package com.zj.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 8741678952172142841L;

    private Long id;

    private int version;

}
