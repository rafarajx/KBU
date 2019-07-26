package core

import math.vec2
import math.vec3
import math.vec4
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL45.*

object RectRenderer {
	
	private var color = vec4(0.0f, 0.0f, 0.0f, 1.0f)
	
	lateinit var program: ShaderProgram
	lateinit var drawprogram: ShaderProgram
	
	object filldata{
		var resolutionUL = 0
		var depthUL = 0
		var positionAL = 0
		var colorAL = 0
	}
	object drawdata{
		var resolutionUL = 0
		var depthUL = 0
		var positionAL = 0
		var colorAL = 0
	}
	
	var depth = 0.0f
	
	fun init(){
		var vertex = Shader("/shaders/fillrect.vs.glsl", Shader.Type.VERTEX)
		var fragment = Shader("/shaders/fillrect.fs.glsl", Shader.Type.FRAGMENT)
		program = ShaderProgram(vertex, fragment)
		
		program.use()
		
		filldata.positionAL = GL20.glGetAttribLocation(program.id, "position")
		filldata.colorAL = GL20.glGetAttribLocation(program.id, "color")
		
		filldata.resolutionUL = glGetUniformLocation(program.id, "resolution")
		filldata.depthUL = glGetUniformLocation(program.id, "depth")
		glProgramUniform2f(program.id, filldata.resolutionUL, Window.width.toFloat(), Window.height.toFloat())
		
		
		
		
		vertex = Shader("/shaders/drawrect.vs.glsl", Shader.Type.VERTEX)
		fragment = Shader("/shaders/drawrect.fs.glsl", Shader.Type.FRAGMENT)
		drawprogram = ShaderProgram(vertex, fragment)
		
		drawprogram.use()
		
		drawdata.positionAL = glGetAttribLocation(drawprogram.id, "position")
		drawdata.colorAL = glGetAttribLocation(drawprogram.id, "color")
		
		drawdata.resolutionUL = glGetUniformLocation(drawprogram.id, "resolution")
		drawdata.depthUL = glGetUniformLocation(drawprogram.id, "depth")
		glProgramUniform2f(drawprogram.id, drawdata.resolutionUL, Window.width.toFloat(), Window.height.toFloat())
	}
	
	fun draw(pos: vec2, size: vec2, linewidth: Float = 1.0f){
		
		val a = pos.x
		val b = pos.y
		val c = pos.x + size.x
		val d = pos.y + size.y
		
		val vertices = floatArrayOf(
			a, b,
			c, b,
			c, d,
			a, d,
			a, b
		)
		
		val colors = floatArrayOf(
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a
		)
		
		drawprogram.use()
		
		GL11.glLineWidth(linewidth)
		
		val vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		val vbo = Utils.createStreamArrayBuffer(vertices)
		glVertexAttribPointer(drawdata.positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(drawdata.positionAL)
		
		var cbo = 0
		if (drawdata.colorAL != -1) {
			cbo = Utils.createStreamArrayBuffer(colors)
			
			glVertexAttribPointer(drawdata.colorAL, 4, GL_FLOAT, false, 0, 0)
			glEnableVertexAttribArray(drawdata.colorAL)
		}
		
		glUniform2fv(drawdata.resolutionUL, Window.size.array)
		glUniform1f(drawdata.depthUL, depth)
		
		glDrawArrays(GL_LINE_STRIP, 0, 5)
		
		glDeleteVertexArrays(vao)
		glBindVertexArray(0)
		
		glDeleteBuffers(vbo)
		glDeleteBuffers(cbo)
	}
	
	fun fill(){
		fill(vec2(0), Window.size)
	}
	
	fun fill(pos: vec2, size: vec2){
		
		
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
		
		val colors = floatArrayOf(
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a,
			color.r, color.g, color.b, color.a
		)
		
		program.use()
		
		val vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		
		val vbo = Utils.createStreamArrayBuffer(vertices)
		
		glVertexAttribPointer(filldata.positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(filldata.positionAL)
		
		var cbo = 0
		if (filldata.colorAL != -1) {
			cbo = Utils.createStreamArrayBuffer(colors)
			
			glVertexAttribPointer(filldata.colorAL, 4, GL_FLOAT, false, 0, 0)
			glEnableVertexAttribArray(filldata.colorAL)
		}
		
		glUniform2fv(filldata.resolutionUL, Window.size.array)
		glUniform1f(filldata.depthUL, depth)
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.size)
		
		glDeleteVertexArrays(vao)
		glBindVertexArray(0)
		
		
		glDeleteBuffers(vbo)
		glDeleteBuffers(cbo)
		
	}
	
	
	fun setColor(color: vec4) {
		this.color = color
	}
	
	fun setColor(color: vec3) {
		this.color = vec4(color, 1.0f)
	}
	
	fun setColor(value: Float) {
		setColor(value, value, value, 1.0f)
	}
	
	fun setColor(v1: Float, v2: Float) {
		setColor(v1, v1, v1, v2)
	}
	
	fun setColor(r: Float, g: Float, b: Float) {
		color = vec4(r, g, b, 1.0f)
	}
	
	fun setColor(r: Float, g: Float, b: Float, a: Float) {
		color = vec4(r, g, b, a)
	}
}