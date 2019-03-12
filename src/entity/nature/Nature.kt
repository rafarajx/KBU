package entity.nature

import entity.Entity
import java.awt.Graphics2D

open class Nature : Entity(){
	
	override fun render(g2d: Graphics2D) { }
	
	override fun update() { }
	
	open fun gatherResources(amount: Int) { }
	
}
