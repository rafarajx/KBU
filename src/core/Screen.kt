package core

import math.AABB
import math.vec2
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object Screen {
	
	var TilesetArray = ArrayList<BufferedImage>()

	var house = ImageIO.read(Window::class.java.getResourceAsStream("/House.png"))!!
	var windmill = ImageIO.read(Window::class.java.getResourceAsStream("/Windmill.png"))!!
	var tower = ImageIO.read(Window::class.java.getResourceAsStream("/Tower.png"))!!
	var woodCamp = ImageIO.read(Window::class.java.getResourceAsStream("/WoodCamp.png"))!!
	var quarry = ImageIO.read(Window::class.java.getResourceAsStream("/Quarry.png"))!!
	var barracks = ImageIO.read(Window::class.java.getResourceAsStream("/Barracks.png"))!!
	var barricade = ImageIO.read(Window::class.java.getResourceAsStream("/Barricade.png"))!!

	private const val TILESET_WIDTH = 16
	private const val TILESET_HEIGHT = 16
	var fontArray = ArrayList<BufferedImage>()
	const val LETTER_WIDTH = 8
	const val LETTER_HEIGHT = 11
	private const val CHARACTERS = " ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789:;<=>?!\"#$%&()* +,-./"
	
	fun init(Tileset: BufferedImage, Font: BufferedImage) {
		for (i in 0 until TILESET_WIDTH * TILESET_HEIGHT) {
			TilesetArray.add(Tileset.getSubimage(i % TILESET_WIDTH * 16, i / TILESET_HEIGHT * 16, 16, 16))
		}
		
		for (i in 0 until Font.width / 8) {
			fontArray.add(Font.getSubimage(i * 8, 0, 8, 11))
		}
	}
	
	fun drawString(g2d: Graphics2D, text: String, x: Int, y: Int, scale: Double) {
		var xnum = 0
		var ynum = 0
		loop@ for (i in 0 until text.length) {
			var letter = text[i]
			val x2 = x + xnum.toDouble() * 8.0 * scale
			val y2 = y + ynum.toDouble() * 11.0 * scale
			xnum++
			when (letter) {
				'ą' -> letter = 'a'
				'ć' -> letter = 'c'
				'ę' -> letter = 'e'
				'ł' -> letter = 'l'
				'ń' -> letter = 'n'
				'ó' -> letter = 'o'
				'ś' -> letter = 's'
				'ź' -> letter = 'z'
				'ż' -> letter = 'z'
				'\n' -> {
					ynum++
					xnum = 0
					continue@loop
				}
			}
			g2d.drawImage(fontArray[getLetterId(letter)], x2.toInt(), y2.toInt(), (LETTER_WIDTH * scale).toInt(), (LETTER_HEIGHT * scale).toInt(), null)
		}
	}
	
	private fun getLetterId(letter: Char): Int {
		for (i in 0 until CHARACTERS.length) {
			if (letter == CHARACTERS[i]) {
				return i
			}
		}
		return 0
	}
	
	fun draw(g2d: Graphics2D, rect: AABB) {
		g2d.drawRect(rect.x.toInt(), rect.y.toInt(), rect.width.toInt(), rect.height.toInt())
	}
	
	fun drawTile(g2d: Graphics2D, TileX: Int, TileY: Int, x: Int, y: Int, width: Int, height: Int) {
		g2d.drawImage(TilesetArray[TileX + TILESET_WIDTH * TileY], x, y, width, height, null)
	}
	
	fun drawTile(g2d: Graphics2D, TileX: Int, TileY: Int, p: vec2, width: Int, height: Int) {
		g2d.drawImage(TilesetArray[TileX + TILESET_WIDTH * TileY], p.x.toInt(), p.y.toInt(), width, height, null)
	}
}
