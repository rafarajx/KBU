package math

import kotlin.math.sqrt

data class vec4(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f, var w: Float = 0f) {

    val array
        get() = floatArrayOf(x, y, z, w)
    
    val r: Float
        get() = x
    val g: Float
        get() = y
    val b: Float
        get() = z
    val a: Float
        get() = w

    constructor(value: Float) : this(value, value, value, value)

    constructor(value: Int) : this(value.toFloat(), value.toFloat(), value.toFloat(), value.toFloat())

    constructor(x: Int, y: Int, z: Int, w: Int) : this(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

    constructor(xyz: vec3, w: Float) : this(xyz.x, xyz.y, xyz.z, w)

    override operator fun equals(other: Any?): Boolean {
        return other is vec4 && x == other.x && y == other.y && z == other.z && w == other.w
    }

    operator fun plus(v: vec4): vec4 {
        return vec4(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: vec4): vec4 {
        return vec4(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(v: vec4): vec4 {
        return vec4(x * v.x, y * v.y, z * v.z, w * v.w)
    }

    operator fun div(v: vec4): vec4 {
        return vec4(x / v.x, y / v.y, z / v.z, w / v.w)
    }

    operator fun plus(n: Float): vec4 {
        return vec4(x + n, y + n, z + n, w + n)
    }

    operator fun minus(n: Float): vec4 {
        return vec4(x - n, y - n, z - n, w - n)
    }

    operator fun times(n: Float): vec4 {
        return vec4(x * n, y * n, z * n, w * n)
    }

    operator fun div(n: Float): vec4 {
        return vec4(x / n, y / n, z / n, w / n)
    }

    operator fun plus(n: Int): vec4 {
        return vec4(x + n, y + n, z + n, w + n)
    }

    operator fun minus(n: Int): vec4 {
        return vec4(x - n, y - n, z - n, w - n)
    }

    operator fun times(n: Int): vec4 {
        return vec4(x * n, y * n, z * n, w * n)
    }

    operator fun div(n: Int): vec4 {
        return vec4(x / n, y / n, z / n, w / n)
    }

    operator fun get(i: Int): Float {
        when (i) {
            0 -> return x
            1 -> return y
            2 -> return z
            3 -> return w
        }
        return 0.0f
    }

    fun square(): vec4 {
        return vec4(x * x, y * y, z * z, w * w)
    }

    fun sum(): Float {
        return x + y + z
    }

    fun length(): Float {
        return sqrt(square().sum())
    }

    fun normalize(): vec4 {
        return this / length()
    }

}
