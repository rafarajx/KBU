package core

import org.lwjgl.opengl.GL44.*

class Shader(filename: String, type: Type){
	
	enum class Type(val id: Int) {
		VERTEX(GL_VERTEX_SHADER),
		TESSELLATION_CONTROL(GL_TESS_CONTROL_SHADER),
		TESSELLATION_EVALUATION(GL_TESS_EVALUATION_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER)
	}
	
	var id: Int = 0
	
	init {
		
		id = glCreateShader(type.id)
		
		val text = Window::class.java.getResource(filename).readText()
		
		glShaderSource(id, text)
		glCompileShader(id)
		
		val res = glGetShaderi(id, GL_COMPILE_STATUS)
		//println("res: $res")
		if (res == GL_FALSE) {
			val length = glGetShaderi(id, GL_INFO_LOG_LENGTH)
			val buffer = glGetShaderInfoLog(id, length)
			println(buffer)
		}
	}
}
