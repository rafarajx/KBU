package state

import java.awt.Color
import java.awt.Graphics2D

import core.Canvas
import core.GameState
import core.Input
import core.Screen
import gametype.FFA
import gametype.SuppressUprising
import gametype.ZombieApocalypse
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
			Screen.drawString(
				g2d,
				"<" + DIFFICULTIES[difficulty] + ">",
				500 - DIFFICULTIES[difficulty].length * 8,
				300,
				3.0
			)
		else
			Screen.drawString(
				g2d,
				"<" + DIFFICULTIES[difficulty] + ">",
				500 - DIFFICULTIES[difficulty].length * 8,
				302,
				3.0
			)
		
		for (i in 0 until S1.length) {
			if (tick % (S1.length * 7) / 7 == i)
				Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 664, 2.0)
			else
				Screen.drawString(g2d, S1.substring(i, i + 1), 240 + i * 16, 666, 2.0)
		}
	}
	
	override fun update() {
		
		tick++
	}
}
