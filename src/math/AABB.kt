package math

import kotlin.math.abs

data class AABB(var downleft: vec2, var upright: vec2) {
	
	constructor (pos: vec2, size: Float): this(pos - size, pos + size)
	constructor (pos: vec2, size: Int): this(pos, size.toFloat())
	
	companion object {
		fun rect(xy: vec2, wh: vec2): AABB {
			return AABB(xy, wh + xy)
		}
	}
	var xmin = downleft.x
	var xmax = upright.x
	
	var ymin = downleft.y
	var ymax = upright.y
	
	var width = xmax - xmin
	var height = ymax - ymin
	
	var center = (downleft + upright) * 0.5f
	
	var size = vec2(width, height)
	
	var x = xmin
	var y = ymin
	
	/*
	fun intersects(other: AABB): Boolean {
		return xmin < other.xmax && xmax > other.xmin && ymin < other.ymax && xmax > other.ymin
	}
	*/
	
	/*
	fun intersects(other: AABB): Boolean {
		return (abs(xmin - other.xmin) * 2 < (width + other.width)) &&
				(abs(ymin - other.ymin) * 2 < (height + other.height))
	}
	
	*/
	
	
	fun intersects(other: AABB): Boolean {
		// If one rectangle is on left side of other
		if (xmin > other.xmax || other.xmin > xmax)
			return false
		
		// If one rectangle is above other
		if (ymax < other.ymin || other.ymax < ymin)
			return false
		
		return true
	}
	
	fun contains(p: vec2): Boolean {
		//println("${c.field}    ${e.p}    ${c.field.contains(e.p)}")
		val b = p.x > xmin && p.x < xmax && p.y > ymin && p.y < ymax
		
		//println("$xmin   $xmax   $ymin   $ymax  ${p.x}  ${p.y}   $ul $dr    $b")
		return b
	}
	
}