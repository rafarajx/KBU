package core

import org.lwjgl.opengl.GL44.*

class ShaderProgram(vararg args: Shader) {
	
	var id: Int = 0
	
	init {
		
		id = glCreateProgram()
		
		for (i in args.indices) {
			glAttachShader(id, args[i].id)
		}
		
		glLinkProgram(id)
		
		var res = glGetProgrami(id, GL_LINK_STATUS)
		
		if (res == GL_FALSE) {
			val buffer = glGetProgramInfoLog(id)
			println(buffer)
		}
		
		glValidateProgram(id)
		
		res = glGetProgrami(id, GL_VALIDATE_STATUS)
		
		if (res == GL_FALSE) {
			println("Failed to vaildate program")
		}
		
	}
	
	fun use() {
		glUseProgram(id)
	}
	
};