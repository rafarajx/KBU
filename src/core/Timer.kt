package core

import gametype.Game
import org.lwjgl.glfw.GLFW.*
import java.util.concurrent.CopyOnWriteArrayList

object Timer {
	
	val everys = CopyOnWriteArrayList<TimeEvery>()
	var deltaTime = 0f
	
	fun addEvery(time: Float, cb: () -> Unit) {
		everys.add(TimeEvery(time, cb))
	}
	
	fun removeEvery(cb: () -> Unit){
		val it = everys.iterator()
		while(it.hasNext()){
			val e = it.next()
			if(e.cb == cb){
				everys.remove(e)
			}
		}
	}
	
	fun update(){
		deltaTime = 1.0f / FPS
		
		val time = glfwGetTime()
		
		val it = everys.iterator()
		while(it.hasNext()){
			val e = it.next()
			if(time - e.starttime > e.time){
				e.starttime = time
				e.cb.invoke()
			}
		}
	}
	
	
}