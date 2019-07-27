package fraction

import core.Canvas
import core.Resources
import core.Window
import entity.building.*
import gametype.Game
import math.AABB
import math.vec2
import sound.SimpleSound
import java.awt.Color
import java.awt.Graphics2D

class Player(start: vec2, c: Color, resources: Resources, teamNumber: Int) : Fraction() {
	
	init {
		this.center = start.copy()
		this.color = c
		this.resources = resources
		this.teamNumber = teamNumber
		Game.camera.x = -start.x + Window.width / 2
		Game.camera.y = -start.y + Window.height / 2
		Tower(start, this, teamNumber).add()
	}
	
	override fun update() {
		if (tick % 100 == 0 && buildingList.size == 0 && entityList.size == 0) isDefeated = true
		for (i in entityList.indices) {
			if(entityList.indices.contains(i))
				entityList[i].update()
		}
		if (isDefeated) return
		for (i in buildingList.indices) {
			if(buildingList.indices.contains(i))
				buildingList[i].update()
		}
		tick++
	}
	
	override fun placeBuilding(id: Int, p: vec2) {
		
		val area = if (id == 6) AABB(p, 8) else AABB(p, 16)
		
		//if (!inRange(buildingList, area)) return
		if (isConstructionColliding(area)) return
		when (id) {
			0 -> if (resources.enough(House.COST)) {
				resources.pay(House.COST)
				SimpleSound.explosion.play()
				House(p, this, teamNumber).add()
			}
			1 -> if (resources.enough(Windmill.COST)) {
				resources.pay(Windmill.COST)
				SimpleSound.explosion.play()
				Windmill(p, this, teamNumber).add()
			}
			2 -> if (resources.enough(Tower.COST)) {
				resources.pay(Tower.COST)
				SimpleSound.explosion.play()
				Tower(p, this, teamNumber).add()
			}
			3 -> if (resources.enough(WoodCamp.COST)) {
				resources.pay(WoodCamp.COST)
				SimpleSound.explosion.play()
				WoodCamp(p, this, teamNumber).add()
			}
			4 -> if (resources.enough(Quarry.COST)) {
				resources.pay(Quarry.COST)
				SimpleSound.explosion.play()
				Quarry(p, this, teamNumber).add()
			}
			5 -> if (resources.enough(Barrack.COST)) {
				resources.pay(Barrack.COST)
				SimpleSound.explosion.play()
				Barrack(p, this, teamNumber).add()
			}
			6 -> if (resources.enough(Barricade.COST)) {
				resources.pay(Barricade.COST)
				SimpleSound.explosion.play()
				Barricade(p, this, teamNumber).add()
			}
		}
	}
}
