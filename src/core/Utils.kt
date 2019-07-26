package core

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL45.*

object Utils {
	
	fun getDepth(): Float{
		return Canvas.SECOND.toFloat() / (System.nanoTime() % Canvas.SECOND)
	}
	
	fun createStreamArrayBuffer(data: FloatArray): Int{
		val vbo = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferData(GL_ARRAY_BUFFER, data, GL_STREAM_DRAW)
		return vbo
	}
	
	fun createBufferStorage(data: FloatArray): Int{
		val vbo = glCreateBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferStorage(GL_ARRAY_BUFFER, data, GL_DYNAMIC_STORAGE_BIT)
		return vbo
	}
	
}