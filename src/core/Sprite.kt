package core

import gametype.Game
import math.AABB
import math.vec2
import java.util.*
import kotlin.collections.ArrayList

class Sprite {

	var positions = FloatArray(8)
	var texcoords = FloatArray(8)
	var depths = FloatArray(8)
	
	fun updatePosition(pos: vec2, size: vec2) {
		val a = pos.x
		val b = pos.y
		val c = pos.x + size.x
		val d = pos.y + size.y
		
		positions[0] = a
		positions[1] = b
		
		positions[2] = c
		positions[3] = b
		
		positions[4] = c
		positions[5] = d
		
		positions[6] = a
		positions[7] = d
	}
	
	fun updateTexCoords(tpos: vec2, tsize: vec2) {
		val a = tpos.x / Canvas.tileset.width
		val b = (tpos.x + tsize.x) / Canvas.tileset.width
		val c = Canvas.tileset.height - (tpos.y + tsize.y) / Canvas.tileset.height
		val d = Canvas.tileset.height - tpos.y / Canvas.tileset.height
		
		texcoords[0] = a
		texcoords[1] = c
		
		texcoords[2] = b
		texcoords[3] = c
		
		texcoords[4] = b
		texcoords[5] = d
		
		texcoords[6] = a
		texcoords[7] = d
	}
	
	fun updateTexCoords() {
	
	}
	
	fun updateDepth(depth: Float){
		
		depths[0] = depth
		depths[1] = depth
		depths[2] = depth
		depths[3] = depth
	}
}