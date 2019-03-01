package core

import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyEvent
import java.io.IOException

import javax.imageio.ImageIO

import state.StateManager

object Canvas : java.awt.Canvas() {

	var input = Input()

	val SECOND: Long = 1000000000
	var UPS: Long = 60
	var UPDATE_TIME = SECOND / UPS

	var FPS: Int = 0


	init {
		setSize(1000, 800)
		isFocusable = true
		addMouseListener(input)
		addMouseMotionListener(input)
		addKeyListener(input)
	
		Screen.init(
			ImageIO.read(Main::class.java.getResourceAsStream("/Tileset.png")),
			ImageIO.read(Main::class.java.getResourceAsStream("/Font.png"))
		)
	}

	fun loop() {
		var lastUpdate = System.nanoTime()
		var lastSecond = System.nanoTime()
		var updatesCount = 0
		
		Thread {
			while(true){
				setupRender()
			}
		}.start()
		
		
		while (true) {
			if (System.nanoTime() - lastUpdate > UPDATE_TIME) {
				StateManager.update()
				lastUpdate += UPDATE_TIME

				if (Input.isKeyDown(KeyEvent.VK_ADD)) {
					UPS++
					UPDATE_TIME = SECOND / UPS
				} else if (Input.isKeyDown(KeyEvent.VK_SUBTRACT) && UPS > 20) {
					UPS--
					UPDATE_TIME = SECOND / UPS
				}
			}
			
			updatesCount++
			if (System.nanoTime() - lastSecond > SECOND) {
				FPS = updatesCount
				updatesCount = 0
				lastSecond += SECOND
			}
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
