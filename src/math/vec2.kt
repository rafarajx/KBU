package math

import kotlin.math.hypot

data class vec2(var x: Float, var y: Float) {
	
	val array
		get() = floatArrayOf(x, y)
	
	constructor(value: Float) : this(value, value)
	
	constructor(value: Int) : this(value.toFloat(), value.toFloat())
	
	constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
	
	operator fun compareTo(other: vec2): Int{
		if(x < other.x && y < other.y) return -1
		if(x > other.x && y > other.y) return 1
		return 0
	}
	
	override operator fun equals(other: Any?): Boolean {
		return other is vec2 && x == other.x && y == other.y
	}
	
	operator fun unaryMinus(): vec2 {
		return vec2(-x, -y)
	}
	
	operator fun plus(v: vec2): vec2 {
		return vec2(x + v.x, y + v.y)
	}
	
	operator fun minus(v: vec2): vec2 {
		return vec2(x - v.x, y - v.y)
	}
	
	operator fun times(v: vec2): vec2 {
		return vec2(x * v.x, y * v.y)
	}
	
	operator fun div(v: vec2): vec2 {
		return vec2(x / v.x, y / v.y)
	}
	
	operator fun plus(n: Float): vec2 {
		return vec2(x + n, y + n)
	}
	
	operator fun minus(n: Float): vec2 {
		return vec2(x - n, y - n)
	}
	
	operator fun times(n: Float): vec2 {
		return vec2(x * n, y * n)
	}
	
	operator fun div(n: Float): vec2 {
		return vec2(x / n, y / n)
	}
	
	operator fun plus(n: Int): vec2 {
		return vec2(x + n, y + n)
	}
	
	operator fun minus(n: Int): vec2 {
		return vec2(x - n, y - n)
	}
	
	operator fun times(n: Int): vec2 {
		return vec2(x * n, y * n)
	}
	
	operator fun div(n: Int): vec2 {
		return vec2(x / n, y / n)
	}
	
	operator fun get(i: Int): Float {
		when (i) {
			0 -> return x
			1 -> return y
		}
		return 0.0f
	}
	
	fun square(): vec2 {
		return vec2(x * x, y * y)
	}
	
	fun sum(): Float {
		return x + y
	}
	
	fun length(): Float {
		return hypot(x, y)
	}
	
	fun normalize(): vec2 {
		return this / length()
	}
	
}
