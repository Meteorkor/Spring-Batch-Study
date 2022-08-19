package com.meteor.batch.util;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnumInitValueTest {

    @BeforeEach
    void setUp() {
        ConfigClient.count = 0;
    }

    @Test
    void noCacheCode() {
        CodeEnum.A001.getValue();
        CodeEnum.A001.getValue();
        CodeEnum.A001.getValue();

        CodeEnum.A002.getValue();
        CodeEnum.A002.getValue();
        CodeEnum.A002.getValue();

        CodeEnum.A003.getValue();
        CodeEnum.A003.getValue();
        CodeEnum.A003.getValue();

        Assertions.assertEquals(9, ConfigClient.count);
    }

    @Test
    void cacheCode() {
        CodeCacheEnum.A001.getValue();
        CodeCacheEnum.A001.getValue();
        CodeCacheEnum.A001.getValue();

        CodeCacheEnum.A002.getValue();
        CodeCacheEnum.A002.getValue();
        CodeCacheEnum.A002.getValue();

        CodeCacheEnum.A003.getValue();
        CodeCacheEnum.A003.getValue();
        CodeCacheEnum.A003.getValue();

        Assertions.assertEquals(3, ConfigClient.count);
    }

    enum CodeEnum {
        A001, A002, A003;

        public String getValue() {
            return ConfigClient.getConfig(this.name());
        }
    }

    enum CodeCacheEnum {
        A001, A002, A003;

        private final static EnumMap<CodeCacheEnum, String> CONFIG_MAP = loadConfig();

        public String getValue() {
            return CONFIG_MAP.get(this);
        }

        public static EnumMap<CodeCacheEnum, String> loadConfig() {
            return new EnumMap<>(
                    Arrays.stream(values())
                          .collect(
                                  Collectors.toMap(Function.identity(),
                                                   code -> ConfigClient.getConfig(code.name()))
                          )
            );
        }

    }

    static class ConfigClient {
        //testData
        public static int count;

        private ConfigClient() {}

        public static String getConfig(String key) {
            count++;
            return "ConfigValue" + key;
        }
    }

}


