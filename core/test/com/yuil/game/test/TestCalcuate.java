package com.yuil.game.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class TestCalcuate {
	Calcuate cal;

	@Before
	public void setUp(){
		System.out.println("set");
		cal=new Calcuate();
	}

	@Test
	public void testAdd(){
		
		int rel=cal.add(12, 22);
		assertEquals(rel, 34);
	}
	
	@Test
	public void testAdd2(){
		
		int rel=cal.add(12, 22);
		assertEquals(rel, 324);
	}
	
}
