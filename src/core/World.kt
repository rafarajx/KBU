package core

import entity.Entity
import entity.Knight
import entity.building.*
import entity.nature.*
import entity.worker.Miller
import entity.worker.Stonemason
import entity.worker.Woodcutter
import fraction.Fraction
import gametype.Game
import math.AABB
import math.vec2
import java.util.ArrayList

object World {
	
	var chunks = ArrayList<Chunk>()
	
	
	//      NATURE
	var natureList = ArrayList<Nature>()
	var cloudList = ArrayList<Cloud>()
	var flowersList = ArrayList<Flowers>()
	var grassList = ArrayList<Grass>()
	var rockList = ArrayList<Rock>()
	var treeList = ArrayList<Tree>()
	var wheatList = ArrayList<Wheat>()
	
	//      BUILDINGS
	var buildingList = ArrayList<Building>()
	
	var houseList = ArrayList<House>()
	var millList = ArrayList<Windmill>()
	var towerList = ArrayList<Tower>()
	var woodCampList = ArrayList<WoodCamp>()
	var quarryList = ArrayList<Quarry>()
	var barrackList = ArrayList<Barrack>()
	var barricadeList = ArrayList<Barricade>()
	
	//      ENTITIES
	var entityList = ArrayList<Entity>()
	var knightList = ArrayList<Knight>()
	
	var nature: Fraction = Fraction()
	
	var fractionList = ArrayList<Fraction>()
	
	fun isColliding(r: AABB): Boolean {
		for (j in entityList.indices) {
			val e = entityList[j]
			if (e.field!!.intersects(r)) return true
		}
		return false
	}
	
	fun isConstructionColliding(r: AABB): Boolean {
		for (j in buildingList.indices) {
			val building = buildingList[j]
			if (building.field!!.intersects(r)) return true
		}
		return false
	}
	
	fun renderGL() {
		
		for(i in chunks.indices){
			chunks[i].renderGL()
		}
		//println(chunks.size)
	}
	
	
	fun update() {
		for(i in entityList.indices){
			if(!entityList.indices.contains(i)) continue
			entityList[i].update()
		}
		for(i in chunks.indices){
			chunks[i].update()
		}
		for(i in fractionList.indices){
			fractionList[i].update()
		}
	}
	
	var counter = 0
	
	fun bla(e: Entity): Boolean{
		for(i in chunks.indices){
			val c = chunks[i]
			
			
			if(c.field.contains(e.p)){
				counter++
				c.add(e)
				
				return false
			}
		}
		return true
	}
	
	@Synchronized
	fun add(e: Entity){
	
		when(e){
			is Tree -> {
				entityList.add(e)
				natureList.add(e)
				treeList.add(e)
			}
			is Rock -> {
				entityList.add(e)
				natureList.add(e)
				rockList.add(e)
			}
			is Cloud -> {
				entityList.add(e)
				natureList.add(e)
				cloudList.add(e)
			}
			is Wheat -> {
				entityList.add(e)
				natureList.add(e)
				wheatList.add(e)
			}
			is Grass -> {
				entityList.add(e)
				natureList.add(e)
				grassList.add(e)
			}
			is Flowers -> {
				entityList.add(e)
				natureList.add(e)
				flowersList.add(e)
			}
			
			is Barrack -> {
				entityList.add(e)
				buildingList.add(e)
				barrackList.add(e)
			}
			is Barricade -> {
				entityList.add(e)
				buildingList.add(e)
				barricadeList.add(e)
			}
			is House -> {
				entityList.add(e)
				buildingList.add(e)
				houseList.add(e)
			}
			is Quarry -> {
				entityList.add(e)
				buildingList.add(e)
				quarryList.add(e)
			}
			is Tower -> {
				entityList.add(e)
				buildingList.add(e)
				towerList.add(e)
			}
			is Windmill -> {
				entityList.add(e)
				buildingList.add(e)
				millList.add(e)
			}
			is WoodCamp -> {
				entityList.add(e)
				buildingList.add(e)
				woodCampList.add(e)
			}
			
			is Miller -> {
				entityList.add(e)
			}
			is Stonemason -> {
				entityList.add(e)
			}
			is Woodcutter -> {
				entityList.add(e)
			}
			is Knight -> {
				entityList.add(e)
			}
		}
		
		if(bla(e)){
			var x = e.p.x.toInt()
			var y = e.p.y.toInt()
			if(x < 0){
				x -= 1024
			}
			if(y < 0){
				y -= 1024
			}
			val p = vec2((x / 1024) * 1024, (y / 1024) * 1024)
			//println(p)
			val chunk = Chunk(p)
			chunk.add(e)
			chunks.add(chunk)
		}
		
		
	}
	
