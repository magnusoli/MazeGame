package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Shader {

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private int globalAmbientLoc;
	//private int colorLoc;
	private int eyePosLoc;

	private int lightPositionLoc;
	private int lightColorLoc;
	private int materialDiffuseLoc;
	private int materialSpecularLoc;

	private int materialShininessLoc;
	private int materialEmissionLoc;


	public Shader()
	{
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/vertexLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/vertexLighting3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		globalAmbientLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");

		//colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");
		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");

		lightPositionLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");

		lightColorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor");

		materialDiffuseLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		materialSpecularLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");

		materialShininessLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
		materialEmissionLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSEmission");

		Gdx.gl.glUseProgram(renderingProgramID);

	}
	/*public void setColor(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(colorLoc, r, b, g, a);
	}*/
	public void setGlobalAmbient(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(globalAmbientLoc, r, b, g, a);
	}
	public void setEyePosition(float x, float y, float z, float w)
	{
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
	}
	public void setLightPosition(float x, float y, float z, float w)
	{
		Gdx.gl.glUniform4f(lightPositionLoc, x, y, z, w);
	}
	public void setLightColor(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(lightColorLoc, r, b, g, a);
	}
	public void setMaterialDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialDiffuseLoc, r, b, g, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialSpecularLoc, r, b, g, a);
	}
	public void setMaterialShininess(float shininess)
	{
		Gdx.gl.glUniform1f(materialShininessLoc, shininess);
	}
	public void setMaterialEmission(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(materialEmissionLoc, r, b, g, a);
	}
	
	
	public int getVertexPointer()
	{
		return positionLoc;
	}
	public int getNormalPointer()
	{
		return normalLoc;
	}
	public void setModelMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);

	}
	public void setViewMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);

	}
	public void setProjectionMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);

	}
}
