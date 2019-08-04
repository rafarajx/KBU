package gametype

import java.awt.Color
import java.awt.Graphics2D

import core.Resources
import core.Screen
import core.World
import fraction.Player
import fraction.StandardAI
import fraction.ZombieAI
import math.vec2
import sound.SimpleSound

object ZombieApocalypse : Game() {

	private var showTime = true

	fun init(FriendsNumber: Int, difficulty: Int) {
		var difficulty = difficulty
		setupMenuBar()
		World.natureList.clear()
		World.fractionList.clear()
		World.setupNature(FriendsNumber + 1)
		var start = vec2(1000.0f, 1000.0f)
		World.fractionList.add(Player(start, Color.BLUE, Resources(100, 60, 8, 30), 0))
		difficulty++
		for (i in 0 until FriendsNumber) {
			val red = random.nextInt(255)
			val green = random.nextInt(255)
			val blue = random.nextInt(255)
			val c = Color(red, green, blue)
			start = vec2(random.nextInt(2000), random.nextInt(2000))
			World.fractionList.add(
				StandardAI(
					start, c,
					Resources(200 / difficulty, 120 / difficulty, 20 / difficulty, 60 / difficulty),
					(5 - difficulty) * 50, 0
				)
			)
		}
		World.fractionList.add(ZombieAI(1000, 1000, Color.RED, 1))
	}

	override fun update() {
		World.update()
		tick++
	}
}
