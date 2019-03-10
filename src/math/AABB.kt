package math

class AABB(var ul: vec2, var dr: vec2){
	
	var width = dr.x - ul.x
	var height = dr.y - ul.y
	
	var mid = (ul + dr) * 0.5f
	
}