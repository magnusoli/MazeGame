package com.ru.tgra.shapes;

public class Cell {
	private boolean left;
	private boolean down;
	private boolean visited;


	public Cell()
	{
		this.left = true;
		this.down = true;
		this.visited = false;
	}
	public void makeLeftFalse()
	{
		this.left = false;
	}
	public void makeDownFalse()
	{
		this.down = false;
	}
	public void makeVisited()
	{
		this.visited = true;
	}
	public boolean getDown()
	{
		return this.down;
	}

	public boolean getLeft()
	{
		return this.left;
	}
	public boolean getVistited()
	{
		return this.visited;
	}
	



}