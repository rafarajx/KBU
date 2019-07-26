package entity.nature

import entity.Entity
import fraction.Fraction
import java.awt.Graphics2D

open class Nature(owner: Fraction) : Entity(owner){

	override fun renderGL() { }

	override fun update() { }
	
	open fun gatherResources(amount: Int) { }
	
}