	@Synchronized
	fun remove(e: Entity){
		when(e){
			is Tree -> {
				entityList.remove(e)
				natureList.remove(e)
				treeList.remove(e)
			}
			is Rock -> {
				entityList.remove(e)
				natureList.remove(e)
				rockList.remove(e)
			}
			is Cloud -> {
				entityList.remove(e)
				natureList.remove(e)
				cloudList.remove(e)
			}
			is Wheat -> {
				entityList.remove(e)
				natureList.remove(e)
				wheatList.remove(e)
			}
			is Grass -> {
				entityList.remove(e)
				natureList.remove(e)
				grassList.remove(e)
			}
			is Flowers -> {
				entityList.remove(e)
				natureList.remove(e)
				flowersList.remove(e)
			}
			
			is Barrack -> {
				entityList.remove(e)
				buildingList.remove(e)
				barrackList.remove(e)
			}
			is Barricade -> {
				entityList.remove(e)
				buildingList.remove(e)
				barricadeList.remove(e)
			}
			is House -> {
				entityList.remove(e)
				buildingList.remove(e)
				houseList.remove(e)
			}
			is Quarry -> {
				entityList.remove(e)
				buildingList.remove(e)
				quarryList.remove(e)
			}
			is Tower -> {
				entityList.remove(e)
				buildingList.remove(e)
				towerList.remove(e)
			}
			is Windmill -> {
				entityList.remove(e)
				buildingList.remove(e)
				millList.remove(e)
			}
			is WoodCamp -> {
				entityList.remove(e)
				buildingList.remove(e)
				woodCampList.remove(e)
			}
			
			is Miller -> {
				entityList.remove(e)
			}
			is Stonemason -> {
				entityList.remove(e)
			}
			is Woodcutter -> {
				entityList.remove(e)
			}
			is Knight -> {
				entityList.remove(e)
			}
		}
		
		for(i in chunks.indices){
			val c = chunks[i]
			if(c.field.contains(e.p)){
				c.remove(e)
			}
		}
	}
	
	
	fun setupNature(fractions: Int) {
		
		for(i in 0..30){
			Tree(nature, vec2(i * 5, i * 5)).add()
		}
		
		for (i in 0..5000) {
			val p = vec2(Utils.nextFloat(),  Utils.nextFloat()) * 6000.0f - 3000.0f
			Tree(nature, p).add()
		}
		for (i in 0..100) {
			val p = vec2(Utils.nextFloat(),  Utils.nextFloat()) * 6000.0f - 3000.0f
			if (!isColliding(AABB(p, 8.0f))) Flowers(nature, p).add()
		}
		for (i in 0..100) {
			val p = vec2(Utils.nextFloat(),  Utils.nextFloat()) * 6000.0f - 3000.0f
			if (!isColliding(AABB(p, 8.0f))) Grass(nature, p).add()
		}
		for (i1 in 0 until 4 * fractions) {
			val p = vec2(Utils.nextFloat(),  Utils.nextFloat()) * 4000.0f - 2000.0f
			for (i2 in 0..9) {
				val p2 = vec2(Utils.nextFloat(),  Utils.nextFloat()) * 200.0f
				if (!isColliding(AABB(p + p2, 8.0f))) Rock(nature, p + p2).add()
			}
		}
	}
}