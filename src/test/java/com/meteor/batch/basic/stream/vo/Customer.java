package com.meteor.batch.basic.stream.vo;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class Customer {

    private final String name;
    private final Grade grade;
    @Default
    private final List<MonthlyPurchaseInfo> monthlyPurchaseInfoList = Collections.emptyList();

    public enum Grade {
        L1, L2, L3, L4
    }

    @Builder
    @Getter
    public static class MonthlyPurchaseInfo {
        private final String yyyyMM;
        private final long sum;
    }

}
