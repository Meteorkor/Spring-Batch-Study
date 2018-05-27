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

package com.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

/**
 * @author unseok.kim
 * @since  2017. 1. 16.
 */

public class RangePartitioner implements Partitioner {

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		System.out.println("rangePartition Test");
	    Map<String, ExecutionContext> result
                       = new HashMap<String, ExecutionContext>();
		int range = 10;
		int fromId = 1;
		int toId = range;
		for (int i = 1; i <= gridSize; i++) {
			ExecutionContext value = new ExecutionContext();
			value.putInt("fromId", fromId);
			value.putInt("toId", toId);
			value.putString("name", "Thread" + i);
			result.put("partition" + i, value);
			fromId = toId + 1;
			toId += range;
		}

		return result;
	}

}
