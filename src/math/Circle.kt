package math

import kotlin.math.max
import kotlin.math.min

class Circle(var p: vec2, var r: Float) {
	
	fun intersects(aabb: AABB): Boolean {
		val dx = p.x - max(aabb.ul.x, min(p.x, aabb.ul.x + aabb.width))
		val dy = p.y - max(aabb.ul.y, min(p.y, aabb.ul.y + aabb.height))
		return (dx * dx + dy * dy) < (r * r)
	}

}