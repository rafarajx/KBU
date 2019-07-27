package core

import math.vec2
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack.stackPush


open class Window {
	
	companion object {
		const val GAME_TITLE = "Kung-fu Barbarian Unit v1.2"
		const val BILLION = 1000000000

		var id: Long = 0
		var width = 1000
		var height = 800

		val size: vec2
		get() = vec2(width, height)
	}


}

var TUPSA = ArrayList<Int>()

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

	val clt = Thread { Canvas.loop() }
	clt.start()

	while (!glfwWindowShouldClose(Window.id)) {
		glfwPollEvents()

		val t1 = System.nanoTime()
		
		Canvas.renderGL()
		
		val t2 = System.nanoTime()
		val d = t2 - t1
		TUPSA.add((Canvas.SECOND / d).toInt())
		if(TUPSA.size > 60) TUPSA.removeAt(0)
		Canvas.TFPS = TUPSA.average().toInt()
		
		glfwSwapBuffers(Window.id)

	}
	
	Canvas.threadrunning = false
	
}



