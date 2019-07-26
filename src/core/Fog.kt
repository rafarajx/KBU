package core

import entity.building.Building
import gametype.Game
import math.vec2
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL45.*

object Fog{
	
	lateinit var fogProgram: ShaderProgram
	
	var tex = 0
	var vao = 0
	var fbo = 0
	var vbo = 0
	
	var resolutionUL = 0
	var pointUL = 0
	
	private val vertices = floatArrayOf(
		-1f, -1f,
		1f, -1f,
		1f, 1f,
		
		-1f, -1f,
		1f, 1f,
		-1f, 1f
	)
	
	fun init() {
		
		fbo = glGenFramebuffers()
		glBindFramebuffer(GL_FRAMEBUFFER, fbo)
		
		tex = glGenTextures()
		glBindTexture(GL_TEXTURE_2D, tex)
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Window.width, Window.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
		
		//glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex, 0)
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex, 0)
		
		
		
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			println("framebuffer not completed")
		} else {
			println("framebuffer completed")
		}
		
		
		
		/*
		val drawbuffers = intArrayOf(GL_COLOR_ATTACHMENT0)
		glDrawBuffers(drawbuffers)
		*/
		
		
		val vert = Shader("/shaders/fog.vs.glsl", Shader.Type.VERTEX)
		val frag = Shader("/shaders/fog.fs.glsl", Shader.Type.FRAGMENT)
		fogProgram = ShaderProgram(vert, frag)
		
		fogProgram.use()
		
		
		resolutionUL = glGetUniformLocation(fogProgram.id, "resolution")
		glUniform2fv(resolutionUL, Window.size.array)
		pointUL = glGetUniformLocation(fogProgram.id, "point")
		
		vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		vbo = glCreateBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STREAM_DRAW)
		
		val positionAL = glGetAttribLocation(fogProgram.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		
		glBindVertexArray(0)
		glBindFramebuffer(GL_FRAMEBUFFER, 0)
		
	}
	
	fun render(list: ArrayList<Building>){
		
		//render to a texture
		glBindFramebuffer(GL_FRAMEBUFFER, fbo)
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
		GL11.glViewport(0, 0, Window.width, Window.height)
		//GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
		
		fogProgram.use()
		glBindVertexArray(vao)
		glUniform2fv(resolutionUL, Window.size.array)
		
		
		for(i in list.indices) {
			//print("${list[i].p}  ")
			glUniform2fv(pointUL, (list[i].p + Game.camera).array)
			glDrawArrays(GL_TRIANGLES, 0, 6)
		}
		//println()
		
		glBindVertexArray(0)
		
		
		
		
		
		//render to the screen
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0)
		
		AlphaInverter.render(tex)
		
		//ImageRenderer.draw(tex, vec2(0), Window.size)
	}
	
}