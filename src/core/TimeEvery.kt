package core

import org.lwjgl.glfw.GLFW.*

class TimeEvery(var time: Float, var cb: () -> Unit){

	var active = true
	var starttime = glfwGetTime()
	
}