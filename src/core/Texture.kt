package core

import math.vec2
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL44.*
import org.lwjgl.stb.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer
import org.lwjgl.system.MemoryStack
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.FileNotFoundException
import java.nio.channels.FileChannel
import java.io.IOException



class Texture{
	
	var id = 0
	
	var width = 0
	var height = 0

	val size
		get() = vec2(width, height)

	var image: ByteBuffer? = null
	
	constructor (filename: String) {
		
		println("Loading texture: $filename ...")
		
		val stream = Window::class.java.getResourceAsStream(filename)
		
		val bytes = ByteArray(stream.available())
		stream.read(bytes)
		
		val buffer = BufferUtils.createByteBuffer(bytes.size)
		buffer.put(bytes)
		buffer.flip()
		
		MemoryStack.stackPush().use { stack->
			val w = stack.mallocInt(1)
			val h = stack.mallocInt(1)
			val comp = stack.mallocInt(1)
			
			stbi_set_flip_vertically_on_load(true)
			image = stbi_load_from_memory(buffer, w, h, comp, 4)
			
			//stbi_set_flip_vertically_on_load(true)
			//image = stbi_load(filename, w, h, comp, 4)
			
			
			if (image == null)
				throw RuntimeException(
					"Failed to load a texture file!\n" + stbi_failure_reason() + "\n" + filename
				)
			println("Texture loaded")

			width = w.get()
			height = h.get()
		}

		id = glGenTextures()
		glBindTexture(GL_TEXTURE_2D, id)
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image)
		
		glBindTexture(GL_TEXTURE_2D, 0)
		
	}

	constructor (bits: ByteBuffer, width: Int, height: Int) {
		
		id = glGenTextures()
		glBindTexture(GL_TEXTURE_2D, id)
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, bits)
		
		glBindTexture(GL_TEXTURE_2D, 0)
	}

	fun bind(num: Int) {
		glBindTexture(GL_TEXTURE_2D, id)
		glActiveTexture(GL_TEXTURE0 + num)
	}
};


