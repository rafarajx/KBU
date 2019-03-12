package math

import kotlin.math.abs

class AABB(var ul: vec2, var dr: vec2) {
	
	constructor(middle: vec2, halfEdge: Int): this(middle - halfEdge, middle + halfEdge)
	
	constructor(middle: vec2, halfEdge: Float): this(middle - halfEdge, middle + halfEdge)
	
	var x = ul.x
	var y = ul.y
	
	var xmin = ul.x
	var xmax = dr.x
	
	var ymin = ul.y
	var ymax = dr.y
	
	var width = dr.x - ul.x
	var height = dr.y - ul.y
	
	var center = (ul + dr) * 0.5f
	
	var ur = vec2(xmax, ymin)
	var dl = vec2(xmin, ymax)
	
	/*
	fun intersects(other: AABB): Boolean {
		return xmin < other.xmax && xmax > other.xmin && ymin < other.ymax && xmax > other.ymin
	}
	*/
	
	fun intersects(other: AABB): Boolean {
		return (abs(xmin - other.xmin) * 2 < (width + other.width)) &&
				(abs(ymin - other.ymin) * 2 < (height + other.height))
	}
	
	fun contains(p: vec2): Boolean {
		return p.x > xmin && p.x < xmax && p.y > ymin && p.y < ymax
	}
	
}