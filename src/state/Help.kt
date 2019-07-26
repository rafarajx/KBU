package state

import core.*
import math.vec2
import java.awt.Color
import java.awt.Graphics2D

import org.lwjgl.glfw.GLFW
import sound.SimpleSound
import java.awt.event.KeyEvent

object Help : GameState() {

	private val HELP_TEXT = arrayOf(
		"Control:",
		"LMB to place buildings",
		"RMB to move through map",
		"LEFT-RIGHT key arrows to change between buildings",
		"Numpad +/- to adjust speed of the game",
		"Press escape to quit..."
	)

	private var keyPressed: Input.KEvent? = null

	override fun onSet() {
		keyPressed = object : Input.KEvent {
			override fun get(e: KeyEvent) {
				if (e.keyCode == 27) {
					SimpleSound.pickup.play()
					Input.keyPressed = null
					StateManager.state = Menu
				}
			}
		}
		Input.keyPressed = keyPressed
	}

	override fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int) {
		if (action == 1) {
			if (key == GLFW.GLFW_KEY_ESCAPE) {
				SimpleSound.pickup.play()
				StateManager.state = Menu
			}
		}
	}

	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Menu.BG, 0, 0, Canvas.width, Canvas.height, null)
		g2d.color = Color(180, 180, 180, 180)
		Screen.drawString(g2d, "FPS: " + Canvas.FPS, 20, 20, 1.0)
		g2d.fill3DRect(60, 200 - 40, Canvas.width - 120, HELP_TEXT.size * 50 + 50, true)
		for (i in HELP_TEXT.indices) {
			Screen.drawString(g2d, HELP_TEXT[i], Canvas.width / 2 - HELP_TEXT[i].length * 8, 200 + i * 50, 2.0)
		}
	}

	val w = w()

	override fun renderGL() {
		ImageRenderer.draw(G.bgImage, vec2(0, 0), vec2(Window.width, Window.height))
		TextRenderer.draw("FPS: " + Canvas.FPS, vec2(20), 1.0f)
		//g2d.fill3DRect(60, 200 - 40, Canvas.width - 120, HELP_TEXT.size * 50 + 50, true)
		val h = HELP_TEXT.size * 50 + 50
		val margin = 80
		
		
		RectRenderer.setColor(0.7f, 0.7f)
		RectRenderer.fill(vec2(Window.width / 2 - w / 2 - margin / 2, 400), vec2(w + margin, h))
		
		TextRenderer.setColor(0.0f)
		for (i in HELP_TEXT.indices) {
			TextRenderer.draw(
				HELP_TEXT[i],
				vec2(Window.width / 2 - HELP_TEXT[i].length * 8, 400 + (HELP_TEXT.size - i) * 50),
				2.0f
			)
		}
	}

	fun w(): Int {
		var max = Int.MIN_VALUE
		for (i in HELP_TEXT.indices) {
			val a = HELP_TEXT[i].length
			if (max < a)
				max = a
		}

		return max * 8 * 2
	}

	override fun update() {

	}
}
