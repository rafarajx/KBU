package state

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.IOException

import javax.imageio.ImageIO

import core.Canvas
import core.Input
import core.Main
import core.Screen
import sound.SimpleSound

object Menu {
    var BG: BufferedImage? = null
    private val OPTIONS = arrayOf("Start Game", "Help", "Exit")
    private var cursor = 0
    private var tick = 0

    init {
        try {
            BG = ImageIO.read(Menu::class.java!!.getResourceAsStream("/MenuBackground.png"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun render(g2d: Graphics2D) {
        g2d.drawImage(BG, 0, 0, Canvas.WIDTH, Canvas.HEIGHT, null)
        Screen.drawString(g2d, "fps: " + Canvas.FPS, 20, 20, 1.0)
        Screen.drawString(g2d, Main.GAME_TITLE, Canvas.WIDTH / 2 - Main.GAME_TITLE.length / 2 * 32, 70, 4.0)
        g2d.color = Color(180, 180, 180, 180)
        g2d.fill3DRect(312, 190, 285, 170, true)
        for (i in OPTIONS.indices) {
            if (cursor == i) {
                if (tick % 30 / 15 == 0) {
                    Screen.drawString(g2d, OPTIONS[i] + "<", Canvas.WIDTH / 2 - OPTIONS[i].length / 2 * 3 * 8, 200 + i * 45, 3.0)
                } else {
                    Screen.drawString(g2d, OPTIONS[i] + "<", Canvas.WIDTH / 2 - OPTIONS[i].length / 2 * 3 * 8, 200 + i * 45 + 2, 3.0)
                }
            } else {
                Screen.drawString(g2d, OPTIONS[i], Canvas.WIDTH / 2 - OPTIONS[i].length / 2 * 3 * 8, 200 + i * 45, 3.0)
            }
        }
    }

    fun update() {
        if (tick % 5 == 0) {
            if (Input.isKeyDown(38) && cursor > 0) cursor--
            if (Input.isKeyDown(40) && cursor < OPTIONS.size - 1) cursor++
        }
        if (tick % 5 == 0 && Input.isKeyDown(10)) {
            SimpleSound.pickup.play()
            when (cursor) {
                0 -> StateManager.changeState(StateManager.State.GameOptions)
                1 -> StateManager.changeState(StateManager.State.Help)
                2 -> {
                    SimpleSound.Die.play()
                    System.exit(0)
                }
            }
        }
        tick++
    }
}
