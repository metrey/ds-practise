package com.seuksa.distributed.tcpexample;


import java.util.*;
import java.io.*;

public class MyDate implements Serializable{
	private Date date_;
	private int number_;
	static int i = 0;
	MyDate(){
		date_ = new Date();
		number_ = i++;
	}
	
	public Date getDate(){
		return date_;
	}
	
	public int getNumber(){
		return number_;
	}
}
