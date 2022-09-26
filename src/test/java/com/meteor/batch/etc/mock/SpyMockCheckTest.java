package com.meteor.batch.etc.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.meteor.batch.reader.vo.FileReaderItem;

public class SpyMockCheckTest {

    @Test
    void mockingNotDeclareTest() {
        final String TEST_DATA = "testData";
        final FileReaderItem spyItem = Mockito.mock(FileReaderItem.class);

        Assertions.assertNull(spyItem.getData());

        Mockito.when(spyItem.getData()).thenReturn(TEST_DATA);

        Assertions.assertEquals(TEST_DATA, spyItem.getData());

        Mockito.verify(spyItem, Mockito.times(2)).getData();
    }

    @Test
    void spyMockingTest() {
        final String BEFORE_DATA = "beforeData";
        final String TEST_DATA = "testData";
        final FileReaderItem spyItem = Mockito.spy(FileReaderItem.builder().data(BEFORE_DATA).build());

        Assertions.assertEquals(BEFORE_DATA, spyItem.getData());

        Mockito.when(spyItem.getData()).thenReturn(TEST_DATA);

        Assertions.assertEquals(TEST_DATA, spyItem.getData());

        Mockito.verify(spyItem, Mockito.times(2)).getData();
    }

    @Test
    void spyCallCheckTest() {
        final FileReaderItem spyItem = Mockito.spy(FileReaderItem.builder().build());
        spyItem.getData();
        Mockito.verify(spyItem, Mockito.times(1)).getData();
    }

}
