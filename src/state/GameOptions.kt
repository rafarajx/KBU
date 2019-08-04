package state

import core.Window
import core.*

import gametype.FFA
import gametype.SuppressUprising
import math.vec2
import org.lwjgl.glfw.GLFW
import java.awt.event.KeyEvent

object GameOptions : GameState() {
	private var gameMode = 0
	private val GAME_MODES = arrayOf("Free For All", "Suppress Uprising"/*, "Zombie Apocalypse"*/)
	private var optionNum = 0
	private var numberOfEnemies = 2
	private var difficulty = 1
	private val DIFFICULTIES = arrayOf("Easy", "Normal", "Hard", "Insane")
	private const val S1 = "Press enter to continue..."

	private var tick = 1
	
	override fun onSet() {
		Input.keyPressed = object : Input.KEvent {
			override fun get(e: KeyEvent) {
				when (e.keyCode) {
					KeyEvent.VK_ENTER -> {
						Input.keyPressed = null
						when (gameMode) {
							0 -> {
								FFA.opponentsNumber = numberOfEnemies
								FFA.difficulty = difficulty + 1
								StateManager.state = FFA
							}
							1 -> {
								SuppressUprising.friendsNumber = numberOfEnemies
								SuppressUprising.difficulty = difficulty + 1
								StateManager.state = SuppressUprising
							}
						}
					}
					KeyEvent.VK_LEFT -> {
						if (optionNum == 0 && gameMode > 0) gameMode--
						else if (optionNum == 1 && numberOfEnemies > 0) numberOfEnemies--
						else if (optionNum == 2 && difficulty > 0) difficulty--
					}
					KeyEvent.VK_RIGHT -> {
						if (optionNum == 0 && gameMode < GAME_MODES.size - 1) gameMode++
						else if (optionNum == 1 && numberOfEnemies < 7) numberOfEnemies++
						else if (optionNum == 2 && difficulty < DIFFICULTIES.size - 1) difficulty++
					}
					KeyEvent.VK_UP -> if (optionNum > 0) optionNum--
					KeyEvent.VK_DOWN -> if (optionNum < 2) optionNum++
				}
			}
		}
	}

	override fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int){
		if(action == 1) when (key) {
			GLFW.GLFW_KEY_ENTER -> {
				when (gameMode) {
					0 -> {
						FFA.opponentsNumber = numberOfEnemies
						FFA.difficulty = difficulty + 1
						StateManager.state = FFA
					}
					1 -> {
						SuppressUprising.friendsNumber = numberOfEnemies
						SuppressUprising.difficulty = difficulty + 1
						StateManager.state = SuppressUprising
					}
				}
			}
			GLFW.GLFW_KEY_LEFT -> {
				if (optionNum == 0 && gameMode > 0) gameMode--
				else if (optionNum == 1 && numberOfEnemies > 0) numberOfEnemies--
				else if (optionNum == 2 && difficulty > 0) difficulty--
			}
			GLFW.GLFW_KEY_RIGHT -> {
				if (optionNum == 0 && gameMode < GAME_MODES.size - 1) gameMode++
				else if (optionNum == 1 && numberOfEnemies < 7) numberOfEnemies++
				else if (optionNum == 2 && difficulty < DIFFICULTIES.size - 1) difficulty++
			}
			GLFW.GLFW_KEY_UP -> if (optionNum > 0) optionNum--
			GLFW.GLFW_KEY_DOWN -> if (optionNum < 2) optionNum++
		}
	}
	
	override fun renderGL() {
		ImageRenderer.draw(bgImage, vec2(0), Window.size)

		val bgw = 800
		val bgh = 600

		val middlew = Window.width / 2
		
		RectRenderer.setColor(0.6f, 0.6f, 0.6f, 0.6f)
		RectRenderer.fill(vec2(middlew - bgw / 2, Window.height - 50 - bgh), vec2(bgw, bgh))

		val go = "Game Options"
		val gm = "Game Mode:"

		TextRenderer.setColor(0.0f)
		TextRenderer.draw(go, vec2(middlew - go.length * 4 * 8 / 2, Window.height - 140), 4.0f)
		TextRenderer.draw(gm, vec2(middlew - 350, Window.height - 200), 3.0f)

		val gmw = middlew - GAME_MODES[gameMode].length * 8 * 3 + 300
		if (optionNum == 0 && tick % 30 / 15 == 0)
			TextRenderer.draw("<" + GAME_MODES[gameMode] + ">", vec2(gmw, Window.height - 200), 3.0f)
		else
			TextRenderer.draw("<" + GAME_MODES[gameMode] + ">", vec2(gmw, Window.height - 200 + 2), 3.0f)


		val nop = "Number of opponents:"
		val noa = "Number of allies:"
		when (gameMode) {
			0 -> TextRenderer.draw(nop, vec2(middlew - 350, Window.height - 250), 3.0f)
			1 -> TextRenderer.draw(noa, vec2(middlew - 350, Window.height - 250), 3.0f)
			else -> TextRenderer.draw(nop, vec2(middlew - 350, Window.height - 250), 3.0f)
		}

		val noew = middlew - numberOfEnemies.toString().length * 8 * 3 + 300
		if (optionNum == 1 && tick % 30 / 15 == 0)
			TextRenderer.draw("<$numberOfEnemies>", vec2(noew, Window.height - 250), 3.0f)
		else
			TextRenderer.draw("<$numberOfEnemies>", vec2(noew, Window.height - 250 + 2), 3.0f)
		
		
		
		TextRenderer.draw("Difficulty:", vec2(middlew - 350, Window.height - 300), 3.0f)

		val difw = middlew - DIFFICULTIES[difficulty].length * 8 * 3 + 300
		if (optionNum == 2 && tick % 30 / 15 == 0)
			TextRenderer.draw("<" + DIFFICULTIES[difficulty] + ">", vec2(difw, Window.height - 300), 3.0f)
		else
			TextRenderer.draw("<" + DIFFICULTIES[difficulty] + ">", vec2(difw, Window.height - 300 + 2), 3.0f)


		for (i in 0 until S1.length) {
			val s1w = middlew - S1.length * 8 * 2 / 2 + i * 16
			if (tick % (S1.length * 7) / 7 == i)
				TextRenderer.draw(S1.substring(i, i + 1), vec2(s1w, Window.height - 600), 2.0f)
			else
				TextRenderer.draw(S1.substring(i, i + 1), vec2(s1w, Window.height - 600 + 2), 2.0f)
		}
	}
	
	override fun update() {
		
		tick++
	}
}
