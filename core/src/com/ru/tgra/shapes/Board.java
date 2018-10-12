package com.ru.tgra.shapes;

public class Board {
	public Cell[][] cells;
	
	public Board(int gridsize)
	{
		cells = new Cell[gridsize][gridsize];
		
		for(int i = 0; i < gridsize; i++) {
			for(int j = 0; j < gridsize; j++) {
				cells[i][j] = new Cell();
				if((i != 0 && j == gridsize-1) || (i != gridsize-1 && j == gridsize-1))
				{
					cells[i][j].makeDownFalse();
				}
				if((j != 0 && i == gridsize-1) || (j != gridsize-1 && i == gridsize-1))
				{
					cells[i][j].makeLeftFalse();
				}
			}
		}
		
	}
}
