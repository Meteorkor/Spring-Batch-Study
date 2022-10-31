package com.meteor.batch.basic.stream.dummy;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.meteor.batch.basic.stream.vo.Customer;
import com.meteor.batch.basic.stream.vo.Customer.Grade;
import com.meteor.batch.basic.stream.vo.Customer.MonthlyPurchaseInfo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDummy {

    public static List<Customer> noL1List() {
        //L2:2, L3:2, L4:2
        return Lists.newArrayList(
                createGradeNoPurchaseInfo(Grade.L2)
                , createGradeNoPurchaseInfo(Grade.L2)
                , createGradeNoPurchaseInfo(Grade.L3)
                , createGradeNoPurchaseInfo(Grade.L4)
                , createGradeNoPurchaseInfo(Grade.L4)
                , createGradeNoPurchaseInfo(Grade.L3)
        );
    }

    public static List<Customer> l1L2ExistList() {
        //L1:1, L2:2, L3:2, L4:2
        return Lists.newArrayList(
                createGradeNoPurchaseInfo(Grade.L1)
                , createGradeNoPurchaseInfo(Grade.L2)
                , createGradeNoPurchaseInfo(Grade.L2)
                , createGradeNoPurchaseInfo(Grade.L3)
                , createGradeNoPurchaseInfo(Grade.L4)
                , createGradeNoPurchaseInfo(Grade.L4)
                , createGradeNoPurchaseInfo(Grade.L3)
        );
    }

    /**
     * <pre>
     *     Grade.L1
     *     210101
     *     210102
     *     210103
     * </pre>
     * @return
     */
    public static Customer getCustomerSample() {
        return Customer.builder()
                       .grade(Grade.L1)
                       .name("kim")
                       .monthlyPurchaseInfoList(
                               Lists.newArrayList(
                                       MonthlyPurchaseInfo.builder()
                                                          .sum(100L)
                                                          .yyyyMM("210101")
                                                          .build()
                                       , MonthlyPurchaseInfo.builder()
                                                            .sum(100L)
                                                            .yyyyMM("210102")
                                                            .build()
                                       , MonthlyPurchaseInfo.builder()
                                                            .sum(100L)
                                                            .yyyyMM("210103")
                                                            .build()
                               )
                       ).build();
    }

    private static final Customer createGradeNoPurchaseInfo(Grade grade) {
        return Customer.builder()
                       .name(UUID.randomUUID().toString())
                       .grade(grade)
                       .build();
    }

}
