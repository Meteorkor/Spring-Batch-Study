package com.meteor.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.meteor.model.Emp;

public class EmpWriter implements ItemWriter<Emp>{

	@Override
	public void write(List<? extends Emp> arg0) throws Exception {
		
	    for(Emp emp : arg0){
	        System.out.println( "emp.toString() : " + emp.toString() );
	    }
	    
		System.out.println("arg0.size() : " + arg0.size() );  
		
	}

}
