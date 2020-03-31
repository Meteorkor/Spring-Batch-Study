/*
 * Copyright (c) 2016 by TmaxSoft co., Ltd.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of TmaxSoft co., Ltd("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with TmaxSoft co., Ltd.
 */

package com.meteor.reader;

import org.springframework.batch.item.file.FlatFileItemReader;

/**
 * @author unseok.kim
 * @since  2018. 5. 27.
 */
public class FlatFileReaderTest1 extends FlatFileItemReader<String> {
    private int errorCnt=2;
    //		System.out.println("flatFileStep1 Run!")
    //
    //	protected T doRead() throws Exception {	;
    @Override
    protected String doRead() throws Exception {
        errorCnt--;
        if(errorCnt<=0) throw new NullPointerException("NPE");
        System.out.println("flatFileStep1 Run!");
        return super.doRead();
    }
}
