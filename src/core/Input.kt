package core

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class Input : MouseListener, MouseMotionListener, KeyListener {

    override fun keyPressed(e: KeyEvent) {
        keys[e.keyCode] = true
    }

    override fun keyReleased(e: KeyEvent) {
        val code = e.keyCode
        keys[code] = false
        ticks[code] = 0
    }

    override fun keyTyped(e: KeyEvent) {}

    override fun mouseDragged(e: MouseEvent) {
        mouseX = e.x
        mouseY = e.y
        mouseDragged = true
        mouseMoved = false
    }

    override fun mouseMoved(e: MouseEvent) {
        mouseX = e.x
        mouseY = e.y
        mouseDragged = false
        mouseMoved = true
    }

    override fun mouseClicked(e: MouseEvent) {}

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseExited(e: MouseEvent) {}

    override fun mousePressed(e: MouseEvent) {
        mouseButton = e.button
        mousePressed = true
    }

    override fun mouseReleased(e: MouseEvent) {
        mousePressed = false
    }

    companion object {
        var mouseX: Int = 0
        var mouseY: Int = 0
        var mouseButton: Int = 0
        var mousePressed: Boolean = false
        var mouseDragged: Boolean = false
        var mouseMoved: Boolean = false
        private val keys = BooleanArray(1024)
        private val ticks = IntArray(1024)

        fun isKeyDown(code: Int): Boolean {
            return keys[code] && ticks[code]++ % 2 == 0
        }
    }
}
