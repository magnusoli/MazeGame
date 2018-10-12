package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.Stack;
import java.util.Random;
public class First3DGame extends ApplicationAdapter implements InputProcessor {

	Shader shader;

	private Camera cam;
	private Camera orthoCam;

	private float speed;
	private int gridsize = 20;
	
	private Board maze;
	
	private int startX;
	private int startZ;
	private int finalX;
	private int finalZ;
	
	private Balls[] ball;
	private int countBalls;

	
	private float height = 4.0f;
	private float width = 1.0f;
	private float length = 10.0f;
	
	private float angle = 0f;
	Texture wall;


	//private ModelMatrix modelMatrix;

	@Override
	public void create () {
	
		Gdx.input.setInputProcessor(this);
		shader = new Shader();
		wall = new Texture(Gdx.files.internal("textures/wall.jpg"));

/*		
		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrixBuffer = BufferUtils.newFloatBuffer(16);
		modelMatrixBuffer.put(mm);
		modelMatrixBuffer.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
*/

		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		//ModelMatrix.main.setShaderMatrix(modelMatrixLoc);

		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		Random rand = new Random();
		createStart(rand);
		
		while(true)
		{
			if(startX == finalX && startZ == finalZ)
			{
				createFinal(rand);
			}
			else
			{
				break;
			}
		}
		cam = new Camera ();
		//cam.look(new Point3D(startX*length, 90f, startZ*length), new Point3D(startX*length, 0f, startZ*length), new Vector3D(1,0,0));
		//cam.look(new Point3D(finalX*length, 90f, finalZ*length), new Point3D(finalX*length, 0f, finalZ*length), new Vector3D(1,0,0));
		cam.perspectiveProjection(90, 1.0f, 0.3f, 200.0f);
		cam.look(new Point3D(startX*length, 0f, startZ*length), new Point3D(startX*length+10, 0f, startZ*length), new Vector3D(0,1,0));
		orthoCam = new Camera();
		generateMaze(rand);
		
		countBalls = 20;
		ball = new Balls[countBalls];
		for(int i = 0; i < countBalls; i++)
		{
			if(i == countBalls-1) {
				ball[i] = new Balls(finalX, finalZ);
			}else {
				int newX = rand.nextInt(gridsize-2);
				int newZ = rand.nextInt(gridsize-2);
				ball[i] = new Balls(newX, newZ);
			}
		}
	}
	private void generateMaze(Random rand) 
	{
		
		maze = new Board(gridsize);
		maze.cells[startX][startZ].makeVisited();
		Stack<Integer> visitedCells = new Stack<Integer>();
		
		
		visitedCells.push(startX);
		visitedCells.push(startZ);
		int xVisit = startX;
		int zVisit = startZ;
		
		
		while(!allVisited())
		{
			int check = rand.nextInt(4);
			if(northTaken(xVisit, zVisit) && southTaken(xVisit, zVisit) && westTaken(xVisit, zVisit) && eastTaken(xVisit, zVisit)){
				zVisit = visitedCells.pop();
				xVisit = visitedCells.pop();
			}
			
			if(check == 0 && !northTaken(xVisit, zVisit)){
				maze.cells[xVisit+1][zVisit].makeDownFalse();
				maze.cells[xVisit+1][zVisit].makeVisited();
				xVisit = xVisit+1;
				visitedCells.push(xVisit);
				visitedCells.push(zVisit);
				
			}
			if(check == 1 && !southTaken(xVisit, zVisit)){
				maze.cells[xVisit][zVisit].makeDownFalse();
				maze.cells[xVisit-1][zVisit].makeVisited();
				xVisit = xVisit-1;
				visitedCells.push(xVisit);
				visitedCells.push(zVisit);
			}
			if(check == 2 && !westTaken(xVisit, zVisit)){
				maze.cells[xVisit][zVisit].makeLeftFalse();
				maze.cells[xVisit][zVisit-1].makeVisited();
				zVisit = zVisit -1;
				visitedCells.push(xVisit);
				visitedCells.push(zVisit);
			}
			if(check == 3 && !eastTaken(xVisit, zVisit)){
				maze.cells[xVisit][zVisit+1].makeLeftFalse();
				maze.cells[xVisit][zVisit+1].makeVisited();
				zVisit = zVisit +1;
				visitedCells.push(xVisit);
				visitedCells.push(zVisit);
			}
			
		}
		
	}



