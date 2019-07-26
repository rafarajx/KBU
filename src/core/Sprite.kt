package core

import math.vec2

class Sprite {

	var id = 0
	
	lateinit var batch: SpriteBatch
	
	init {
	
	
	}
	
	fun updatePosition(pos: vec2, size: vec2) {
		batch.updatePosition(id, pos, size)
		batch.updateDepth(id, 1.0f / -pos.y)
	}
	
	fun updateTexCoords(tpos: vec2, tsize: vec2) {
		batch.updateTexCoords(id, tpos, tsize)
	}
	
	fun updateTexCoords() {
		updateTexCoords(vec2(0f), batch.tileset.size)
	}
	
	fun updateDepth(depth: Float){
		batch.updateDepth(id, depth)
	}
}