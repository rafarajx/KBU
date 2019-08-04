package core

import math.vec2
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush
import state.StateManager


open class Window {
	
	companion object {
		const val GAME_TITLE = "Kung-fu Barbarian Unit v1.2"
		const val BILLION = 1_000_000_000

		var id: Long = 0
		var width = 1000
		var height = 800

		val size: vec2
		get() = vec2(width, height)
	}


}

var updatethreadrunning = true

var UPS: Long = 60
var UPDATE_TIME = 1.0 / UPS
var TUPS: Int = 0
var TUPSA = ArrayList<Double>()


var FPS: Int = 60
var FPScounter = 0
var FRAME_TIME = 1.0 / FPS
var TFPS: Int = 0
var TFPSA = ArrayList<Double>()


fun main(){
	
	println("LWJGL Version " + Version.getVersion())

	if ( !glfwInit())
		throw IllegalStateException("Unable to initialize GLFW")

	Window.id = glfwCreateWindow(Window.width, Window.height, "Game", 0, 0)

	stackPush().use { stack ->
		val pWidth = stack.mallocInt(1)
		val pHeight = stack.mallocInt(1)

		glfwGetWindowSize(Window.id, pWidth, pHeight)

		val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

		glfwSetWindowPos(
			Window.id, (vidmode!!.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2
		)
	}

	glfwSetWindowSizeCallback(Window.id) { _: Long, w: Int, h: Int -> Canvas.windowSizeCallback(w, h) }
	glfwSetKeyCallback(Window.id) { _: Long, key: Int, scancode: Int, action: Int, mods: Int ->
		Canvas.keyCallback(
			key,
			scancode,
			action,
			mods
		)
	}
	glfwSetMouseButtonCallback(Window.id) { _: Long, button: Int, action: Int, mods: Int ->
		Canvas.mouseButtonCallback(
			button,
			action,
			mods
		)
	}
	glfwSetWindowPosCallback(Window.id) { _: Long, xpos: Int, ypos: Int ->
		Canvas.windowPosCallback(
			xpos,
			ypos
		)
	}
	glfwSetCursorPosCallback(Window.id) { _: Long, xpos: Double, ypos: Double ->
		Canvas.cursorPosCallback(
			xpos.toFloat(),
			ypos.toFloat()
		)
	}
	
	
	glfwMakeContextCurrent(Window.id)

	glfwSwapInterval(1)

	glfwShowWindow(Window.id)

	GL.createCapabilities()
	
	Canvas.init()

	//val clt = Thread { loop() }
	//clt.start()

	Timer.addEvery(1.0f) {
		FPS = FPScounter
		FPScounter = 0
	}
	
	while (!glfwWindowShouldClose(Window.id)) {
		glfwPollEvents()

		val t1 = glfwGetTime()
		
		Canvas.renderGL()
		
		StateManager.update()
		Timer.update()
		
		val t2 = glfwGetTime()
		val d = t2 - t1
		
		FPScounter++
		
		TFPSA.add(1.0 / d)
		if(TFPSA.size > 60) TFPSA.removeAt(0)
		TFPS = TFPSA.average().toInt()
		
		glfwSwapBuffers(Window.id)
	}
	
	updatethreadrunning = false
	
}

/*
fun loop() {
	
	while (updatethreadrunning) {
		val t1 = glfwGetTime()
		
		StateManager.update()
		Timer.update()
		
		val t2 = glfwGetTime()
		val deltatime = t2 - t1 //time taken to complete update
		var st = 1.0 / UPS - deltatime
		if(st < 0) st = 0.0
		TUPSA.add(deltatime)
		if(TUPSA.size > UPS) TUPSA.removeAt(0)
		TUPS = (1.0 / TUPSA.average()).toInt()
		
		val millis = st * 1000.0
		val nanos = (millis - floor(millis)) * 1_000_000.0
		
		Thread.sleep(millis.toLong(), nanos.toInt())
	}
}
*/

