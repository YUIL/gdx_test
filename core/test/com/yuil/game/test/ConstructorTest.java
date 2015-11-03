package com.yuil.game.test;

public class ConstructorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConstructorTest t=new ConstructorTest();
		ClassSub c=t.new ClassSub(1);
	}

	public class ClassBase{
		public ClassBase(){
			System.out.println("base");
		}
	}
	public class ClassSub extends ClassBase{
		public ClassSub(){
			
		}
		public ClassSub(int i){
			
		}
	}
}
