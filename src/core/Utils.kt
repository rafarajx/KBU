package core

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL45.*
import kotlin.random.Random

object Utils {
	
	val random = Random(2837934)
	
	fun createStreamArrayBuffer(data: FloatArray): Int{
		val vbo = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferData(GL_ARRAY_BUFFER, data, GL_STREAM_DRAW)
		return vbo
	}
	
	fun createBufferStorage(data: FloatArray): Int{
		val vbo = glCreateBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferStorage(GL_ARRAY_BUFFER, data, GL_MAP_WRITE_BIT or GL_MAP_PERSISTENT_BIT or GL_MAP_COHERENT_BIT or GL_DYNAMIC_STORAGE_BIT)
		return vbo
	}
	
	fun nextFloat(): Float{
		return random.nextFloat()
	}
	
	fun nextInt(until: Int): Int{
		return random.nextInt(until)
	}
	
}