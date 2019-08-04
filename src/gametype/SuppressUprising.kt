package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import core.World
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
		World.natureList.clear()
		World.fractionList.clear()
		World.setupNature(friendsNumber + 1)
		
		var start = vec2(random.nextInt(2000), random.nextInt(2000))
		
		player = Player(start, Color.BLUE, Resources(100, 60, 8, 30), 0)
		World.fractionList.add(player)
		
		for (i in 0 until friendsNumber) {
			val red = random.nextInt(255)
			val green = random.nextInt(255)
			val blue = random.nextInt(255)
			val c = Color(red, green, blue)
			start = vec2(random.nextInt(2000), random.nextInt(2000))
			World.fractionList.add(
				StandardAI(
					start, c,
					Resources(50 * difficulty, 30 * difficulty, 5 * difficulty, 50 * difficulty),
					difficulty * 50, 0
                )
			)
		}
		start = vec2(1000.0f, 1000.0f)
		World.fractionList.add(Uprising(start, Color.RED, 100, 1))
		
	}

	override fun update() {
		World.update()
		tick++
	}
}
