package com.ascendant.snake;

public class Main {

	private static Snake snake;

	private static void CreateNewClassInstance() { 
		snake = new Snake(); 
	}
	
	public static Snake getSnake() { 
		return snake; 
	}
	
	public static void main(String[] args) {
		CreateNewClassInstance(); 
	}
} /// END

//Created by Ascendant
