package state

import core.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import sound.SimpleSound
import java.awt.event.KeyEvent

object Menu: GameState() {
	var BG: BufferedImage? = null
	private val OPTIONS = arrayOf("Start Game", "Help", "Exit")
	private var cursor = 0
	private var tick = 0
	
	init {
		BG = ImageIO.read(Menu::class.java.getResourceAsStream("/MenuBackground.png"))
		//SimpleSound.soundtrack.loop(Int.MAX_VALUE)
	}
	
	private var keyPressed: Input.KEvent? = null
	
	override fun onSet() {
		keyPressed = object: Input.KEvent {
			override fun get(e: KeyEvent) {
				when(e.keyCode) {
					10 -> {
						SimpleSound.pickup.play()
						when (cursor) {
							0 -> {
								Input.keyPressed = null
								StateManager.state = GameOptions
							}
							1 -> {
								Input.keyPressed = null
								StateManager.state = Help
							}
							2 -> {
								SimpleSound.die.play()
								System.exit(0)
							}
						}
					}
					38 -> {
						if(cursor > 0) cursor--
					}
					40 -> {
						if(cursor < OPTIONS.size - 1) cursor++
					}
				}
			}
		}
		Input.keyPressed = keyPressed
	}
	
	override fun render(g2d: Graphics2D) {
		g2d.drawImage(BG, 0, 0, Canvas.width, Canvas.height, null)
		Screen.drawString(g2d, "fps: " + Canvas.FPS, 20, 20, 1.0)
		Screen.drawString(g2d, Main.GAME_TITLE, Canvas.width / 2 - Main.GAME_TITLE.length / 2 * 32, 70, 4.0)
		g2d.color = Color(180, 180, 180, 180)
		g2d.fill3DRect(Canvas.width / 2 - 200, Canvas.height / 2 - 250, 400, 300, true)
		for (i in OPTIONS.indices) {
			if (cursor == i) {
				if (tick % 30 / 15 == 0) {
					Screen.drawString(g2d, OPTIONS[i] + "<", Canvas.width / 2 - OPTIONS[i].length / 2 * 3 * 8, Canvas.height / 2 - 200 + i * 45, 3.0)
				} else {
					Screen.drawString(g2d, OPTIONS[i] + "<", Canvas.width / 2 - OPTIONS[i].length / 2 * 3 * 8, Canvas.height / 2 - 200 + i * 45 + 2, 3.0)
				}
			} else {
				Screen.drawString(g2d, OPTIONS[i], Canvas.width / 2 - OPTIONS[i].length / 2 * 3 * 8, Canvas.height / 2 - 200 + i * 45, 3.0)
			}
		}
	}
	
	override fun update() {
		
		tick++
	}
}
