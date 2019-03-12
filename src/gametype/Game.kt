package gametype

import core.*
import entity.Knight
import entity.building.*
import entity.nature.*
import fraction.Fraction
import fraction.Player
import math.AABB
import math.vec2
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.*

open class Game: GameState(){
	
	override fun render(g2d: Graphics2D) {}
	
	override fun update() {}
	
	override fun onSet() { setBasicInput() }
	
	companion object {
		var x1 = 0.0f
		var y1 = 0.0f
		var showHealth = false
		var canBuild = false
		
		@JvmStatic
		var camera = vec2(0.0f, 0.0f)
		var random = Random()
		const val MAP_SIZE = 3200
		var mouseX = 0
		var mouseY = 0
		var fractionList = ArrayList<Fraction>()
		var player: Player = Player(vec2(0.0f, -900.0f), Color.BLUE, Resources(100, 60, 10, 200), 0)
		
		var natureList = ArrayList<Nature>()
		var cloudList = ArrayList<Cloud>()
		var flowersList = ArrayList<Flowers>()
		var grassList = ArrayList<Grass>()
		var rockList = ArrayList<Rock>()
		var treeList = ArrayList<Tree>()
		var wheatList = ArrayList<Wheat>()
		
		//var rockTree = QuadTree<Rock>(vec2(-2000, -2000), vec2(2000, 2000))
		var treeTree = QuadTree<Tree>(null, vec2(-3000, -3000), vec2(3000, 3000))
		var rockTree = QuadTree<Rock>(null, vec2(-3000, -3000), vec2(3000, 3000))
		
		
		var knightList = ArrayList<Knight>(100)
		
		//var knightTree = QuadTree<Knight>(null, vec2(-3000, -3000), vec2(3000, 3000))
		
		private val MENU_BAR = arrayOf(
			IntArray(2),
			intArrayOf(0, 1),
			intArrayOf(0, 2),
			intArrayOf(0, 3),
			intArrayOf(0, 4),
			intArrayOf(0, 5),
			intArrayOf(0, 6)
		)
		
		var selectedBuilding = 0
		private val COSTS = arrayOf(House.COST, Windmill.COST, Tower.COST, WoodCamp.COST, Quarry.COST, Barrack.COST, Barricade.COST)
		private val RESOURCE_NAMES = arrayOf("Wood", "Stone", "Iron", "Food")
		private const val youWon = "You Won!"
		private const val youLost = "You Lost!"
		var AF: AffineTransform? = null
		private const val MINIMAP_SIZE = 100
		private var minimap = BufferedImage(MINIMAP_SIZE, MINIMAP_SIZE, BufferedImage.TYPE_INT_RGB)
		var tick = 1
		
		fun setupMenuBar() {
		
		}
		
		fun setBasicInput(){
			Input.mouseDragged = object: Input.MEvent {
				override fun get(e: MouseEvent) {
					
					mouseX = e.x
					mouseY = e.y
					
					val b1 = MouseEvent.BUTTON1_DOWN_MASK
					val b2 = MouseEvent.BUTTON2_DOWN_MASK
					val b3 = MouseEvent.BUTTON3_DOWN_MASK
					
					if (e.modifiersEx and b3 == b3) {
						val x2 = e.x.toFloat()
						val y2 = e.y.toFloat()
						val xd = x2 - x1
						val yd = y2 - y1
						x1 = e.x.toFloat()
						y1 = e.y.toFloat()
						camera.x += xd
						camera.y += yd
					}
				}
			}
			
			Input.mouseClicked = object: Input.MEvent {
				override fun get(e: MouseEvent) {
				
					
				}
			}
			
			Input.mousePressed = object: Input.MEvent {
				override fun get(e: MouseEvent){
					if(e.button == 1){
						player.placeBuilding(selectedBuilding, vec2(e.x.toFloat() - Game.camera.x, e.y.toFloat() - Game.camera.y))
					}
				}
			}
			
			Input.mouseMoved = object: Input.MEvent{
				override fun get(e: MouseEvent){
					x1 = e.x.toFloat()
					y1 = e.y.toFloat()
					mouseX = e.x
					mouseY = e.y
					
					canBuild = inRange(player.buildingList, AABB(vec2(e.x, e.y) - camera, vec2(1.0f)))
					
				}
			}
			
			Input.keyPressed = object: Input.KEvent{
				override fun get(e: KeyEvent){
					when(e.keyCode) {
						KeyEvent.VK_LEFT -> if (selectedBuilding > 0) selectedBuilding--
						KeyEvent.VK_RIGHT -> if (selectedBuilding < MENU_BAR.size - 1) selectedBuilding++
						KeyEvent.VK_ADD -> if (Canvas.UPS + 60 <= 600) {
							Canvas.UPS += 60
							Canvas.UPDATE_TIME = Canvas.SECOND / Canvas.UPS
						}
						KeyEvent.VK_SUBTRACT -> if(Canvas.UPS - 60 >= 60) {
							Canvas.UPS -= 60
							Canvas.UPDATE_TIME = Canvas.SECOND / Canvas.UPS
						}
						KeyEvent.VK_SPACE -> showHealth = true
					}
				}
			}
		}
		
		fun setupNature(fractions: Int) {
			for (i in 0..5000) {
				val x = random.nextFloat() * 6000.0f - 3000.0f
				val y = random.nextFloat() * 6000.0f - 3000.0f
				if (!isColliding(AABB(vec2(x, y), Tree.HALF_EDGE))) Tree(vec2(x, y)).add()
			}
			for (i in 0..100) {
				val x = random.nextFloat() * 6000.0f - 3000.0f
				val y = random.nextFloat() * 6000.0f - 3000.0f
				if (!isColliding(AABB(vec2(x, y), 8.0f))) Flowers(vec2(x, y)).add()
			}
			for (i in 0..100) {
				val x = random.nextFloat() * 6000.0f - 3000.0f
				val y = random.nextFloat() * 6000.0f - 3000.0f
				if (!isColliding(AABB(vec2(x, y), 8.0f))) Grass(vec2(x, y)).add()
			}
			for (i1 in 0 until 4 * fractions) {
				val x1 = random.nextFloat() * 4000.0f - 2000.0f
				val y1 = random.nextFloat() * 4000.0f - 2000.0f
				for (i2 in 0..9) {
					val x2 = random.nextFloat() * 200.0f
					val y2 = random.nextFloat() * 200.0f
					if (!isColliding(AABB(vec2(x1 + x2, y1 + y2), 8.0f))) Rock(vec2(x1 + x2, y1 + y2)).add()
				}
			}
		}
		
		private fun isColliding(r: AABB): Boolean {
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
			
			
			/*
			g2d.color = Color(255, 0, 0)
			knightTree.draw(g2d)
			*/
			
			/*
			g2d.color = Color(255, 0, 0)
			treeTree.draw(g2d)
			
			
			val qt = treeTree.search(vec2(mouseX - camera.x, mouseY - camera.y))
			g2d.color = Color.GREEN
			g2d.fillRect(qt.aabb.ul.x.toInt(), qt.aabb.ul.y.toInt(), (qt.aabb.dr.x - qt.aabb.ul.x).toInt(), (qt.aabb.dr.y - qt.aabb.ul.y).toInt())
			*/
			
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
			
			/*
			val t = treeTree.nearest(vec2(mouseX - camera.x, mouseY - camera.y))
			g2d.color = Color.BLUE
			if(t != null) {
				g2d.fillRect(t.p.x.toInt(), t.p.y.toInt(), 3, 3)
			}
			*/
			
			g2d.transform = AF
		}
		
		fun drawInterface(g2d: Graphics2D) {
			
			createMinimap()
			g2d.drawImage(minimap, Canvas.width - MINIMAP_SIZE - 5, Canvas.height - MINIMAP_SIZE - 5, null)
			
			g2d.color = Color(80, 80, 80, 255)
			g2d.fillRect(0, 0, 300, 70)
			
			g2d.color = Color(0, 0, 0, 255)
			g2d.drawString("FPS: " + Canvas.FPS, 5 + 1, 15 + 1)
			g2d.drawString("UPS: " + Canvas.UPS, 5 + 1, 30 + 1)
			g2d.drawString("TUPS: " + Canvas.TUPS, 5 + 1, 45 + 1)
			
			g2d.color = Color(255, 255, 255, 255)
			g2d.drawString("FPS: " + Canvas.FPS, 5, 15)
			g2d.drawString("UPS: " + Canvas.UPS, 5, 30)
			g2d.drawString("TUPS: " + Canvas.TUPS, 5 + 1, 45 + 1)
			
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
			g2d.drawRect(selectedBuilding * 16 * 3, Canvas.height - 60, 48, 48)
			for (i in MENU_BAR.indices) {
				Screen.drawTile(g2d, MENU_BAR[i][0], MENU_BAR[i][1], 48 * i, Canvas.height - 60, 48, 48)
				if (i == 1) Screen.drawTile(g2d, 7, 2, 48 * i, Canvas.height - 60, 48, 48)
				if (player.isDefeated || !player.resources.enough(COSTS[i])) {
					Screen.drawTile(g2d, 8, 2, 48 * i, Canvas.height - 60, 48, 48)
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
				val youLostX = Canvas.width / 2 - Screen.LETTER_WIDTH * youLost.length / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
				val youLostY = Canvas.height / 2 - Screen.LETTER_HEIGHT / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
				Screen.drawString(g2d, youLost, youLostX.toInt(), youLostY.toInt(), youLostScale)
			}
		}
		
		private fun createMinimap() {
			val g2d = minimap.graphics as Graphics2D
			g2d.color = Color.BLACK
			g2d.fillRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE)
			for (i in fractionList.indices) {
				if(!fractionList.indices.contains(i)) return
				val fraction = fractionList[i]
				g2d.color = fraction.color
				for (i in fraction.buildingList.indices) {
					if(fraction.buildingList.indices.contains(i)) {
						val b = fraction.buildingList[i]
						val x = ((b.p.x + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).coerceIn(0.0f, MAP_SIZE.toFloat())
						val y = ((b.p.y + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).coerceIn(0.0f, MAP_SIZE.toFloat())
						g2d.fillRect(x.toInt(), y.toInt(), 2, 2)
					}
				}
				for (i in fraction.entityList.indices) {
					if(fraction.entityList.indices.contains(i)) {
						val e = fraction.entityList[i]
						val x = ((e.p.x + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).coerceIn(0.0f, MAP_SIZE.toFloat())
						val y = ((e.p.y + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).coerceIn(0.0f, MAP_SIZE.toFloat())
						g2d.fillRect(x.toInt(), y.toInt(), 1, 1)
					}
				}
			}
			g2d.color = Color.WHITE
			g2d.drawLine(0, 0, MINIMAP_SIZE - 1, 0)
			g2d.drawLine(MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1, MINIMAP_SIZE - 1)
			g2d.drawLine(MINIMAP_SIZE - 1, MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1)
			g2d.drawLine(0, MINIMAP_SIZE - 1, 0, 0)
			
			val cx1 = ((-camera.x + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).toInt()
			val cx2 = ((-camera.x + MAP_SIZE / 2 + Canvas.width) / MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy1 = ((-camera.y + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy2 = ((-camera.y + MAP_SIZE / 2 + Canvas.height) / MAP_SIZE * MINIMAP_SIZE).toInt()
			
			g2d.drawLine(cx1, cy1, cx2, cy1)
			g2d.drawLine(cx2, cy1, cx2, cy2)
			g2d.drawLine(cx2, cy2, cx1, cy2)
			g2d.drawLine(cx1, cy2, cx1, cy1)
		}
		
		fun updateObjects() {
			/*
			knightTree = QuadTree<Knight>(null, vec2(-3000, -3000), vec2(3000, 3000))
			
			
			for(knight in knightList){
				knightTree.add(knight)
			}
			*/
			for (i in fractionList.indices) {
				if (fractionList.indices.contains(i))
					fractionList[i].update()
			}
			for (i in natureList.indices) {
				if (natureList.indices.contains(i))
					natureList[i].update()
			}
		}
		
		fun <T: Building> inRange(arrayList: ArrayList<T>, r: AABB): Boolean {
			for (i in arrayList.indices) {
				val b = arrayList[i]
				if (b.range!!.intersects(r)) {
					return true
				}
			}
			return false
		}
		
		fun toTicks(time: IntArray): Int {
			return time[0] * 60 * 60 + time[1] * 10 / 60 + time[2] / 60
		}
		
		fun toTime(ticks: Int): IntArray {
			return intArrayOf(Math.abs(ticks / 3600), Math.abs(ticks / 60 % 60 / 10), Math.abs(ticks / 60 % 10))
		}
	}
}