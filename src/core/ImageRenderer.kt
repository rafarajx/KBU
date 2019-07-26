package core

import math.vec2
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL45.*

object ImageRenderer {
	
	lateinit var imageProgram: ShaderProgram
	var resolutionUL = 0
	var depthUL = 0
	
	var depth = 0.0f
	
	fun init(){
		val vertex = Shader("/shaders/image.vs.glsl", Shader.Type.VERTEX)
		//Shader *geometry = new Shader("res/shaders/text.gs.glsl", Shader::GEOMETRY);
		val fragment = Shader("/shaders/image.fs.glsl", Shader.Type.FRAGMENT)
		imageProgram = ShaderProgram(vertex, fragment)
		
		
		imageProgram.use()
		
		resolutionUL = glGetUniformLocation(imageProgram.id, "resolution")
		glUniform2fv(resolutionUL, Window.size.array)
		
		depthUL = glGetUniformLocation(imageProgram.id, "depth")
	}
	
	fun draw(image: Texture, pos: vec2){
		draw(image, pos, image.size)
	}
	
	fun draw(image: Texture, pos: vec2, size: vec2){
		draw(image.id, pos, size)
	}
	
	fun draw(texture: Int, pos: vec2, size: vec2){
		
		val a = pos.x
		val b = pos.y
		val c = pos.x + size.x
		val d = pos.y + size.y
		
		val vertices = floatArrayOf(
			a, b,
			c, b,
			c, d,
			
			a, b,
			c, d,
			a, d
		)
		
		val texCoords = floatArrayOf(
			0f, 0f,
			1f, 0f,
			1f, 1f,
			
			0f, 0f,
			1f, 1f,
			0f, 1f
		)
		
		imageProgram.use()
		
		val vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		val vbo = Utils.createStreamArrayBuffer(vertices)
		
		val positionAL = glGetAttribLocation(imageProgram.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		
		val texCoordAL = glGetAttribLocation(imageProgram.id, "texCoord")
		
		var tbo = 0
		if (texCoordAL != -1) {
			tbo = Utils.createStreamArrayBuffer(texCoords)
			
			glVertexAttribPointer(texCoordAL, 2, GL_FLOAT, false, 0, 0)
			glEnableVertexAttribArray(texCoordAL)
		}
		glBindTexture(GL_TEXTURE_2D, texture)
		glActiveTexture(GL_TEXTURE0)
		
		glUniform2fv(resolutionUL, Window.size.array)
		glUniform1f(depthUL, depth)
		
		glDrawArrays(GL_TRIANGLES, 0, 6)
		
		
		glBindVertexArray(0)
		glDeleteVertexArrays(vao)
		
		glDeleteBuffers(vbo)
		glDeleteBuffers(tbo)
		
	}
}