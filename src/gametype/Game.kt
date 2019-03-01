package gametype

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.util.ArrayList
import java.util.Random

import building.Barrack
import building.Barricade
import building.House
import building.Mill
import building.Quarry
import building.Tower
import building.WoodCamp
import core.Canvas
import core.Input
import core.Resources
import core.Screen
import fraction.Fraction
import fraction.Player
import math.vec2
import nature.*

open class Game {
	
	open fun render(g2d: Graphics2D) {}
	
	open fun update() {}
	
	companion object {
		
		@JvmStatic
		var camera = vec2(0.0f, 0.0f)
		var random = Random()
		val MAP_SIZE = 2000
		var mouseX: Int = 0
		var mouseY: Int = 0
		//val PLAYER_INDEX = 0
		var fractionList = ArrayList<Fraction>()
		var player: Player = Player(vec2(1000.0f, 100.0f), Color.BLUE, Resources(100, 60, 10, 200), 0)
		
		var natureList = ArrayList<Nature>()
		var cloudList = ArrayList<Cloud>()
		var flowersList = ArrayList<Flowers>()
		var grassList = ArrayList<Grass>()
		var rockList = ArrayList<Rock>()
		var treeList = ArrayList<Tree>()
		var wheatList = ArrayList<Wheat>()
		
		
		private val MENU_BAR = arrayOf(
			IntArray(2),
			intArrayOf(0, 1),
			intArrayOf(0, 2),
			intArrayOf(0, 3),
			intArrayOf(0, 4),
			intArrayOf(0, 5),
			intArrayOf(0, 6)
		)
		var MENU_BAR_SCALE = 3
		var SELECTED_BUILDING = 0
		private val COSTS = arrayOf(House.COST, Mill.COST, Tower.COST, WoodCamp.COST, Quarry.COST, Barrack.COST, Barricade.COST)
		private val RESOURCE_NAMES = arrayOf("Wood", "Stone", "Iron", "Food")
		private const val youWon = "You Won!"
		private const val youLost = "You Lost!"
		var MBFields = ArrayList<Rectangle2D>()
		var AF: AffineTransform? = null
		var x1: Float = 0.0f
		var y1: Float = 0.0f
		private const val MINIMAP_SIZE = 100
		private var minimap = BufferedImage(MINIMAP_SIZE, MINIMAP_SIZE, BufferedImage.TYPE_INT_RGB)
		var tick = 1
		
		fun setupMenuBar() {
			for (i in MENU_BAR.indices)
				MBFields.add(
					Rectangle2D.Float(
						i * 16.0f * MENU_BAR_SCALE,
						Canvas.height - 60.0f,
						16.0f * MENU_BAR_SCALE,
						16.0f * MENU_BAR_SCALE
					)
				)
		}
		
		fun setupNature(fractions: Int) {
			for (i in 0..1000) {
				val x = random.nextInt(3200) - 600.toFloat()
				val y = random.nextInt(3200) - 600.toFloat()
				if (!isColliding(
						Rectangle2D.Float(
							x - Tree.EDGE_LENGTH / 2,
							y - Tree.EDGE_LENGTH / 2,
							Tree.EDGE_LENGTH.toFloat(),
							Tree.EDGE_LENGTH.toFloat()
						)
					)
				) {
					Tree(vec2(x, y)).add()
				}
			}
			for (i in 0..49) {
				val x = random.nextInt(3200) - 600.toFloat()
				val y = random.nextInt(3200) - 600.toFloat()
				if (!isColliding(Rectangle2D.Float(x - 8.0f, y - 8.0f, 16.0f, 16.0f))) {
					Flowers(vec2(x, y)).add()
				}
			}
			for (i in 0..49) {
				val x = random.nextInt(3200) - 600.toFloat()
				val y = random.nextInt(3200) - 600.toFloat()
				if (!isColliding(Rectangle2D.Float(x - 8.0f, y - 8.0f, 16.0f, 16.0f))) {
					Grass(vec2(x, y)).add()
				}
			}
			for (i1 in 0 until 4 * fractions) {
				val x1 = random.nextInt(3200) - 600.toFloat()
				val y1 = random.nextInt(3200) - 600.toFloat()
				for (i2 in 0..9) {
					val x2 = random.nextInt(200).toFloat()
					val y2 = random.nextInt(200).toFloat()
					if (!isColliding(Rectangle2D.Float(x1 + x2 - 8.0f, y1 + y2 - 8.0f, 16.0f, 16.0f))) {
						Rock(vec2(x1 + x2, y1 + y2)).add()
					}
				}
			}
		}
		
		private fun isColliding(r: Rectangle2D): Boolean {
			for (f in fractionList.indices) {
				val fraction = fractionList[f]
				for (j in fraction.buildingList.indices) {
					val building = fraction.buildingList[j]
					if (building.field!!.intersects(r)) return true
				}
				for (j in fraction.entityList.indices) {
					val entity = fraction.entityList[j]
					if (entity.field!!.intersects(r)) return true
				}
			}
			for (i in natureList.indices) {
				val nature = natureList[i]
				if (nature.field!!.intersects(r)) return true
			}
			return false
		}
		
		fun drawObjects(g2d: Graphics2D) {
			AF = g2d.transform
			g2d.translate(camera.x.toInt(), camera.y.toInt())
			
			
			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].renderBuildings(g2d)
			}
			
			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].renderEntities(g2d)
			}
			
			for(i in natureList.indices){
				if(natureList.indices.contains(i))
					natureList[i].render(g2d)
			}
			
			
			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].drawStatus(g2d)
			}
			
			g2d.transform = AF
		}
		
		fun drawInterface(g2d: Graphics2D) {
			createMinimap()
			g2d.drawImage(minimap, Canvas.width - MINIMAP_SIZE - 5, Canvas.height - MINIMAP_SIZE - 5, null)
			
			g2d.color = Color(80, 80, 80, 180)
			g2d.fillRoundRect(0, 0, 100, 70, 10, 10)
			
			g2d.color = Color(0, 0, 0, 255)
			g2d.drawString("fps: " + Canvas.FPS, 5 + 1, 15 + 1)
			g2d.drawString("ups: " + Canvas.UPS, 5 + 1, 30 + 1)
			
			g2d.color = Color(255, 255, 255, 255)
			g2d.drawString("fps: " + Canvas.FPS, 5, 15)
			g2d.drawString("ups: " + Canvas.UPS, 5, 30)
			
			g2d.color = Color(80, 80, 80, 180)
			g2d.fillRoundRect(105, 0, 100, 70, 10, 10)
			
			g2d.color = Color(0, 0, 0, 255)
			g2d.drawString("Wood: " + player.resources.wood, 115 + 1, 15 + 1)
			g2d.drawString("Stone: " + player.resources.stone, 115 + 1, 30 + 1)
			g2d.drawString("Iron: " + player.resources.iron, 115 + 1, 45 + 1)
			g2d.drawString("Food: " + player.resources.food, 115 + 1, 60 + 1)
			
			g2d.color = Color(100, 200, 20, 255)
			g2d.drawString("Wood: " + player.resources.wood, 115, 15)
			g2d.drawString("Stone: " + player.resources.stone, 115, 30)
			g2d.drawString("Iron: " + player.resources.iron, 115, 45)
			g2d.drawString("Food: " + player.resources.food, 115, 60)
			
			g2d.color = Color(80, 80, 80, 180)
			g2d.fillRoundRect(210, 0, 100, 70, 10, 10)
			
			g2d.color = Color(0, 0, 0, 255)
			g2d.drawString("Pop: " + player.population, 220 + 1, 15 + 1)
			g2d.drawString("MaxPop: " + player.maxPopulation, 220 + 1, 30 + 1)
			g2d.drawString("Buildings: " + player.buildingList.size, 220 + 1, 45 + 1)
			g2d.drawString("Soldiers: " + player.entityList.size, 220 + 1, 60 + 1)
			
			g2d.color = Color(100, 150, 255, 255)
			g2d.drawString("Pop: " + player.population, 220, 15)
			g2d.drawString("MaxPop: " + player.maxPopulation, 220, 30)
			g2d.drawString("Buildings: " + player.buildingList.size, 220, 45)
			g2d.drawString("Soldiers: " + player.entityList.size, 220, 60)
			
			g2d.color = Color(80, 80, 80, 180)
			g2d.fillRoundRect(0, Canvas.height - 60, MENU_BAR.size * 50, 70, 10, 10)
			g2d.color = Color.BLACK
			g2d.stroke = BasicStroke(1.5f)
			g2d.drawRect(SELECTED_BUILDING * 16 * 3, Canvas.height - 60, 48, 48)
			for (i in MENU_BAR.indices) {
				Screen.drawTile(g2d, MENU_BAR[i][0], MENU_BAR[i][1], 48 * i, Canvas.height - 60, 48, 48)
				if (i == 1) Screen.drawTile(g2d, 7, 2, 48 * i, Canvas.height - 60, 48, 48)
				if (player.isDefeated || !player.resources.enough(COSTS[i])) {
					Screen.drawTile(g2d, 8, 2, 48 * i, Canvas.height - 60, 48, 48)
				}
			}
			for (i in MENU_BAR.indices) {
				if (MBFields[i].contains(mouseX.toDouble(), mouseY.toDouble())) {
					Screen.showInfo(g2d, mouseX.toFloat(), mouseY - 80.0f, "", RESOURCE_NAMES, COSTS[i])
				}
			}
			if (fractionList.size < 2 && !player.isDefeated) {
				val youWonScale = 4 + Math.sin(tick / 20.0f % Math.PI * 2)
				val youWonX = Canvas.width / 2 - Screen.LETTER_WIDTH * youWon.length / 2 * youWonScale
				val youWonY = Canvas.height / 2 - Screen.LETTER_HEIGHT * youWonScale / 2
				Screen.drawString(g2d, youWon, youWonX.toInt(), youWonY.toInt(), youWonScale)
			}
			if (player.isDefeated) {
				val youLostScale = 4.0
				val youLostX =
					Canvas.width / 2 - Screen.LETTER_WIDTH * youLost.length / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(
						10 + 1
					) - 5)
				val youLostY =
					Canvas.height / 2 - Screen.LETTER_HEIGHT / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(
						10 + 1
					) - 5)
				Screen.drawString(g2d, youLost, youLostX.toInt(), youLostY.toInt(), youLostScale)
			}
		}
		
		private fun createMinimap() {
			val g2d = minimap.graphics as Graphics2D
			g2d.color = Color.BLACK
			g2d.fillRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE)
			for (fraction in fractionList) {
				g2d.color = fraction.color
				for (i in fraction.buildingList.indices) {
					if(fraction.buildingList.indices.contains(i)) {
						var building = fraction.buildingList[i]
						val x = Math.min(Math.max(0, (building.p.x / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
						val y = Math.min(Math.max(0, (building.p.y / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
						g2d.fillRect(x, y, 2, 2)
					}
				}
				for (i in fraction.entityList.indices) {
					if(fraction.entityList.indices.contains(i)) {
						var entity = fraction.entityList[i]
						val x = Math.min(Math.max(0, (entity.p.x / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
						val y = Math.min(Math.max(0, (entity.p.y / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
						g2d.fillRect(x, y, 1, 1)
					}
				}
			}
			g2d.color = Color.WHITE
			g2d.drawLine(0, 0, MINIMAP_SIZE - 1, 0)
			g2d.drawLine(MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1, MINIMAP_SIZE - 1)
			g2d.drawLine(MINIMAP_SIZE - 1, MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1)
			g2d.drawLine(0, MINIMAP_SIZE - 1, 0, 0)
			
			val cx1 = ((-camera.x) / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
			val cx2 = ((-camera.x + Canvas.width) / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy1 = ((-camera.y) / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy2 = ((-camera.y + Canvas.height) / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
			
			g2d.drawLine(cx1, cy1, cx2, cy1)
			g2d.drawLine(cx2, cy1, cx2, cy2)
			g2d.drawLine(cx2, cy2, cx1, cy2)
			g2d.drawLine(cx1, cy2, cx1, cy1)
		}
		
		fun updateObjects() {
			for (i in fractionList.indices) {
				if (fractionList.indices.contains(i))
					fractionList[i].update()
			}
			for (i in natureList.indices) {
				if (natureList.indices.contains(i))
					natureList[i].update()
			}
		}
		
		fun updateInput() {
			if (Input.mousePressed) {
				if (Input.mouseButton == 1) {
					player.placeBuilding(SELECTED_BUILDING, vec2(Input.mouseX.toFloat(), Input.mouseY.toFloat()))
				}
			}
			if (Input.mouseMoved) {
				mouseX = Input.mouseX
				mouseY = Input.mouseY
				x1 = Input.mouseX.toFloat()
				y1 = Input.mouseY.toFloat()
			}
			if (Input.mouseButton == 3 || Input.mouseButton == 2) {
				val x2 = Input.mouseX.toFloat()
				val y2 = Input.mouseY.toFloat()
				val xd = x2 - x1
				val yd = y2 - y1
				x1 = Input.mouseX.toFloat()
				y1 = Input.mouseY.toFloat()
				camera.x += xd
				camera.y += yd
			}
			if (tick % 5 == 0) {
				if (Input.isKeyDown(37) && SELECTED_BUILDING > 0) SELECTED_BUILDING--
				if (Input.isKeyDown(39) && SELECTED_BUILDING < MENU_BAR.size - 1) SELECTED_BUILDING++
			}
		}
		
		fun toTicks(time: IntArray): Int {
			return time[0] * 60 * 60 + time[1] * 10 / 60 + time[2] / 60
		}
		
		fun toTime(ticks: Int): IntArray {
			return intArrayOf(Math.abs(ticks / 3600), Math.abs(ticks / 60 % 60 / 10), Math.abs(ticks / 60 % 10))
		}
	}
}