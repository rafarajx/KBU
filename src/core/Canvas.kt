package core

import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyEvent

import javax.imageio.ImageIO

import state.StateManager
import java.awt.Dimension
import kotlin.math.abs
import kotlin.math.ceil

object Canvas : java.awt.Canvas() {

	val SECOND: Long = 1_000_000_000
	var UPS: Long = 60
	var UPDATE_TIME = SECOND / UPS
	var FPS: Int = 60
	var FRAME_TIME = SECOND / FPS
	var TUPS: Int = 0
	var TUPSA = ArrayList<Int>()

	init {
		setSize(1000, 800)
		isFocusable = true
		addMouseListener(Input)
		addMouseMotionListener(Input)
		addKeyListener(Input)
	
		Screen.init(
			ImageIO.read(Main::class.java.getResourceAsStream("/Tileset.png")),
			ImageIO.read(Main::class.java.getResourceAsStream("/Font.png"))
		)
	}

	fun loop() {
		
		Thread {
			while(true){
				val t1 = System.nanoTime()
				setupRender()
				val t2 = System.nanoTime()
				val d = t2 - t1
				Thread.sleep(abs(FRAME_TIME - d) / 1000000)
			}
		}.start()
		
		
		while (true) {
			val t1 = System.nanoTime()
			if(Frame.minSize.width < 1000){
				setSize(1000, Frame.minSize.width)
			}
			if(Frame.minSize.height < 800){
				setSize(width, Frame.minSize.height)
			}
			
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

	private fun setupRender() {
		val bs = bufferStrategy
		if (bs == null) {
			createBufferStrategy(2)
			return
		}
		val g2d = bs.drawGraphics as Graphics2D
		g2d.color = Color(0, 160, 60)
		g2d.fillRect(0, 0, width, height)
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		render(g2d)
		bs.show()
	}

	private fun render(g2d: Graphics2D) {
		StateManager.render(g2d)
	}

}
