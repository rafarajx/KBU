package math

import kotlin.math.max
import kotlin.math.min

class Circle(var center: vec2, var range: Float) {

	var rangesq = range * range
	
	constructor(p: vec2, r: Int) : this(p, r.toFloat())
	
	
	fun intersects(aabb: AABB): Boolean {
		val dx = center.x - max(aabb.xmin, min(center.x, aabb.xmin + aabb.width))
		val dy = center.y - max(aabb.ymin, min(center.y, aabb.ymin + aabb.height))
		return dx * dx + dy * dy < rangesq
	}
	
	fun contains(aabb: AABB): Boolean {
		return aabb.contains((center - aabb.center).normalize() * range)
	}
	
	fun contains(point: vec2): Boolean{
		return (point - center).square().sum() < rangesq
	}
	

}