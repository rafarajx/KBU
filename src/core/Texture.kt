package core

import math.vec2
import org.lwjgl.opengl.GL44.*
import org.lwjgl.stb.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer
import org.lwjgl.system.MemoryStack



class Texture{
	
	var id = 0
	
	var width = 0
	var height = 0

	val size
		get() = vec2(width, height)

	var image: ByteBuffer? = null

	constructor (filename: String) {

		/*

		image = ImageIO.read(Main::class.java.getResourceAsStream(filename))
		//val data = (image.raster.dataBuffer as DataBufferByte).data
		//val pixels: ByteBuffer = ByteBuffer.wrap(data)

		width = image.width
		height = image.height



		val rawPixels = image.getRGB(0, 0, width, height, null, 0, width)

		val pixels = BufferUtils.createByteBuffer(width * height * 4)

		for(i in rawPixels.indices){
			val c = rawPixels[i]
			pixels.put(((c shr 0) or 0xFF).toByte())		//BLUE
			pixels.put(((c shr 8) or 0xFF).toByte())		//GREEN
			pixels.put(((c shr 16) or 0xFF).toByte())	//RED
			pixels.put(((c shr 24) or 0xFF).toByte())	//ALPHA

		}

		pixels.flip()
		*/

		MemoryStack.stackPush().use { stack->
			val w = stack.mallocInt(1)
			val h = stack.mallocInt(1)
			val comp = stack.mallocInt(1)

			stbi_set_flip_vertically_on_load(true)
			image = stbi_load(filename, w, h, comp, 4)
			if (image == null)
				throw RuntimeException("Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason())

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


