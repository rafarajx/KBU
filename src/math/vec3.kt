package math

import kotlin.math.sqrt

data class vec3(var x: Float, var y: Float, var z: Float) {

    val array
        get() = floatArrayOf(x, y, z)

    val r: Float
        get() = x
    val g: Float
        get() = y
    val b: Float
        get() = z

    val xxx
        get() = vec3(x, x, x)
    val yyy
        get() = vec3(y, y, y)
    val zzz
        get() = vec3(z, z, z)
    val zyx
        get() = vec3(z, y, x)

    constructor(value: Float) : this(value, value, value)

    constructor(value: Int) : this(value.toFloat(), value.toFloat(), value.toFloat())

    constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())

    constructor(xy: vec2, z: Float) : this(xy.x, xy.y, z)

    override operator fun equals(other: Any?): Boolean {
        return other is vec3 && x == other.x && y == other.y && z == other.z
    }

    operator fun plus(v: vec3): vec3 {
        return vec3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: vec3): vec3 {
        return vec3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(v: vec3): vec3 {
        return vec3(x * v.x, y * v.y, z * v.z)
    }

    operator fun div(v: vec3): vec3 {
        return vec3(x / v.x, y / v.y, z / v.z)
    }

    operator fun plus(n: Float): vec3 {
        return vec3(x + n, y + n, z + n)
    }

    operator fun minus(n: Float): vec3 {
        return vec3(x - n, y - n, z - n)
    }

    operator fun times(n: Float): vec3 {
        return vec3(x * n, y * n, z * n)
    }

    operator fun div(n: Float): vec3 {
        return vec3(x / n, y / n, z / n)
    }

    operator fun plus(n: Int): vec3 {
        return vec3(x + n, y + n, z + n)
    }

    operator fun minus(n: Int): vec3 {
        return vec3(x - n, y - n, z - n)
    }

    operator fun times(n: Int): vec3 {
        return vec3(x * n, y * n, z * n)
    }

    operator fun div(n: Int): vec3 {
        return vec3(x / n, y / n, z / n)
    }

    operator fun get(i: Int): Float {
        when (i) {
            0 -> return x
            1 -> return y
            2 -> return z
        }
        return 0.0f
    }

    fun square(): vec3 {
        return vec3(x * x, y * y, z * z)
    }

    fun sum(): Float {
        return x + y + z
    }

    fun length(): Float {
        return sqrt(square().sum())
    }

    fun normalize(): vec3 {
        return this / length()
    }

}
