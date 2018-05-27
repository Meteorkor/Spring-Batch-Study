package com.example.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.example.model.Emp;

public class EmpWriter implements ItemWriter<Emp>{

	@Override
	public void write(List<? extends Emp> arg0) throws Exception {
		
		System.out.println("arg0.size() : " + arg0.size() );  
		
	}

}
