package state

import java.awt.Color
import java.awt.Graphics2D

import core.Canvas
import core.Input
import core.Screen
import sound.SimpleSound

object Help {
    
    private val HELP_TEXT = arrayOf(
        "Control:",
        "LMB to place buildings",
        "RMB to move through map",
        "LEFT-RIGHT key arrows to change between buildings",
        "Numpad +/- to adjust speed of the game",
        "Press escape to quit..."
    )

    fun render(g2d: Graphics2D) {
        g2d.drawImage(Menu.BG, 0, 0, Canvas.width, Canvas.height, null)
        g2d.color = Color(180, 180, 180, 180)
        Screen.drawString(g2d, "FPS: " + Canvas.FPS, 20, 20, 1.0)
        g2d.fill3DRect(60, 200 - 40, Canvas.width - 120, HELP_TEXT.size * 50 + 50, true)
        for (i in HELP_TEXT.indices) {
            Screen.drawString(g2d, HELP_TEXT[i], Canvas.width / 2 - HELP_TEXT[i].length * 8, 200 + i * 50, 2.0)
        }
    }

    fun update() {
        if (Input.isKeyDown(27)) {
            SimpleSound.pickup.play()
            StateManager.changeState(StateManager.State.Menu)
        }
    }
}
