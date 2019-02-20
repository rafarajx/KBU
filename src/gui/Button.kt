package gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage

import core.Input

class Button(private val x: Int, private val y: Int, private val w: Int, private val h: Int) {
    private var content: BufferedImage? = null

    val isPressed: Boolean
        get() = Input.mousePressed &&
                Input.mouseX > x &&
                Input.mouseX < x + w &&
                Input.mouseY > y &&
                Input.mouseY < y + h

    fun setContent(content: BufferedImage) {
        this.content = content
    }

    fun render(g2d: Graphics2D) {
        g2d.drawImage(content, x, y, w, h, null)
    }
}
