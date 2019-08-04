package core

import gametype.Game
import math.vec2
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL45.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.util.*
import kotlin.collections.ArrayList

class SpriteBatch (var tileset: Texture){
	var program: ShaderProgram
	
	companion object{
		const val reservedSprites = 20000
	}
	
	var numberOfSprites = 0
	
	
	
	val positionbuffer: FloatBuffer
	val texcoordbuffer: FloatBuffer
	val depthbuffer: FloatBuffer
	
	var vao = 0
	
	var vbo = 0
	var tbo = 0
	var dbo = 0
	
	var positionAL = 0
	var texCoordAL = 0
	var depthAL = 0

	var resolutionUL = 0
	var cameraUL = -1

	init {
		
		vao = glCreateVertexArrays()
		glBindVertexArray(vao)
		
		val vertex = Shader("/shaders/sprite.vs.glsl", Shader.Type.VERTEX)
		val fragment = Shader("/shaders/sprite.fs.glsl", Shader.Type.FRAGMENT)
		program = ShaderProgram(vertex, fragment)
		
		program.use()
		
		
		
		
		
		val positions = FloatArray(reservedSprites * 8)
		vbo = Utils.createBufferStorage(positions)
		positionAL = glGetAttribLocation(program.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		//glVertexAttribDivisor(positionAL, 0)
		positionbuffer = glMapBufferRange(GL_ARRAY_BUFFER, 0, reservedSprites.toLong() * 4 * 8, GL_MAP_WRITE_BIT or GL_MAP_PERSISTENT_BIT or GL_MAP_COHERENT_BIT)!!.asFloatBuffer()
		
		
		val texcoords = FloatArray(reservedSprites * 8)
		tbo = Utils.createBufferStorage(texcoords)
		texCoordAL = glGetAttribLocation(program.id, "texCoord")
		glVertexAttribPointer(texCoordAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(texCoordAL)
		//glVertexAttribDivisor(texCoordAL, 0)
		texcoordbuffer = glMapBufferRange(GL_ARRAY_BUFFER, 0, reservedSprites.toLong() * 4 * 8, GL_MAP_WRITE_BIT or GL_MAP_PERSISTENT_BIT or GL_MAP_COHERENT_BIT)!!.asFloatBuffer()
		
		
		val depths = FloatArray(reservedSprites * 4)
		dbo = Utils.createBufferStorage(depths)
		depthAL = glGetAttribLocation(program.id, "depth")
		glVertexAttribPointer(depthAL, 1, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(depthAL)
		//glVertexAttribDivisor(depthAL, 0)
		depthbuffer = glMapBufferRange(GL_ARRAY_BUFFER, 0, reservedSprites.toLong() * 4 * 4, GL_MAP_WRITE_BIT or GL_MAP_PERSISTENT_BIT or GL_MAP_COHERENT_BIT)!!.asFloatBuffer()
		
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		
		
		resolutionUL = glGetUniformLocation(program.id, "resolution")
		cameraUL = glGetUniformLocation(program.id, "camera")
		
		
		glUniform2f(cameraUL, 0f, 0f)


		glBindVertexArray(0)
	}

	
	
	var renderablechunks = 0
	
	fun render() {
		
		positionbuffer.clear()
		texcoordbuffer.clear()
		depthbuffer.clear()
		
		var nos = 0
		var rc = 0
		
		for(i in World.chunks.indices){
			val chunk = World.chunks[i]
			if(!chunk.renderable) continue
			rc++
			for(j in chunk.entityList.indices){
				if(!chunk.entityList.indices.contains(j)) break
				val entity = chunk.entityList[j]
				
				for(k in entity.sprites.indices){
					
					val sprite = entity.sprites[k]
					
					positionbuffer.put(sprite.positions)
					texcoordbuffer.put(sprite.texcoords)
					depthbuffer.put(sprite.depths)
					
					nos++
				}
				
			}
		}
		numberOfSprites = nos
		renderablechunks = rc
		
		glBindVertexArray(vao)

		program.use()
		
		glProgramUniform2fv(program.id, resolutionUL, Window.size.array)
		glProgramUniform2fv(program.id, cameraUL, Game.camera.array)
		
		tileset.bind(0)
		
		glDrawArrays(GL_QUADS, 0, numberOfSprites * 4)
		
		
		//glDrawArraysInstanced(GL_QUADS, 0, 4, numberOfSprites)
		
		glBindVertexArray(0)
	}

}