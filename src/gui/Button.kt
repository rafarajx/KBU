package gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Button(private val x: Int, private val y: Int, private val w: Int, private val h: Int) {
	private var content: BufferedImage? = null
	
	fun isPressed(): Boolean {
		return false
	}
	
	fun setText(content: BufferedImage) {
		this.content = content
	}
	
}
