package gametype

import core.Window
import core.*
import entity.Knight
import entity.building.*
import entity.nature.*
import fraction.Fraction
import fraction.Player
import math.AABB
import math.vec2
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.abs
import kotlin.math.sin

open class Game: GameState(){
	
	override fun render(g2d: Graphics2D) {}
	
	override fun update() {}
	
	override fun windowPosCallback(xpos: Int, ypos: Int){

	}

	override fun cursorPosCallback(xpos: Float, ypos: Float) {

		mouseX = xpos.toInt()
		mouseY = Window.height - ypos.toInt()

		mousePos = vec2(xpos, Window.height - ypos)

		if(mapPressed) {
			mousePos0 = mousePos.copy()
			val posd = mousePos0 - mousePos1
			mousePos1 = mousePos.copy()
			camera += posd
		}

	}

	override fun mouseButtonCallback(button: Int, action: Int, mods: Int) {

		when(button) {
			GLFW.GLFW_MOUSE_BUTTON_1 -> {
				val pos = vec2(
					mousePos.x - camera.x,
					mousePos.y - camera.y
				)
				player.placeBuilding(selectedBuilding, pos)
			}
			GLFW.GLFW_MOUSE_BUTTON_2 -> {
				mapPressed = action == 1
				if (action == 1) {
					mousePos1 = mousePos.copy()
				}
			}
		}
	}

	override fun keyCallback(key: Int, scancode: Int, action: Int, mods: Int) {
		println("keyCallback: $action")
		when(key) {
			GLFW.GLFW_KEY_LEFT -> if(action == 1)
				if (selectedBuilding > 0) selectedBuilding--
			GLFW.GLFW_KEY_RIGHT -> if(action == 1)
				if(selectedBuilding < MENU_BAR.size - 1) selectedBuilding++
			GLFW.GLFW_KEY_KP_ADD -> if(action == 1) if (UPS + 60 <= 600) {
				UPS += 60
				UPDATE_TIME = 1.0 / UPS
			}
			GLFW.GLFW_KEY_KP_SUBTRACT -> if(action == 1) if(UPS - 60 >= 60) {
				UPS -= 60
				UPDATE_TIME = 1.0 / UPS
			}
			GLFW.GLFW_KEY_SPACE ->
				if (action == 1) {
					showHealth = true
				} else if (action == 0) {
					showHealth = false
				}
			GLFW.GLFW_KEY_F -> {
				if(action == 1)
					fogEnabled = !fogEnabled
			}
			GLFW.GLFW_KEY_F1 -> {
				if(action == 1)
					drawchunks = !drawchunks
			}
		}
	}

