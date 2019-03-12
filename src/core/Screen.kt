package core

import math.AABB
import math.vec2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object Screen {
	
	var TilesetArray = ArrayList<BufferedImage>()
	
	var house = ImageIO.read(Main::class.java.getResourceAsStream("/House.png"))!!
	var windmill = ImageIO.read(Main::class.java.getResourceAsStream("/Windmill.png"))!!
	var tower = ImageIO.read(Main::class.java.getResourceAsStream("/Tower.png"))!!
	var woodCamp = ImageIO.read(Main::class.java.getResourceAsStream("/WoodCamp.png"))!!
	var quarry = ImageIO.read(Main::class.java.getResourceAsStream("/Quarry.png"))!!
	var barracks = ImageIO.read(Main::class.java.getResourceAsStream("/Barracks.png"))!!
	var barricade = ImageIO.read(Main::class.java.getResourceAsStream("/Barricade.png"))!!
	
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
	
	fun showInfo(g2d: Graphics2D, PositionX: Float, PositionY: Float, Title: String, Text: Array<String>, Numbers: IntArray) {
		g2d.color = Color(80, 80, 80, 180)
		g2d.fillRect(PositionX.toInt(), PositionY.toInt(), Math.max(Title.length * 7 + 20, 80), Text.size * 15 + 30)
		g2d.drawRect(PositionX.toInt(), PositionY.toInt(), Math.max(Title.length * 7 + 20, 80), Text.size * 15 + 30)
		g2d.color = Color(255, 255, 200, 180)
		g2d.drawString(Title, 10 + PositionX.toInt(), 15 + PositionY.toInt())
		g2d.color = Color(255, 255, 255, 180)
		for (i in Text.indices) {
			g2d.drawString(Text[i] + ": " + Numbers[i], 10 + PositionX.toInt(), i * 15 + 35 + PositionY.toInt())
		}
	}
	
	fun showInfo(g2d: Graphics2D, PositionX: Float, PositionY: Float, Title: String, Text: Array<String>, resources: Resources) {
		g2d.color = Color(80, 80, 80, 180)
		g2d.fillRect(PositionX.toInt(), PositionY.toInt(), Math.max(Title.length * 7 + 20, 80), Text.size * 15 + 30)
		g2d.drawRect(PositionX.toInt(), PositionY.toInt(), Math.max(Title.length * 7 + 20, 80), Text.size * 15 + 30)
		g2d.color = Color(255, 255, 200, 180)
		g2d.drawString(Title, 10 + PositionX.toInt(), 15 + PositionY.toInt())
		g2d.color = Color(255, 255, 255, 180)
		g2d.drawString(Text[0] + ": " + resources.wood, 10 + PositionX.toInt(), 0 * 15 + 35 + PositionY.toInt())
		g2d.drawString(Text[1] + ": " + resources.stone, 10 + PositionX.toInt(), 1 * 15 + 35 + PositionY.toInt())
		g2d.drawString(Text[2] + ": " + resources.iron, 10 + PositionX.toInt(), 2 * 15 + 35 + PositionY.toInt())
		g2d.drawString(Text[3] + ": " + resources.food, 10 + PositionX.toInt(), 3 * 15 + 35 + PositionY.toInt())
	}
}
