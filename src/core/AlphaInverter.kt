package core

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL45.*

object AlphaInverter {
	
	lateinit var program: ShaderProgram
	
	var vao = 0
	var vbo = 0
	var resolutionUL = 0
	
	val vertices = floatArrayOf(
		-1f, -1f,
		1f, -1f,
		1f, 1f,
		
		-1f, -1f,
		1f, 1f,
		-1f, 1f
	)
	
	val texCoords = floatArrayOf(
		0f, 0f,
		1f, 0f,
		1f, 1f,
		
		0f, 0f,
		1f, 1f,
		0f, 1f
	)
	
	fun init(){
		
		val vert = Shader("/shaders/alphainverter.vs.glsl", Shader.Type.VERTEX)
		val frag = Shader("/shaders/alphainverter.fs.glsl", Shader.Type.FRAGMENT)
		program = ShaderProgram(vert, frag)
		
		program.use()
		
		vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		vbo = Utils.createStreamArrayBuffer(vertices)
		
		val positionAL = glGetAttribLocation(program.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		
		val texCoordAL = glGetAttribLocation(program.id, "texCoord")
		
		var tbo = 0
		if (texCoordAL != -1) {
			tbo = Utils.createStreamArrayBuffer(texCoords)
			
			glVertexAttribPointer(texCoordAL, 2, GL_FLOAT, false, 0, 0)
			glEnableVertexAttribArray(texCoordAL)
		}
		
		resolutionUL = GL20.glGetUniformLocation(program.id, "resolution")
		
		glBindVertexArray(0)
		
	}
	
	fun render(textureId: Int){
		
		glBindVertexArray(vao)
		
		program.use()
		
		//GL20.glUniform2fv(resolutionUL, Window.size.array)
		
		glBindTexture(GL_TEXTURE_2D, textureId)
		glActiveTexture(GL_TEXTURE0)
		
		glDrawArrays(GL11.GL_TRIANGLES, 0, 6)
		
	}
	
	
}