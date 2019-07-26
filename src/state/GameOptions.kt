package state

import core.*
import java.awt.Color
import java.awt.Graphics2D

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
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(Menu.BG, 0, 0, Canvas.width, Canvas.height, null)
		g2d.color = Color(180, 180, 180, 180)
		g2d.fill3DRect(50, 50, 780, 660, true)
		Screen.drawString(g2d, "Game Options", 225, 100, 4.0)
		Screen.drawString(g2d, "Game Mode:", 130, 200, 3.0)

		if (optionNum == 0 && tick % 30 / 15 == 0)
			Screen.drawString(g2d, "<" + GAME_MODES[gameMode] + ">", 520 - GAME_MODES[gameMode].length * 8, 200, 3.0)
		else
			Screen.drawString(g2d, "<" + GAME_MODES[gameMode] + ">", 520 - GAME_MODES[gameMode].length * 8, 202, 3.0)

		when (gameMode) {
			0 -> Screen.drawString(g2d, "Number of opponents:", 130, 250, 3.0)
			1 -> Screen.drawString(g2d, "Number of allies:", 130, 250, 3.0)
			else -> Screen.drawString(g2d, "Number of opponents:", 130, 250, 3.0)
		}

		if (optionNum == 1 && tick % 30 / 15 == 0)
			Screen.drawString(g2d, "<$numberOfEnemies>", 620, 250, 3.0)
		else
			Screen.drawString(g2d, "<$numberOfEnemies>", 620, 252, 3.0)

		Screen.drawString(g2d, "Difficulty:", 130, 300, 3.0)
		if (optionNum == 2 && tick % 30 / 15 == 0)
			Screen.drawString(g2d, "<" + DIFFICULTIES[difficulty] + ">", 500 - DIFFICULTIES[difficulty].length * 8, 300, 3.0)
		else
			Screen.drawString(g2d, "<" + DIFFICULTIES[difficulty] + ">", 500 - DIFFICULTIES[difficulty].length * 8, 302, 3.0)

		for (i in 0 until S1.length) {
			if (tick % (S1.length * 7) / 7 == i)
				Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 664, 2.0)
			else
				Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 666, 2.0)
		}
	}

	override fun renderGL() {
		ImageRenderer.draw(G.bgImage, vec2(0), vec2(Window.width, Window.height))

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
