package core

import entity.Entity
import entity.Knight
import entity.building.*
import entity.nature.*
import entity.worker.Miller
import entity.worker.Stonemason
import entity.worker.Woodcutter
import gametype.Game
import math.AABB
import math.vec2
import java.util.ArrayList

class Chunk(var pos: vec2) {
	
	companion object {
		val EDGE_LENGTH = 1024
		val HALF_EDGE = EDGE_LENGTH / 2
		val size = vec2(EDGE_LENGTH)
	}
	
	
	var natureList = ArrayList<Nature>()
	var cloudList = ArrayList<Cloud>()
	var flowersList = ArrayList<Flowers>()
	var grassList = ArrayList<Grass>()
	var rockList = ArrayList<Rock>()
	var treeList = ArrayList<Tree>()
	var wheatList = ArrayList<Wheat>()
	
	var buildingList = ArrayList<Building>()
	
	var houseList = ArrayList<House>()
	var millList = ArrayList<Windmill>()
	var towerList = ArrayList<Tower>()
	var woodCampList = ArrayList<WoodCamp>()
	var quarryList = ArrayList<Quarry>()
	var barrackList = ArrayList<Barrack>()
	var barricadeList = ArrayList<Barricade>()
	
	var entityList = ArrayList<Entity>()
	
	var knightList = ArrayList<Knight>()
	
	
	var field: AABB
	var updatable = false
	var renderable = false
	var prenderable = false
	
	init{
		field = AABB(pos, pos + size)
		
	}
	
	fun setContentRenderable(bool: Boolean){
		for(i in entityList.indices){
			if(!entityList.indices.contains(i)) continue
			val e = entityList[i]
			e.renderable = bool
			e.renderGL()
		}
	}
	
	fun enableRenderCallback(){
		//println("yes")
		setContentRenderable(true)
	}
	
	fun disableRenderCallback(){
		//println("no")
		setContentRenderable(false)
	}
	
	fun renderGL(){
		val screen = AABB(vec2(0, 0) - Game.camera, Window.size - Game.camera)
		
		renderable = field.intersects(screen)
		
		if(renderable) {
			synchronized(entityList) {
				for (i in entityList.indices) {
					if (!entityList.indices.contains(i)) continue
					val e = entityList[i]
					e.renderGL()
				}
			}
		}
		
		if(prenderable && !renderable){
			disableRenderCallback()
		}
		
		if(!prenderable && renderable){
			enableRenderCallback()
		}
		
		
		prenderable = field.intersects(screen)
		
		if(Game.drawchunks) {
			RectRenderer.enableCamera()
			if (renderable) {
				RectRenderer.setColor(1.0f, 0.0f, 0.0f)
			} else {
				RectRenderer.setColor(0.0f)
			}
			RectRenderer.draw(field, 2.0f)
		}
		
		
	}
	
	fun update(){
	
		for(i in entityList.indices){
			if(!entityList.indices.contains(i)) continue
			val e = entityList[i]
			if(!e.static){
				if(!field.contains(e.p)){
					World.remove(e)
					World.add(e)
				}
			}
		}
	
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
	}


}