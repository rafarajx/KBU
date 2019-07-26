package core

import gametype.Game
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL45.*
import state.StateManager
import java.awt.Color
import java.awt.Graphics2D
import javax.imageio.ImageIO
import kotlin.math.abs

object Canvas : java.awt.Canvas() {
	
	
	const val SECOND: Long = 1_000_000_000
	
	var UPS: Long = 60
	var UPDATE_TIME = SECOND / UPS
	
	var FPS: Int = 60
	var FRAME_TIME = SECOND / FPS
	
	var TUPS: Int = 0
	var TFPS: Int = 0
	
	
	var TUPSA = ArrayList<Int>()
	var TFPSA = ArrayList<Int>()
	
	
	lateinit var bra: Texture
	lateinit var tileset: Texture
	
	init {
		
		setSize(Window.width, Window.height)
		isFocusable = true
		addMouseListener(Input)
		addMouseMotionListener(Input)
		addKeyListener(Input)
		
		Screen.init(
			ImageIO.read(Window::class.java.getResourceAsStream("/Tileset.png")),
			ImageIO.read(Window::class.java.getResourceAsStream("/Font.png"))
		)
	}

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

		bra = Texture("./res/House.png")
		tileset = Texture("./res/Tileset.png")
		
		
		glEnable(GL_DEPTH_TEST)
		
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		ImageRenderer.init()
		TextRenderer.init()
		RectRenderer.init()
		AlphaInverter.init()
		Fog.init()
		

		

		
		G.batch = SpriteBatch(tileset)

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
	

	fun loopGL() {

		while (true) {
			val t1 = System.nanoTime()

			StateManager.update()

			val t2 = System.nanoTime()
			val d = t2 - t1
			var st = UPDATE_TIME - d
			if(st < 0) st = 1
			TUPSA.add((SECOND / d).toInt())
			if(TUPSA.size > UPS) TUPSA.removeAt(0)
			TUPS = TUPSA.average().toInt()
			Thread.sleep(st / 1000000, st.rem(1000000).toInt())
		}
	}

	fun renderGL() {
		
		glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)
		//glEnable(GL11.GL_DEPTH_TEST)
		glClearColor(0.0f, 0.63f, 0.24f, 1.0f)

		G.batch.openPosition()
		G.batch.openTexCoords()
		/*
		for (i in 0..100) {
			val x = i % 10
			val y = i / 10

			G.batch.updatePosition(i, vec2(32 * x, 32 * y), vec2(32, 32))
			G.batch.updateTexCoords(i, vec2(0, 240), vec2(16, 16))
		}
		*/

		StateManager.renderGL()
		


		G.batch.closePosition()
		G.batch.closeTexCoords()

		G.batch.render()
		
		
		

	}
	
}
