package gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Button(private val x: Int, private val y: Int, private val w: Int, private val h: Int) {
	private var content: BufferedImage? = null
	
	fun isPressed(): Boolean {
		return false
	}
	
	fun setContent(content: BufferedImage) {
		this.content = content
	}
	
	fun render(g2d: Graphics2D) {
		g2d.drawImage(content, x, y, w, h, null)
	}
}
