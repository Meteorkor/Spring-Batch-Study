package com.example.reader;

import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class EmpReader implements ItemReader<String> {

//	static Queue<Object> q = new PriorityQueue();
	Queue<Object> q = new PriorityQueue();


	public void add(){
		for (int idx = 0; idx < 100; idx++) {
			q.add(idx);
		}
	}
	public EmpReader(){
		System.out.println("init~!");
		add();
	}
	int idx=0;
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//		if(true)
//			throw new NullPointerException();
		System.out.println( "Thread : " + Thread.currentThread().toString() );
		System.out.println( "ObjHash : " + System.identityHashCode( this ) );
		/*
		idx++;
		if(idx>10){
			return null;
		}
		*/
		// if(true) throw new NullPointerException("");
		Object obj = q.poll();
		return obj != null ? String.valueOf(obj) : null;

		// return "aaa";
	}

}