	private boolean eastTaken(int xVisit, int zVisit) {
		// TODO Auto-generated method stub
		return (zVisit == gridsize-2 || maze.cells[xVisit][zVisit+1].getVistited());
	}
	private boolean westTaken(int xVisit, int zVisit) {
		// TODO Auto-generated method stub
		return (zVisit == 0 || maze.cells[xVisit][zVisit-1].getVistited());
	}
	private boolean southTaken(int xVisit, int zVisit) {
		// TODO Auto-generated method stub
		return (xVisit == 0 || maze.cells[xVisit-1][zVisit].getVistited());
	}
	private boolean northTaken(int xVisit, int zVisit) {
		// TODO Auto-generated method stub
		return (xVisit == gridsize-2 || maze.cells[xVisit+1][zVisit].getVistited());
	}



	private boolean allVisited() {
		// TODO Auto-generated method stub
		for(int i = 0; i < gridsize-1; i++) {
			for(int j = 0; j < gridsize-1; j++) {
				if(!maze.cells[i][j].getVistited())
				{
					return false;
				}
			}
		}
		return true;
	}

	private void createFinal(Random rand) {
		// TODO Auto-generated method stub
		finalX = rand.nextInt(gridsize-2);
		finalZ = rand.nextInt(gridsize-2);
	}

	private void createStart(Random rand) {
		// TODO Auto-generated method stub
		startX = rand.nextInt(gridsize-2);
		startZ = rand.nextInt(gridsize-2);
		finalX = rand.nextInt(gridsize-2);
		finalZ = rand.nextInt(gridsize-2);
	}

