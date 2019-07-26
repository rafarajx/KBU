package core

import gametype.Game
import math.vec2
import org.lwjgl.opengl.GL45.*

class SpriteBatch (var tileset: Texture){
	var program: ShaderProgram
	
	var reservedTiles = 0
	var numberOfTiles = 0
	
	var active: BooleanArray
	
	var positions: FloatArray
	var texcoords: FloatArray
	var depths: FloatArray
	
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
		
		reservedTiles = 20000
		
		active = BooleanArray(reservedTiles)
		
		vao = glCreateVertexArrays()
		glBindVertexArray(vao)
		
		val vertex = Shader("/shaders/sprite.vs.glsl", Shader.Type.VERTEX)
		val fragment = Shader("/shaders/sprite.fs.glsl", Shader.Type.FRAGMENT)
		program = ShaderProgram(vertex, fragment)
		
		program.use()
		
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		
		positions = FloatArray(reservedTiles * 8)
		vbo = Utils.createBufferStorage(positions)
		positionAL = glGetAttribLocation(program.id, "position")
		glVertexAttribPointer(positionAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(positionAL)
		
		
		texcoords = FloatArray(reservedTiles * 8)
		tbo = Utils.createBufferStorage(texcoords)
		texCoordAL = glGetAttribLocation(program.id, "texCoord")
		glVertexAttribPointer(texCoordAL, 2, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(texCoordAL)
		
		
		depths = FloatArray(reservedTiles * 4)
		dbo = Utils.createBufferStorage(depths)
		depthAL = glGetAttribLocation(program.id, "depth")
		glVertexAttribPointer(depthAL, 1, GL_FLOAT, false, 0, 0)
		glEnableVertexAttribArray(depthAL)
		
		glBindBuffer(GL_ARRAY_BUFFER, 0)
		
		
		resolutionUL = glGetUniformLocation(program.id, "resolution")
		cameraUL = glGetUniformLocation(program.id, "camera")


		glUniform2f(cameraUL, 0f, 0f)


		glBindVertexArray(0)
	}

	fun getId(): Int {

		for (i in 0..reservedTiles) {
			if (!active[i]) {
				active[i] = true
				if (i > numberOfTiles) {
					numberOfTiles = i
				}
				return i
			}

		}
		return 0
	}
	
	fun updatePosition(index: Int, pos: vec2, size: vec2) {
		
		val a = pos.x
		val b = pos.y
		val c = pos.x + size.x
		val d = pos.y + size.y
		
		val z = index * 4 * 2
		
		positions[z + 0] = a
		positions[z + 1] = b
		
		positions[z + 2] = c
		positions[z + 3] = b
		
		positions[z + 4] = c
		positions[z + 5] = d
		
		positions[z + 6] = a
		positions[z + 7] = d
	}
	
	fun updateSize(){
	
	}
	
	fun updateTexCoords(index: Int, tpos: vec2, tsize: vec2) {
		
		val a = tpos.x / tileset.width
		val b = (tpos.x + tsize.x) / tileset.width
		val c = tileset.height - (tpos.y + tsize.y) / tileset.height
		val d = tileset.height - tpos.y / tileset.height

		val z = index * 8
		
		texcoords[z + 0] = a
		texcoords[z + 1] = c
		
		texcoords[z + 2] = b
		texcoords[z + 3] = c
		
		texcoords[z + 4] = b
		texcoords[z + 5] = d
		
		texcoords[z + 6] = a
		texcoords[z + 7] = d
	}
	
	fun updateTexCoords(index: Int) {
		updateTexCoords(index, vec2(0), tileset.size)
	}
	
	fun updateDepth(index: Int, depth: Float){
		val z = index * 4
		
		depths[z + 0] = depth
		depths[z + 1] = depth
		depths[z + 2] = depth
		depths[z + 3] = depth
	}
	
	fun render() {


		glBindVertexArray(vao)

		program.use()
		
		glProgramUniform2fv(program.id, resolutionUL, Window.size.array)
		glProgramUniform2fv(program.id, cameraUL, Game.camera.array)
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo)
		glBufferSubData(GL_ARRAY_BUFFER, 0, texcoords.copyOf(numberOfTiles * 8))
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferSubData(GL_ARRAY_BUFFER, 0, positions.copyOf(numberOfTiles * 8))
		
		glBindBuffer(GL_ARRAY_BUFFER, dbo)
		glBufferSubData(GL_ARRAY_BUFFER, 0, depths.copyOf(numberOfTiles * 4))
		
		tileset.bind(0)
		
		glDrawArrays(GL_QUADS, 0, numberOfTiles * 4)
		//glDrawArraysInstanced(GL_QUADS, 0, 8, numberOfTiles)
		
		glBindVertexArray(0)
	}

	fun removeSprite(index: Int) {
		active[index] = false
		
		val z = index * 8
		
		texcoords[z + 0] = 0.0f
		texcoords[z + 1] = 0.0f
		
		texcoords[z + 2] = 0.0f
		texcoords[z + 3] = 0.0f
		
		texcoords[z + 4] = 0.0f
		texcoords[z + 5] = 0.0f
		
		texcoords[z + 6] = 0.0f
		texcoords[z + 7] = 0.0f
	}
	
	
	
	fun openPosition() {
	
	}
	
	fun openTexCoords() {
	
	}
	
	fun closePosition() {
		
		
		
		//glBufferData(GL_ARRAY_BUFFER, reserved_bytes, position_data, GL_DYNAMIC_DRAW);
		
		/*
		void *data = glMapNamedBufferRange(vbo, 0, number_of_bytes, GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT);
		data = (void*)position_data;
		glUnmapNamedBuffer(vbo);
		*/
	}
	
	fun add(sprite: Sprite){
	
		sprite.id = getId()
		sprite.batch = this
		
	}
	
	fun remove(sprite: Sprite){
		removeSprite(sprite.id)
	}
	
	fun closeTexCoords() {
		
		
		
		
		//glBufferData(GL_ARRAY_BUFFER, reserved_bytes, texcoord_data, GL_DYNAMIC_DRAW);
		
		/*
		void *data = glMapNamedBufferRange(tbo, 0, number_of_bytes, GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT);
		data = (void*)texcoord_data;
		glUnmapNamedBuffer(tbo);
		*/
	}
}