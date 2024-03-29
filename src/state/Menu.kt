package state

import core.Window
import core.*
import math.vec2
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import sound.SimpleSound
import java.awt.event.KeyEvent
import kotlin.system.exitProcess

object Menu: GameState() {
	var BG: BufferedImage? = null
	private val OPTIONS = arrayOf("Start Game", "Help", "Exit")
	private var cursor = 0
	private var tick = 0
	
	init {
		BG = ImageIO.read(Menu::class.java.getResourceAsStream("/MenuBackground.png"))
		//SimpleSound.soundtrack.loop(Int.MAX_VALUE)
	}
	
	override fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int){
		if(action == 1) when(key) {
			GLFW.GLFW_KEY_ENTER -> {
				SimpleSound.pickup.play()
				when (cursor) {
					0 -> {
						StateManager.state = GameOptions
					}
					1 -> {
						StateManager.state = Help
					}
					2 -> {
						SimpleSound.die.play()
						exitProcess(0)
					}
				}
			}
			GLFW.GLFW_KEY_UP -> {
				if(cursor > 0) cursor--
			}
			GLFW.GLFW_KEY_DOWN -> {
				if(cursor < OPTIONS.size - 1) cursor++
			}
		}
	}
	
	override fun renderGL() {
		ImageRenderer.draw(bgImage, vec2(0), vec2(Window.width, Window.height))
		
		val middle = Window.width / 2
		val h = 300
		
		RectRenderer.depth = -0.1f
		RectRenderer.setColor(0.7f, 0.7f)
		RectRenderer.fill(vec2(middle - 200, Window.height - h - 200), vec2(400, h))
		
		TextRenderer.depth = -0.2f
		TextRenderer.setColor(0.0f)
		TextRenderer.draw(
			Window.GAME_TITLE,
			vec2(Window.width / 2 - Window.GAME_TITLE.length / 2 * 32, Window.height - 70),
			4.0f
		)
		
		for (i in OPTIONS.indices) {
			val x = middle - OPTIONS[i].length / 2 * 3 * 8
			if (cursor == i) {
				if (tick % 30 / 15 == 0) {
					TextRenderer.draw(OPTIONS[i] + "<", vec2(x, Window.height - h - i * 50), 3.0f)
				} else {
					TextRenderer.draw(OPTIONS[i] + "<", vec2(x, Window.height - h - i * 50 + 2), 3.0f)
				}
			} else {
				TextRenderer.draw(OPTIONS[i], vec2(x, Window.height - h - i * 50), 3.0f)
			}
		}
	}
	
	override fun update() {
		
		tick++
	}
}
