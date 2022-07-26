package com.meteor.batch.reader.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Emp {
    private Integer empno;
    private String ename;
}
