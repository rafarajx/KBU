package core

import math.vec2
import math.vec3
import math.vec4
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL45.*

object TextRenderer {
	
	val CHARACTERS = " ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789:;<=>?!\"#$%&()* +,-./";
	
	lateinit var fontProgram: ShaderProgram
	
	lateinit var fontTexture: Texture
	
	private var color = vec4(0.0f, 0.0f, 0.0f, 1.0f)
	
	var resolutionUL = 0
	var colorUL = 0
	var depthUL = 0
	
	var depth = 0.0f
	
	fun init(){
		fontTexture = Texture("./res/Font.png")
		
		
		val vertex = Shader("/shaders/text.vs.glsl", Shader.Type.VERTEX)
		//Shader *geometry = new Shader("res/shaders/text.gs.glsl", Shader::GEOMETRY);
		val fragment = Shader("/shaders/text.fs.glsl", Shader.Type.FRAGMENT)
		fontProgram = ShaderProgram(vertex, fragment)
		
		
		fontProgram.use()
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		resolutionUL = glGetUniformLocation(fontProgram.id, "resolution")
		glProgramUniform2f(fontProgram.id, resolutionUL, Window.width.toFloat(), Window.height.toFloat())
		colorUL = glGetUniformLocation(fontProgram.id, "u_color")
		depthUL = glGetUniformLocation(fontProgram.id, "depth")
	}
	
	fun draw(text: Int, pos: vec2, scale: Float = 1.0f) {
		draw(text.toString(), pos, scale)
	}
	
	fun draw(text: String, pos: vec2, scale: Float = 1.0f) {
		
		var tbo = 0
		
		val tileWidth = 8 * scale
		val tileHeight = 11 * scale
		
		val vertices = FloatArray(12 * text.length)
		val texCoords = FloatArray(12 * text.length)
		
		val sb = StringBuilder()
		
		sb.append(text)
		
		for(i in sb.indices) {
			
			when (text[i]) {
				'ą' -> sb[i] = 'a'
				'ć' -> sb[i] = 'c'
				'ę' -> sb[i] = 'e'
				'ł' -> sb[i] = 'l'
				'ń' -> sb[i] = 'n'
				'ó' -> sb[i] = 'o'
				'ś' -> sb[i] = 's'
				'ź' -> sb[i] = 'z'
				'ż' -> sb[i] = 'z'
			}
			
			val c: Float = pos.x + i * tileWidth
			val d: Float = pos.y
			
			val index = 12 * i
			
			vertices[index + 0] = c
			vertices[index + 1] = d
			vertices[index + 2] = c + tileWidth
			vertices[index + 3] = d
			vertices[index + 4] = c + tileWidth
			vertices[index + 5] = d + tileHeight
			
			vertices[index + 6] = c
			vertices[index + 7] = d
			vertices[index + 8] = c + tileWidth
			vertices[index + 9] = d + tileHeight
			vertices[index + 10] = c
			vertices[index + 11] = d + tileHeight
			
			val id = getLetterId(text[i])
			
			val a = id * (8.0f / 1000.0f)
			val b = (id + 1) * (8.0f / 1000.0f)
			
			texCoords[index + 0] = a
			texCoords[index + 1] = 0f
			texCoords[index + 2] = b
			texCoords[index + 3] = 0f
			texCoords[index + 4] = b
			texCoords[index + 5] = 1f
			
			texCoords[index + 6] = a
			texCoords[index + 7] = 0f
			texCoords[index + 8] = b
			texCoords[index + 9] = 1f
			texCoords[index + 10] = a
			texCoords[index + 11] = 1f
		}
		
		fontProgram.use()
		
		val vao = glGenVertexArrays()
		glBindVertexArray(vao)
		
		
		val vbo = Utils.createStreamArrayBuffer(vertices)
		
		val positionAL = glGetAttribLocation(fontProgram.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		
		val texCoordAL = glGetAttribLocation(fontProgram.id, "texCoord")
		if (texCoordAL != -1) {
			tbo = Utils.createStreamArrayBuffer(texCoords)
			
			glVertexAttribPointer(texCoordAL, 2, GL_FLOAT, false, 0, 0)
			glEnableVertexAttribArray(texCoordAL)
		}
		
		fontTexture.bind(0)
		
		
		glUniform2fv(resolutionUL, Window.size.array)
		glUniform4fv(colorUL, color.array)
		glUniform1f(depthUL, depth)
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.size)
		glBindVertexArray(0)
		glDeleteVertexArrays(vao)
		glDeleteBuffers(vbo)
		glDeleteBuffers(tbo)
	}
	
	private fun getLetterId(character: Char): Int{
		for (i in CHARACTERS.indices) {
			if (character == CHARACTERS[i]) {
				return i
			}
		}
		return 0
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