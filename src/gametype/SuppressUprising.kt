package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import fraction.Player
import fraction.StandardAI
import fraction.Uprising
import math.vec2

object SuppressUprising : Game() {
	
	var difficulty = 0
	var friendsNumber = 0
	
	override fun onSet() {
        difficulty = 6 - difficulty
		setupMenuBar()
		Game.natureList.clear()
		Game.fractionList.clear()
		Game.setupNature(friendsNumber + 1)
		var start = vec2(Game.random.nextInt(2000), Game.random.nextInt(2000))
		Game.player = Player(start, Color.BLUE, Resources(100, 60, 8, 30), 0)
		Game.fractionList.add(Game.player)
		for (i in 0 until friendsNumber) {
			val red = Game.random.nextInt(255)
			val green = Game.random.nextInt(255)
			val blue = Game.random.nextInt(255)
			val c = Color(red, green, blue)
			start = vec2(Game.random.nextInt(2000), Game.random.nextInt(2000))
			Game.fractionList.add(
				StandardAI(
					start, c,
					Resources(50 * difficulty, 30 * difficulty, 5 * difficulty, 50 * difficulty),
					difficulty * 50, 0
                )
			)
		}
		start = vec2(1000.0f, 1000.0f)
		Game.fractionList.add(Uprising(start, Color.RED, 100, 1))
		
		setBasicInput()
	}

	override fun render(g2d: Graphics2D) {
		Game.drawObjects(g2d)
		Game.drawInterface(g2d)
	}

	override fun update() {
		Game.updateObjects()
		Game.tick++
	}
}