	private void input()
	{
		
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		angle += 180.0f * deltaTime;
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			//cam.slide(-speed*deltaTime, 0, 0);
			move(-speed*deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			//cam.slide(speed*deltaTime, 0, 0);
			move(speed*deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			//cam.slide(0, 0, -speed*deltaTime);
			move(0, -speed*deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			//cam.slide(0, 0, speed*deltaTime);
			move(0, speed*deltaTime);
		}
		
		
		//do all updates to the game
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.yaw(90.0f*deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.yaw(-90.0f*deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(90.0f*deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(-90.0f*deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			speed = 8;
		}else {
			speed=4;
			
		}
		for(int i = 0; i < countBalls; i++)
		{
			if(ball[i].getShow())
			{
				Point3D currPos = new Point3D(ball[i].getXpos()*length, 0, ball[i].getZpos()*length);
				Vector3D dist = Vector3D.difference(currPos, cam.eye);
				if(dist.length() < 2*width)
				{
					ball[i].dontShow();
				}
				ball[i].moveBalls(maze, gridsize, deltaTime);
			}
		}
		
	
		
	}
		/*float movX = finalXmov*deltaTime;
		float movZ = finalZmov*deltaTime;
		

		if((float)finalX - finalXpos <= 0.01f  && -(float)finalX + finalXpos <=  0.01f && (float)finalZ - finalZpos <= 0.01f && -(float)finalZ + finalZpos <= 0.01f)
		{
			chooseFinalNext();
		}else if((finalXpos > finalX && finalXmov < 0)||(finalXpos < finalX && finalXmov > 0)||(finalZpos > finalZ && finalZmov < 0)||(finalZpos < finalZ && finalZmov > 0)) {
			finalXpos = finalXpos + movX;
			finalZpos = finalZpos + movZ;
		}else {
			finalXpos = finalX;
			finalZpos = finalZ;
		}
		angle = angle + 20f*deltaTime;

	}
	private void chooseFinalNext() {
		if(finalXmov < 0 && finalX > 0){
			if(!maze.cells[finalX][finalZ].getDown())
			{
				finalX = finalX - 1;
				finalXmov = -0.5f;
				finalZmov = 0f;
				return;
			}
		}
		if(finalXmov > 0 && finalX < gridsize-1){
			if(!maze.cells[finalX+1][finalZ].getDown())
			{
				finalX = finalX + 1;
				finalXmov = 0.5f;
				finalZmov = 0f;
				return;
			}	
		}
		if(finalZmov < 0 && finalZ > 0) {
			if(!maze.cells[finalX][finalZ].getLeft())
			{
				finalZ = finalZ - 1;
				finalXmov = 0f;
				finalZmov = -0.5f;
				return;
			}
		}
		if(finalZmov > 0 && finalZ < gridsize-1) {
			if(!maze.cells[finalX][finalZ+1].getLeft())
			{
				finalZ = finalZ + 1;
				finalXmov = 0f;
				finalZmov = 0.5f;
				return;
			}	
		}
		Random rand = new Random();
		int check = rand.nextInt(4);
		if(check == 0 && finalX > 0){
			if(!maze.cells[finalX][finalZ].getDown())
			{
				finalX = finalX - 1;
				finalXmov = -0.5f;
				finalZmov = 0f;
				return;
			}
		}
		if(check == 1 && finalX < gridsize-1){
			if(!maze.cells[finalX+1][finalZ].getDown())
			{
				finalX = finalX + 1;
				finalXmov = 0.5f;
				finalZmov = 0f;
				return;
			}	
		}
		if(check == 2 && finalZ > 0) {
			if(!maze.cells[finalX][finalZ].getLeft())
			{
				finalZ = finalZ - 1;
				finalXmov = 0f;
				finalZmov = -0.5f;
				return;
			}
		}
		if(check == 3 && finalZ < gridsize-1) {
			if(!maze.cells[finalX][finalZ+1].getLeft())
			{
				finalZ = finalZ + 1;
				finalXmov = 0f;
				finalZmov = 0.5f;
				return;
			}	
		}
	}
	*/
	private void move(float inputX, float inputZ)
	{
		float x = cam.getX();
		float z = cam.getZ();
		
		
		int gridposX = 0;
		int gridposZ = 0;
		
		for(int i = 0; i < gridsize-1; i++)
		{
			if(i*length-length/2 < x && i*length+length/2 > x) 
			{
				gridposX = i;
			}
			if(i*length-length/2 < z && i*length+length/2 > z) 
			{
				gridposZ = i;
			}
		}
		
		float cellX = x - gridposX*length;
		float cellZ = z - gridposZ*length;
		boolean allowX = true;
		boolean allowZ = true;
		if(maze.cells[gridposX][gridposZ].getDown() && allowX) {
			if(cam.checkSlideX(inputX, 0, inputZ) < 0){
				if(cellX + cam.checkSlideX(inputX, 0, inputZ) < -length/2 + width*2f){
					allowX = false;
				}
			}
		}
		if(maze.cells[gridposX+1][gridposZ].getDown() && allowX) {
			if(cam.checkSlideX(inputX, 0, inputZ) > 0){
				if(cellX + cam.checkSlideX(inputX, 0, inputZ) > length/2 - width*2f){
					allowX = false;
				}
			}
		}
		if(maze.cells[gridposX][gridposZ].getLeft() && allowZ) {
			if(cam.checkSlideZ(inputX, 0, inputZ) < 0){
				if(cellZ + cam.checkSlideZ(inputX, 0, inputZ) < -length/2 + width*2f){
					allowZ = false;
				}
			}
		}
		if(maze.cells[gridposX][gridposZ+1].getLeft() && allowZ) {
			if(cam.checkSlideZ(inputX, 0, inputZ) > 0){
				if(cellZ + cam.checkSlideZ(inputX, 0, inputZ) > length/2 - width*2f){
					allowZ = false;
				}
			}
		}
		if(cam.checkSlideZ(inputX, 0, inputZ) > 0 && (cellX >= length/2 -width*2 ||  cellX <= -length/2 +width*2) && allowZ){
			if(cellZ + cam.checkSlideZ(inputX, 0, inputZ) > length/2 - width*2f){
				allowZ = false;
			}
		}
		if(cam.checkSlideZ(inputX, 0, inputZ) < 0 && (cellX >= length/2 -width*2 ||  cellX <= -length/2 +width*2) && allowZ){
			if(cellZ + cam.checkSlideZ(inputX, 0, inputZ) < -length/2 + width*2f){
				allowZ = false;
			}
		}
		if(cam.checkSlideX(inputX, 0, inputZ) > 0 && (cellZ >= length/2 -width*2 ||  cellZ <= -length/2 +width*2) && allowX){
			if(cellX + cam.checkSlideX(inputX, 0, inputZ) > length/2 - width*2f){
				allowX = false;
			}
		}
		if(cam.checkSlideX(inputX, 0, inputZ) < 0 && (cellZ >= length/2 -width*2 ||  cellZ <= -length/2 +width*2) && allowX){
			if(cellX + cam.checkSlideX(inputX, 0, inputZ) < -length/2 + width*2f){
				allowX = false;
			}
		}
		

		if(allowZ && allowX)
		{
			cam.slide(inputX, 0, inputZ);
		}else if(allowZ)
		{
			cam.slideZ(inputX, 0, inputZ);	

		}else if(allowX)
		{
			cam.slideX(inputX, 0, inputZ);

		}
			
		
		
		
		
		
	}
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);
				shader.setMaterialShininess(5.0f);
				
				
				shader.setLightPosition(length*gridsize/2, height* 5.0f, length*gridsize/2, 1.0f);
				shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);
				shader.setGlobalAmbient(0.1f, 0.1f, 0.1f, 1.0f);
				ModelMatrix.main.pushMatrix();
				
				ModelMatrix.main.addTranslation(length*gridsize/2, height* 4.0f, length*gridsize/2);
				shader.setMaterialEmission(10.0f, 10.0f, 10.0f, 1.0f);
				shader.setMaterialDiffuse(0.8f, 0.8f, 0, 1);

				ModelMatrix.main.addScale(width,width,width);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();
				ModelMatrix.main.popMatrix();


			}else
			{
				Gdx.gl.glViewport(Gdx.graphics.getWidth()*4/5, Gdx.graphics.getHeight()*4/5, Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);
				orthoCam.orthographicProjection(-40, 40, -40, 40, 0.1f, 100);

				orthoCam.look(new Point3D(cam.eye.x, 60f, cam.eye.z), new Point3D(cam.eye.x, 0f, cam.eye.z), new Vector3D(1,0,0));

				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

				shader.setEyePosition(orthoCam.eye.x, orthoCam.eye.y, orthoCam.eye.z, 1.0f);
				shader.setMaterialShininess(10000.0f);


				//shader.setLightDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
			}
		

			
			ModelMatrix.main.loadIdentityMatrix();
			
			
			

			ModelMatrix.main.pushMatrix();
			shader.setMaterialDiffuse(0.6f, 0.0f, 0.1f, 1.0f);

			
			ModelMatrix.main.addTranslation(startX*length, 0f, startZ*length);
			ModelMatrix.main.addRotationZ(angle);
			ModelMatrix.main.addRotationY(angle);
	
			ModelMatrix.main.addRotationX(angle);
	
			ModelMatrix.main.addScale(width/3, width/3, width/3);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();

			for(int i = 0; i < countBalls; i++)
			{
				if(ball[i].getShow())
				{
					ModelMatrix.main.pushMatrix();
					if(i == countBalls-1) {
						shader.setMaterialDiffuse(0.1f, 0.7f, 0f, 1.0f);
	
					}else {
						shader.setMaterialDiffuse(0.7f, 0.0f, 0.1f, 1.0f);
					}
					ModelMatrix.main.addTranslation(ball[i].getXpos()*length, 0f, ball[i].getZpos()*length);
					
					ModelMatrix.main.addScale(width, width, width);
					shader.setModelMatrix(ModelMatrix.main.getMatrix());
					SphereGraphic.drawSolidSphere();
			
					ModelMatrix.main.popMatrix();
				}
			}
			ModelMatrix.main.pushMatrix();
			shader.setMaterialDiffuse(0.8f, 0.8f, 0.8f, 1.0f);
	
			ModelMatrix.main.addTranslation(((gridsize-1)/2)*length, -height/2, ((gridsize-1)/2)*length);
			
			ModelMatrix.main.addScale((gridsize-1)*length, 0.1f, (gridsize-1)*length);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube();
			ModelMatrix.main.popMatrix();
			shader.setMaterialDiffuse(0.6f, 0.3f, 0.7f, 1.0f);
	
			for(int i = 0; i < gridsize; i++) {
				for(int j = 0; j < gridsize; j++) {
					ModelMatrix.main.pushMatrix();
					
					ModelMatrix.main.addTranslation(i*length-length/2, 0f, j*length-length/2);
					
					ModelMatrix.main.addScale(width+0.3f, height, width+0.3f);
					shader.setModelMatrix(ModelMatrix.main.getMatrix());
					BoxGraphic.drawSolidCube();
					ModelMatrix.main.popMatrix();
	
					
	
					if(maze.cells[i][j].getLeft()) {
						ModelMatrix.main.pushMatrix();
						
						ModelMatrix.main.addTranslation(i*length, 0f, j*length-length/2);
						
						ModelMatrix.main.addScale(length, height, width);
						shader.setModelMatrix(ModelMatrix.main.getMatrix());
						BoxGraphic.drawSolidCube();
		
						ModelMatrix.main.popMatrix();
					}
					if(maze.cells[i][j].getDown()) {
						ModelMatrix.main.pushMatrix();
						ModelMatrix.main.addTranslation(i*length-length/2, 0f, j*length);
						
						ModelMatrix.main.addScale(width, height, length);
						shader.setModelMatrix(ModelMatrix.main.getMatrix());
						BoxGraphic.drawSolidCube();
						//BoxGraphic.drawOutlineCube();
						//SphereGraphic.drawSolidSphere();
						//SphereGraphic.drawOutlineSphere();
						ModelMatrix.main.popMatrix();
					}
					//BoxGraphic.drawOutlineCube();
					//SphereGraphic.drawSolidSphere();
					//SphereGraphic.drawOutlineSphere();
				}
			}
			if(viewNum == 1) {
				ModelMatrix.main.pushMatrix();
				
				ModelMatrix.main.addTranslation(cam.getX(), 0f, cam.getZ());
			
				ModelMatrix.main.addScale(width*2, width*2, width*2);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
				ModelMatrix.main.pushMatrix();
				shader.setMaterialDiffuse(0f, 0f, 0f, 1.0f);
		
				ModelMatrix.main.addTranslation(((gridsize-1)/2)*length, -height/2, ((gridsize-1)/2)*length);
				
				ModelMatrix.main.addScale((gridsize)*2*length, 0.1f, (gridsize)*2*length);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube();
				ModelMatrix.main.popMatrix();
			}
		}

	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	/*private void Look3D(Point3D eye, Point3D center, Vector3D up) {
		
		Vector3D n = Vector3D.difference(eye, center);
		Vector3D u = up.cross(n);
		n.normalize();
		u.normalize();
		Vector3D v = n.cross(u);

		Vector3D minusEye = new Vector3D(-eye.x, -eye.y, -eye.z);
		
		float[] pm = new float[16];

		pm[0] = u.x; pm[4] = u.y; pm[8] = u.z; pm[12] = minusEye.dot(u);
		pm[1] = v.x; pm[5] = v.y; pm[9] = v.z; pm[13] = minusEye.dot(v);
		pm[2] = n.x; pm[6] = n.y; pm[10] = n.z; pm[14] = minusEye.dot(n);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrixBuffer);
	}*/

	/*private void OrthographicProjection3D(float left, float right, float bottom, float top, float near, float far) {
		float[] pm = new float[16];

		pm[0] = 2.0f / (right - left); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -(right + left) / (right - left);
		pm[1] = 0.0f; pm[5] = 2.0f / (top - bottom); pm[9] = 0.0f; pm[13] = -(top + bottom) / (top - bottom);
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 2.0f / (near - far); pm[14] = (near + far) / (near - far);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrixBuffer);

	}

	private void PerspctiveProjection3D() {
		float[] pm = new float[16];

		pm[0] = 1.0f; pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = 0.0f;
		pm[1] = 0.0f; pm[5] = 1.0f; pm[9] = 0.0f; pm[13] = 0.0f;
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = -1.02f; pm[14] = -2.02f;
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = -1.0f; pm[15] = 0.0f;

		matrixBuffer = BufferUtils.newFloatBuffer(16);
		matrixBuffer.put(pm);
		matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrixBuffer);

	}

	@Override
	*/
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}