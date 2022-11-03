package com.meteor.batch.basic.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.meteor.batch.basic.stream.dummy.CustomerDummy;
import com.meteor.batch.basic.stream.vo.Customer;
import com.meteor.batch.basic.stream.vo.Customer.Grade;
import com.meteor.batch.basic.stream.vo.Customer.MonthlyPurchaseInfo;

public class CustomerExTest {

    @Test
    @DisplayName("한종류의 아이템 리스트를 필터링")
    void pickL1Customer() {
        final List<Customer> l1List = CustomerDummy.l1L2ExistList()
                                                   .stream()
                                                   .filter(customer -> customer.getGrade() == Grade.L1)
                                                   .collect(Collectors.toList());

        Assertions.assertEquals(1, l1List.size());

        //assertions에만 필요하기 때문에 localVariable도 제거해도 됨
        final List<Customer> l1ListNoExist = CustomerDummy.noL1List()
                                                          .stream()
                                                          .filter(customer -> customer.getGrade() == Grade.L1)
                                                          .collect(Collectors.toList());

        Assertions.assertEquals(0, l1ListNoExist.size());
    }

    @Test
    @DisplayName("한종류의 아이템 리스트를 필터링-old")
    void pickL1Customer_old() {
        List<Customer> l1List = new ArrayList<>();
        for (Customer customer : CustomerDummy.l1L2ExistList()) {
            if (customer.getGrade() == Grade.L1) {
                l1List.add(customer);
            }
        }

        Assertions.assertEquals(1, l1List.size());

        List<Customer> l1ListNoExist = new ArrayList<>();
        for (Customer customer : CustomerDummy.l1L2ExistList()) {
            if (customer.getGrade() == Grade.L1) {
                l1List.add(customer);
            }
        }

        Assertions.assertEquals(0, l1ListNoExist.size());
    }

    @Test
    @DisplayName("두종류(한종류 이상)의 아이템 리스트를 필터링")
    void pick1and2Customer() {
        final List<Customer> l1List = CustomerDummy.l1L2ExistList()
                                                   .stream()
                                                   .filter(customer -> customer.getGrade() == Grade.L1
                                                                       || customer.getGrade() == Grade.L2)
                                                   .collect(Collectors.toList());

        Assertions.assertEquals(1 + 2, l1List.size());

        //assertions에만 필요하기 때문에 localVariable도 제거해도 됨
        final List<Customer> l1ListNoExist = CustomerDummy.noL1List()
                                                          .stream()
                                                          .filter(customer ->
                                                                          customer.getGrade() == Grade.L1
                                                                          || customer.getGrade() == Grade.L2)
                                                          .collect(Collectors.toList());

        Assertions.assertEquals(0 + 2, l1ListNoExist.size());
    }

    @Test
    @DisplayName("두종류(한종류 이상)의 아이템 리스트를 필터링-old")
    void pick1and2Customer_old() {
        final List<Customer> l1L2List = new ArrayList<>();
        for (Customer customer : CustomerDummy.l1L2ExistList()) {
            if (customer.getGrade() == Grade.L1 || customer.getGrade() == Grade.L2) {
                l1L2List.add(customer);
            }
        }

        Assertions.assertEquals(1 + 2, l1L2List.size());

        final List<Customer> l1ListNoExist = new ArrayList<>();
        for (Customer customer : CustomerDummy.noL1List()) {
            if (customer.getGrade() == Grade.L1 || customer.getGrade() == Grade.L2) {
                l1ListNoExist.add(customer);
            }
        }

        Assertions.assertEquals(0 + 2, l1ListNoExist.size());
    }

    @Test
    @DisplayName("중복되지 않은 데이터에 대해 필터링 하여 획득")
    void getPurChaseInfoPick1MonthCustomer() {
        final Optional<MonthlyPurchaseInfo> firstMonthInfoOptional = CustomerDummy.getCustomerSample()
                                                                                  .getMonthlyPurchaseInfoList()
                                                                                  .stream().filter(
                        purchaseInfo -> "210101".equals(purchaseInfo.getYyyyMM()))
                                                                                  .findFirst();

        long firstMonthSum = firstMonthInfoOptional.map(MonthlyPurchaseInfo::getSum)
                                                   .orElse(0L);

        Assertions.assertEquals(100L, firstMonthSum);
    }

    @Test
    @DisplayName("중복되지 않은 데이터에 대해 필터링 하여 획득-old")
    void getPurChaseInfoPick1MonthCustomer_old() {
        MonthlyPurchaseInfo monthlyPurchaseInfo210101 = null;
        for (MonthlyPurchaseInfo monthlyPurchaseInfo : CustomerDummy.getCustomerSample()
                                                                    .getMonthlyPurchaseInfoList()) {
            if ("210101".equals(monthlyPurchaseInfo.getYyyyMM())) {
                monthlyPurchaseInfo210101 = monthlyPurchaseInfo;
                //setting firstMonthSum
                break;
            }
        }

        long firstMonthSum = monthlyPurchaseInfo210101 != null ? monthlyPurchaseInfo210101.getSum() : 0L;

        Assertions.assertEquals(100L, firstMonthSum);
    }

    @Test
    @DisplayName("중복되지 않은 데이터에 대해 필터링 하여 획득")
    void getPurChaseInfoPickMonthsCustomer() {
        final Map<String, MonthlyPurchaseInfo> collect = CustomerDummy.getCustomerSample()
                                                                      .getMonthlyPurchaseInfoList()
                                                                      .stream().collect(Collectors.toMap(
                        MonthlyPurchaseInfo::getYyyyMM, Function.identity()
                ));

        Assertions.assertEquals(100L, collect.get("210101").getSum());
        Assertions.assertEquals(100L, collect.get("210102").getSum());
        Assertions.assertEquals(100L, collect.get("210103").getSum());
        Assertions.assertNull(collect.get("XXXXX"));
    }

    @Test
    @DisplayName("중복되지 않은 데이터에 대해 필터링 하여 획득-old")
    void getPurChaseInfoPickMonthsCustomer_old() {
        Map<String, MonthlyPurchaseInfo> map = new HashMap<>();
        for (MonthlyPurchaseInfo monthlyPurchaseInfo : CustomerDummy.getCustomerSample()
                                                                    .getMonthlyPurchaseInfoList()) {
            map.put(monthlyPurchaseInfo.getYyyyMM(), monthlyPurchaseInfo);
        }

        Assertions.assertEquals(100L, map.get("210101").getSum());
        Assertions.assertEquals(100L, map.get("210102").getSum());
        Assertions.assertEquals(100L, map.get("210103").getSum());
        Assertions.assertNull(map.get("XXXXX"));
    }

}