	companion object {

		var mapPressed = false

		var mousePos0 = vec2(0, 0)
		var mousePos1 = vec2(0, 0)

		var drawchunks = false
		
		var speed = 1.0f

		var fogEnabled = false
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
		var mousePos: vec2 = vec2(0, 0)
		
		lateinit var player: Player
		
		
		
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

		
		
		
		
		fun drawObjectsGL() {

			/*
			g2d.color = Color(255, 0, 0)
			knightTree.draw(g2d)
			*/

			/*
			g2d.color = Color(255, 0, 0)
			treeTree.draw(g2d)
			*/
			/*
			val qt = treeTree.search(vec2(mouseX - camera.x, mouseY - camera.y))
			g2d.color = Color.GREEN
			g2d.fillRect(qt.aabb.ul.x.toInt(), qt.aabb.ul.y.toInt(), (qt.aabb.dr.x - qt.aabb.ul.x).toInt(), (qt.aabb.dr.y - qt.aabb.ul.y).toInt())
			*/

			/*
			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].renderBuildingsGL()
			}

			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].renderEntitiesGL()
			}

			for(i in World.natureList.indices){
				if(World.natureList.indices.contains(i))
					World.natureList[i].renderGL()
			}

			for(i in fractionList.indices){
				if(fractionList.indices.contains(i))
					fractionList[i].drawStatusGL()
			}
			*/
			
			World.renderGL()

			/*
			val t = treeTree.nearest(vec2(mouseX - camera.x, mouseY - camera.y))
			g2d.color = Color.BLUE
			if(t != null) {
				g2d.fillRect(t.p.x.toInt(), t.p.y.toInt(), 3, 3)
			}
			*/
		}
		
		fun drawInterfaceGL() {

			createMinimap()
			//g2d.drawImage(minimap, Canvas.width - MINIMAP_SIZE - 5, Canvas.height - MINIMAP_SIZE - 5, null)
			
			RectRenderer.disableCamera()
			RectRenderer.depth = -0.1f
			RectRenderer.setColor(0.3f, 0.3f, 0.3f, 1.0f)
			RectRenderer.fill(vec2(0, Window.height - 70), vec2(320, 70))
			
			TextRenderer.depth = -0.2f
			TextRenderer.setColor(0.0f)
			TextRenderer.draw("FPS: " + FPS, vec2(5 + 1, Window.height - (15 + 1)))
			TextRenderer.draw("UPS: " + UPS, vec2(5 + 1, Window.height - (30 + 1)))
			TextRenderer.draw("TUPS: " + TUPS, vec2(5 + 1, Window.height - (45 + 1)))
			TextRenderer.draw("TFPS: " + TFPS, vec2(5 + 1, Window.height - (60 + 1)))
			TextRenderer.draw("NOS: " + batch.numberOfSprites, vec2(5 + 1, Window.height - (75 + 1)))
			TextRenderer.draw("RC: " + batch.renderablechunks, vec2(5 + 1, Window.height - (90 + 1)))
			TextRenderer.setColor(0.0f)
			TextRenderer.draw("Wood: " + player.resources.wood, vec2(115 + 1, Window.height - (15 + 1)))
			TextRenderer.draw("Stone: " + player.resources.stone, vec2(115 + 1, Window.height - (30 + 1)))
			TextRenderer.draw("Iron: " + player.resources.iron, vec2(115 + 1, Window.height - (45 + 1)))
			TextRenderer.draw("Food: " + player.resources.food, vec2(115 + 1, Window.height - (60 + 1)))
			TextRenderer.setColor(0.0f)
			TextRenderer.draw("Pop: " + player.population, vec2(220 + 1, Window.height - (15 + 1)))
			TextRenderer.draw("MaxPop: " + player.maxPopulation, vec2(220 + 1, Window.height - (30 + 1)))
			TextRenderer.draw("Buildings: " + player.buildingList.size, vec2(220 + 1, Window.height - (45 + 1)))
			TextRenderer.draw("Soldiers: " + player.entityList.size, vec2(220 + 1, Window.height - (60 + 1)))
			
			TextRenderer.depth = -0.3f
			TextRenderer.setColor(1.0f)
			TextRenderer.draw("FPS: $FPS", vec2(5, Window.height - 15))
			TextRenderer.draw("UPS: $UPS", vec2(5, Window.height - 30))
			TextRenderer.draw("TUPS: $TUPS", vec2(5, Window.height - 45))
			TextRenderer.draw("TFPS: $TFPS", vec2(5, Window.height - 60))
			TextRenderer.draw("NOS: " + batch.numberOfSprites, vec2(5, Window.height - 75))
			TextRenderer.draw("RC: " + batch.renderablechunks, vec2(5, Window.height - 90))
			TextRenderer.setColor(0.4f, 0.8f, 0.08f)
			TextRenderer.draw("Wood: " + player.resources.wood, vec2(115, Window.height - 15))
			TextRenderer.draw("Stone: " + player.resources.stone, vec2(115, Window.height - 30))
			TextRenderer.draw("Iron: " + player.resources.iron, vec2(115, Window.height - 45))
			TextRenderer.draw("Food: " + player.resources.food, vec2(115, Window.height - 60))
			TextRenderer.setColor(0.4f, 0.6f, 1.0f)
			TextRenderer.draw("Pop: " + player.population, vec2(220, Window.height - 15))
			TextRenderer.draw("MaxPop: " + player.maxPopulation, vec2(220, Window.height - 30))
			TextRenderer.draw("Buildings: " + player.buildingList.size, vec2(220, Window.height - 45))
			TextRenderer.draw("Soldiers: " + player.entityList.size, vec2(220, Window.height - 60))
			
			

			RectRenderer.setColor(0.3f, 0.7f)
			RectRenderer.fill(vec2(0), vec2(MENU_BAR.size * 50, 70))

			//g2d.stroke = BasicStroke(1.5f)
			//Canvas.drawRect(selectedBuilding * 16 * 3, Canvas.height - 60, 48, 48)
			
			RectRenderer.depth = -0.5f
			RectRenderer.setColor(0.0f)
			RectRenderer.draw(vec2(selectedBuilding * 48 + 10, 10), vec2(48), 2.0f)
			
			for (i in MENU_BAR.indices) {
				ImageRenderer.depth = -0.3f
				ImageRenderer.draw(gameBarImages[i], vec2(48 * i + 10, 10), vec2(48, 48))
				if (player.isDefeated || !player.resources.enough(COSTS[i])) {
					ImageRenderer.depth = -0.4f
					ImageRenderer.draw(crossImage, vec2(48 * i + 10, 10), vec2(48, 48))
				}
			}
			
			TextRenderer.setColor(0.0f)

			if (World.fractionList.size < 2 && !player.isDefeated) {
				val bla: Float = (tick / 20.0f % Constants.pi) * 2.0f
				val youWonScale: Float = 4.0f + sin(bla)
				val youWonX = Window.width / 2 - Screen.LETTER_WIDTH * youWon.length / 2 * youWonScale
				val youWonY = Window.height / 2 - Screen.LETTER_HEIGHT * youWonScale / 2
				TextRenderer.draw(youWon, vec2(youWonX, youWonY), youWonScale)
			}

			if (player.isDefeated) {
				val youLostScale = 4.0f
				val youLostX = Window.width / 2 - Screen.LETTER_WIDTH * youLost.length / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
				val youLostY = Window.height / 2 - Screen.LETTER_HEIGHT / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
				TextRenderer.draw(youLost, vec2(youLostX, youLostY), youLostScale)
			}
		}
		
		private fun createMinimap() {
			val g2d = minimap.graphics as Graphics2D
			g2d.color = Color.BLACK
			g2d.fillRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE)
			for (i in World.fractionList.indices) {
				if(!World.fractionList.indices.contains(i)) return
				val fraction = World.fractionList[i]
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
			val cx2 = ((-camera.x + MAP_SIZE / 2 + Window.width) / MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy1 = ((-camera.y + MAP_SIZE / 2) / MAP_SIZE * MINIMAP_SIZE).toInt()
			val cy2 = ((-camera.y + MAP_SIZE / 2 + Window.height) / MAP_SIZE * MINIMAP_SIZE).toInt()
			
			g2d.drawLine(cx1, cy1, cx2, cy1)
			g2d.drawLine(cx2, cy1, cx2, cy2)
			g2d.drawLine(cx2, cy2, cx1, cy2)
			g2d.drawLine(cx1, cy2, cx1, cy1)
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
			return intArrayOf(abs(ticks / 3600), abs(ticks / 60 % 60 / 10), abs(ticks / 60 % 10))
		}
	}
}