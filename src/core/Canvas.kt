package core

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL45.*
import state.StateManager
import kotlin.math.floor

object Canvas {
	lateinit var bra: Texture
	lateinit var tileset: Texture
	
	fun windowSizeCallback(width: Int, height: Int){
		println("$width $height")
		Window.width = width
		Window.height = height
		glViewport(0, 0, width, height)
	}

	fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int){
		StateManager.keyCallback(key, scancode, action, mods)
	}

	fun mouseButtonCallback(button: Int, action: Int, mods: Int){
		StateManager.mouseButtonCallback(button, action, mods)
	}

	fun windowPosCallback(xpos: Int, ypos: Int){
		StateManager.windowPosCallback(xpos, ypos)
	}

	fun cursorPosCallback(xpos: Float, ypos: Float){
		StateManager.cursorPosCallback(xpos, ypos)
	}

	fun init(){

		glEnable(GL_DEBUG_OUTPUT)
		glDebugMessageCallback({ source: Int, type: Int, id: Int, severity: Int, length: Int, message: Long, userParam: Long ->
			val msg = GLDebugMessageCallback.getMessage(length, message)
			if(type == GL_DEBUG_TYPE_ERROR)
				println("ERROR source = $source id = $id severity = $severity message = $msg")
		}, 0)

		bra = Texture("/House.png")
		tileset = Texture("/Tileset.png")
		
		glEnable(GL_DEPTH_TEST)
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		ImageRenderer.init()
		TextRenderer.init()
		RectRenderer.init()
		AlphaInverter.init()
		Fog.init()
		

		batch = SpriteBatch(tileset)

		/*
		for (i in 0..100) {

			val x = i % 10
			val y = i / 10

			batch.updatePosition(i, vec2(32 * x, 32 * y), vec2(32, 32))
			batch.updateTexCoords(i, vec2(0, 240), vec2(16, 16))
		}

		batch.closePosition()
		batch.closeTexCoords()
		*/
	}

	

	fun renderGL() {
		
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
		//glEnable(GL11.GL_DEPTH_TEST)
		glClearColor(0.0f, 0.63f, 0.24f, 1.0f)

		/*
		for (i in 0..100) {
			val x = i % 10
			val y = i / 10

			G.batch.updatePosition(i, vec2(32 * x, 32 * y), vec2(32, 32))
			G.batch.updateTexCoords(i, vec2(0, 240), vec2(16, 16))
		}
		*/
		StateManager.renderGL()
		

		batch.render()
	}
}
