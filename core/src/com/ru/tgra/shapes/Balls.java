package com.ru.tgra.shapes;

import java.util.Random;

public class Balls {
	private int ballX;
	private int ballZ;
	private int ballXprev;
	private int ballZprev;
	private float ballXpos;
	private float ballZpos;
	
	private float ballXmov;
	private float ballZmov;
	private boolean show;
	Balls(int x,int z)
	{
		ballX = x;
		ballZ = z;
		ballXprev = -1;
		ballZprev = -1;
		ballXpos = 0;
		ballZpos = 0;
		ballXmov = 0;
		ballZmov = 0;
		show = true;
	}
	public void dontShow()
	{
		show = false;
	}
	public boolean getShow()
	{
		return show;
	}
	public float getXpos()
	{
		return this.ballXpos;
	}
	public float getZpos()
	{

		return this.ballZpos;
	}
	public void moveBalls(Board maze, int gridsize, float deltaTime) {
		float movX = ballXmov*deltaTime;
		float movZ = ballZmov*deltaTime;
		

		if((float)ballX - ballXpos <= 0.01f  && -(float)ballX + ballXpos <=  0.01f && (float)ballZ - ballZpos <= 0.01f && -(float)ballZ + ballZpos <= 0.01f)
		{
			chooseNext(maze,gridsize);
		}else if((ballXpos > ballX && ballXmov < 0)||(ballXpos < ballX && ballXmov > 0)||(ballZpos > ballZ && ballZmov < 0)||(ballZpos < ballZ && ballZmov > 0)) {
			ballXpos = ballXpos + movX;
			ballZpos = ballZpos + movZ;
		}else {
			ballXpos = ballX;
			ballZpos = ballZ;
		}
	}
	private void chooseNext(Board maze, int gridsize) {
		
		Random rand = new Random();
		int count = 0;
		if(ballX > 0 && !maze.cells[ballX][ballZ].getDown())
		{
			count = count + 1;
		}
			
		if(ballX < gridsize-1 && !maze.cells[ballX+1][ballZ].getDown())
		{
			count = count + 1;

		}	
			
		if(ballZ > 0 && !maze.cells[ballX][ballZ].getLeft())
		{
			count = count + 1;

		}
			
		if(ballZ < gridsize-1 && !maze.cells[ballX][ballZ+1].getLeft())
		{
			count = count + 1;
		}	
			
			
			while(true) {	
				int check = rand.nextInt(4);
				if(check == 0 && ballX > 0 && (ballXprev+1 != ballX || count == 1)){
					if(!maze.cells[ballX][ballZ].getDown())
					{
						ballXprev = ballX;
						ballZprev = -1;
						ballX = ballX - 1;
						ballXmov = -0.5f;
						ballZmov = 0f;
						return;
					}
				}
				if(check == 1 && ballX < gridsize-1 && (ballXprev-1 != ballX || count == 1)){
					if(!maze.cells[ballX+1][ballZ].getDown())
					{
						ballXprev = ballX;
						ballZprev = -1;
						ballX = ballX + 1;
						ballXmov = 0.5f;
						ballZmov = 0f;
						return;
					}	
				}
				if(check == 2 && ballZ > 0 && (ballZprev+1 != ballZ || count == 1)) {
					if(!maze.cells[ballX][ballZ].getLeft())
					{
						ballZprev = ballZ;
						ballXprev = -1;
						ballZ = ballZ - 1;
						ballXmov = 0f;
						ballZmov = -0.5f;
						return;
					}
				}
				if(check == 3 && ballZ < gridsize-1 && (ballZprev-1 != ballZ || count == 1)) {
					if(!maze.cells[ballX][ballZ+1].getLeft())
					{
						ballZprev = ballZ;
						ballXprev = -1;
						ballZ = ballZ + 1;
						ballXmov = 0f;
						ballZmov = 0.5f;
						return;
					}	
				}
			}
		}
	
}
