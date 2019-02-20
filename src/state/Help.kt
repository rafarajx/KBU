package state

import java.awt.Color
import java.awt.Graphics2D

import core.Canvas
import core.Input
import core.Screen
import sound.SimpleSound

object Help {
    private val HELP_TEXT = arrayOf("Control:", "mouse, keyboard", "Press escape to quit...")

    fun render(g2d: Graphics2D) {
        g2d.drawImage(Menu.BG, 0, 0, 900, 800, null)
        g2d.color = Color(180, 180, 180, 180)
        g2d.fill3DRect(170, 180, 600, 200, true)
        Screen.drawString(g2d, "fps: " + Canvas.FPS, 20, 20, 1.0)
        for (i in HELP_TEXT.indices) {
            Screen.drawString(g2d, HELP_TEXT[i], 450 - HELP_TEXT[i].length / 2 * 24, 200 + i * 50, 3.0)
        }
    }

    fun update() {
        if (Input.isKeyDown(27)) {
            SimpleSound.pickup.play()
            StateManager.changeState(StateManager.State.Menu)
        }
    }
}
