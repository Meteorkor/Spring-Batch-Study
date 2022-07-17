package com.meteor.batch.reader.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class FileReaderItem {
    private final String name;
    private final String data;
}
