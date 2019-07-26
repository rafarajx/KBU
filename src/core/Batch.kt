package core

import org.lwjgl.opengl.GL45.*

class Batch {

	var vbo1 = 0
	var vbo2 = 0
	var vbo3 = 0
	var vbo4 = 0
	
	lateinit var positions: FloatArray
	
	var reservedObjects = 0
	
	fun init() {
	
		reservedObjects = 10000
		
		positions = FloatArray(reservedObjects)
		vbo1 = Utils.createBufferStorage(positions)
	
	
		
		
	
	}
	
	fun render() {
	
		glDrawArrays(GL_POINTS, 0, reservedObjects)
	
	}
	
	
}