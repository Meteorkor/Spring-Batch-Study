package com.meteor.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.meteor.model.Emp;

public class EmpItemProcessor implements ItemProcessor<String, Emp>{
	 private static final Logger log = LoggerFactory.getLogger(EmpItemProcessor.class);

	@Override
	public Emp process(String arg0) throws Exception {
		System.out.println( "TTTThread.currentThread().toString() : " + Thread.currentThread().toString() );
		if(arg0!=null){
			
			Emp emp = new Emp();
			emp.setEname(arg0);
			System.out.println("emp.toString()  :" + emp.toString());
//			log.debug( "emp.toString()  :" + emp.toString() );
			
			return emp;	
		}else{
			return null;
		}
		
	}


}
