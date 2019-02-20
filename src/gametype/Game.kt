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
import nature.Flowers
import nature.Grass
import nature.Nature
import nature.Rock
import nature.Tree

open class Game {

    open fun render(g2d: Graphics2D) {}

    open fun update() {}

    fun setupMenuBar() {
        for (i in MENU_BAR.indices)
            MBFields.add(Rectangle2D.Float(
                i * 16.0f * MENU_BAR_SCALE,
                Canvas.HEIGHT - 60.0f,
                16.0f * MENU_BAR_SCALE,
                16.0f * MENU_BAR_SCALE
            ))
    }

    companion object {
        var random = Random()
        val MAP_SIZE: Int
        var mouseX: Int = 0
        var mouseY: Int = 0
        val PLAYER_INDEX: Int
        var fractionList: ArrayList<Fraction>
        var player: Player = Player(1000, 100, "PLAYER", 0, Color.BLUE, Resources(100, 60, 10, 200), 0)
        var natureList = ArrayList<Nature>()
        var cameraX: Int = 0
        var cameraY: Int = 0
        val MENU_BAR: Array<IntArray>
        var MENU_BAR_SCALE: Int = 0
        var SELECTED_BUILDING: Int = 0
        val COSTS: Array<Resources>
        val RESOURCE_NAMES: Array<String>
        val youWon: String
        val youLost: String
        var MBFields = ArrayList<Rectangle2D>()
        var AF: AffineTransform? = null
        var x1: Float = 0.0f
        var y1: Float = 0.0f
        val MINIMAP_SIZE: Int
        var minimap: BufferedImage
        var tick = 1

        init {
            MAP_SIZE = 2000
            PLAYER_INDEX = 0
            fractionList = ArrayList()
            natureList = ArrayList()
            cameraY = 0
            cameraX = 0
            MENU_BAR = arrayOf(IntArray(2), intArrayOf(0, 1), intArrayOf(0, 2), intArrayOf(0, 3), intArrayOf(0, 4), intArrayOf(0, 5), intArrayOf(0, 6))
            MENU_BAR_SCALE = 3
            SELECTED_BUILDING = 0
            COSTS = arrayOf(House.COST, Mill.COST, Tower.COST, WoodCamp.COST, Quarry.COST, Barrack.COST, Barricade.COST)
            RESOURCE_NAMES = arrayOf("Wood", "Stone", "Iron", "Food")
            youWon = "You Won!"
            youLost = "You Lost!"
            MINIMAP_SIZE = 100
            minimap = BufferedImage(MINIMAP_SIZE, MINIMAP_SIZE, BufferedImage.TYPE_INT_RGB)
        }

        fun setupNature(fractions: Int) {
            for (i in 0..1000) {
                val x = random.nextInt(3000) - 500
                val y = random.nextInt(3000) - 500
                if (!isColliding(Rectangle2D.Double((x - Tree.EDGE_LENGTH / 2).toDouble(), (y - Tree.EDGE_LENGTH / 2).toDouble(), Tree.EDGE_LENGTH.toDouble(), Tree.EDGE_LENGTH.toDouble()))) {
                    natureList.add(Tree(x.toFloat(), y.toFloat()))
                }
            }
            for (i in 0..49) {
                val x = random.nextInt(3000) - 500
                val y = random.nextInt(3000) - 500
                if (!isColliding(Rectangle2D.Double((x - 8).toDouble(), (y - 8).toDouble(), 16.0, 16.0))) {
                    natureList.add(Flowers(x.toFloat(), y.toFloat()))
                }
            }
            for (i in 0..49) {
                val x = random.nextInt(3000) - 500
                val y = random.nextInt(3000) - 500
                if (!isColliding(Rectangle2D.Double((x - 8).toDouble(), (y - 8).toDouble(), 16.0, 16.0))) {
                    natureList.add(Grass(x.toFloat(), y.toFloat()))
                }
            }
            for (i1 in 0 until 4 * fractions) {
                val x1 = random.nextInt(3000) - 500
                val y1 = random.nextInt(3000) - 500
                for (i2 in 0..9) {
                    val x2 = random.nextInt(200)
                    val y2 = random.nextInt(200)
                    if (!isColliding(Rectangle2D.Double((x1 + x2 - 8).toDouble(), (y1 + y2 - 8).toDouble(), 16.0, 16.0))) {
                        natureList.add(Rock((x1 + x2).toFloat(), (y1 + y2).toFloat()))
                    }
                }
            }
        }

        fun isColliding(r: Rectangle2D): Boolean {
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
            g2d.translate(cameraX, cameraY)
            for (i in fractionList.indices)
                fractionList[i].renderBuildings(g2d)
            for (i in fractionList.indices)
                fractionList[i].renderEntities(g2d)
            for (i in natureList.indices)
                natureList[i].render(g2d)
            for (i in fractionList.indices)
                fractionList[i].drawStatus(g2d)
            g2d.transform = AF
        }

        fun drawInterface(g2d: Graphics2D) {
            createMinimap()
            g2d.drawImage(minimap, Canvas.WIDTH - MINIMAP_SIZE - 5, Canvas.HEIGHT - MINIMAP_SIZE - 5, null)

            g2d.color = Color(80, 80, 80, 180)
            g2d.fillRoundRect(0, 0, 100, 70, 10, 10)
            g2d.color = Color(255, 255, 255, 180)
            g2d.drawString("fps: " + Canvas.FPS, 5, 15)
            g2d.drawString("ups: " + Canvas.UPS, 5, 30)

            g2d.color = Color(80, 80, 80, 180)
            g2d.fillRoundRect(105, 0, 100, 70, 10, 10)
            g2d.color = Color(100, 200, 20, 180)
            g2d.drawString("Wood: " + player.resources.wood, 115, 15)
            g2d.drawString("Stone: " + player.resources.stone, 115, 30)
            g2d.drawString("Iron: " + player.resources.iron, 115, 45)
            g2d.drawString("Food: " + player.resources.food, 115, 60)

            g2d.color = Color(80, 80, 80, 180)
            g2d.fillRoundRect(210, 0, 100, 70, 10, 10)
            g2d.color = Color(100, 150, 200, 180)
            g2d.drawString("Pop: " + player.population, 220, 15)
            g2d.drawString("MaxPop: " + player.maxPopulation, 220, 30)
            g2d.drawString("Buildings: " + player.buildingList.size, 220, 45)
            g2d.drawString("Soldiers: " + player.entityList.size, 220, 60)

            g2d.color = Color(80, 80, 80, 180)
            g2d.fillRoundRect(0, Canvas.HEIGHT - 60, MENU_BAR.size * 50, 70, 10, 10)
            g2d.color = Color.BLACK
            g2d.stroke = BasicStroke(1.5f)
            g2d.drawRect(SELECTED_BUILDING * 16 * 3, Canvas.HEIGHT - 60, 48, 48)
            for (i in MENU_BAR.indices) {
                Screen.drawTile(g2d, MENU_BAR[i][0], MENU_BAR[i][1], 48 * i, Canvas.HEIGHT - 60, 48, 48)
                if (i == 1) Screen.drawTile(g2d, 7, 2, 48 * i, Canvas.HEIGHT - 60, 48, 48)
                if (player.isDefeated || !player.resources.enough(COSTS[i])) {
                    Screen.drawTile(g2d, 8, 2, 48 * i, Canvas.HEIGHT - 60, 48, 48)
                }
            }
            for (i in MENU_BAR.indices) {
                if (MBFields[i].contains(mouseX.toDouble(), mouseY.toDouble())) {
                    Screen.showInfo(g2d, mouseX.toFloat(), mouseY - 80.0f, "", RESOURCE_NAMES, COSTS[i])
                }
            }
            if (fractionList.size < 2 && !player.isDefeated) {
                val youWonScale = 4 + Math.sin(tick / 20.0f % Math.PI * 2)
                val youWonX = Canvas.WIDTH / 2 - Screen.LETTER_WIDTH * youWon.length / 2 * youWonScale
                val youWonY = Canvas.HEIGHT / 2 - Screen.LETTER_HEIGHT * youWonScale / 2
                Screen.drawString(g2d, youWon, youWonX.toInt(), youWonY.toInt(), youWonScale)
            }
            if (player.isDefeated) {
                val youLostScale = 4.0
                val youLostX = Canvas.WIDTH / 2 - Screen.LETTER_WIDTH * youLost.length / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
                val youLostY = Canvas.HEIGHT / 2 - Screen.LETTER_HEIGHT / 2 * youLostScale + (tick shr 3 and 1) * (random.nextInt(10 + 1) - 5)
                Screen.drawString(g2d, youLost, youLostX.toInt(), youLostY.toInt(), youLostScale)
            }
        }

        protected fun createMinimap() {
            val g2d = minimap.graphics as Graphics2D
            g2d.color = Color.BLACK
            g2d.fillRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE)
            for (fraction in fractionList) {
                g2d.color = fraction.color
                for (building in fraction.buildingList) {
                    val x = Math.min(Math.max(0, (building.x.toFloat() / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
                    val y = Math.min(Math.max(0, (building.y.toFloat() / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
                    g2d.fillRect(x, y, 2, 2)
                }
                for (entity in fraction.entityList) {
                    val x = Math.min(Math.max(0, (entity.x / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
                    val y = Math.min(Math.max(0, (entity.y / MAP_SIZE * MINIMAP_SIZE).toInt()), MAP_SIZE)
                    g2d.fillRect(x, y, 1, 1)
                }
            }
            g2d.color = Color.WHITE
            g2d.drawLine(0, 0, MINIMAP_SIZE - 1, 0)
            g2d.drawLine(MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1, MINIMAP_SIZE - 1)
            g2d.drawLine(MINIMAP_SIZE - 1, MINIMAP_SIZE - 1, 0, MINIMAP_SIZE - 1)
            g2d.drawLine(0, MINIMAP_SIZE - 1, 0, 0)

            val cx1 = ((-Game.cameraX).toDouble() / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
            val cx2 = ((-Game.cameraX + Canvas.WIDTH).toDouble() / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
            val cy1 = ((-Game.cameraY).toDouble() / Game.MAP_SIZE * MINIMAP_SIZE).toInt()
            val cy2 = ((-Game.cameraY + Canvas.HEIGHT).toDouble() / Game.MAP_SIZE * MINIMAP_SIZE).toInt()

            g2d.drawLine(cx1, cy1, cx2, cy1)
            g2d.drawLine(cx2, cy1, cx2, cy2)
            g2d.drawLine(cx2, cy2, cx1, cy2)
            g2d.drawLine(cx1, cy2, cx1, cy1)
        }

        fun updateObjects() {
            for (i in fractionList.indices) {
                if(fractionList.indices.contains(i))
                    fractionList[i].update()
            }
            for (i in natureList.indices) {
                if(natureList.indices.contains(i))
                    natureList[i].update()
            }
        }

        fun updateInput() {
            if (Input.mousePressed) {
                if (Input.mouseButton == 1) {
                    player.placeBuilding(SELECTED_BUILDING, Input.mouseX, Input.mouseY)
                }
            }
            if (Input.mouseMoved) {
                mouseX = Input.mouseX
                mouseY = Input.mouseY
                x1 = Input.mouseX.toFloat()
                y1 = Input.mouseY.toFloat()
            }
            if (Input.mouseButton == 3 || Input.mouseButton == 2) {
                val x2 = Input.mouseX.toDouble()
                val y2 = Input.mouseY.toDouble()
                val XD = x2 - x1
                val YD = y2 - y1
                x1 = Input.mouseX.toFloat()
                y1 = Input.mouseY.toFloat()
                cameraX += XD.toInt()
                cameraY += YD.toInt()
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
