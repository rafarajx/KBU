package state

import core.Window
import core.*
import math.vec2

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

	override fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int) {
		if (action == 1) {
			if (key == GLFW.GLFW_KEY_ESCAPE) {
				SimpleSound.pickup.play()
				StateManager.state = Menu
			}
		}
	}

	val w = w()

	override fun renderGL() {
		ImageRenderer.draw(bgImage, vec2(0, 0), vec2(Window.width, Window.height))
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