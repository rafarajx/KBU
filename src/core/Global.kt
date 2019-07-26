package core

import math.vec3

class G {
    companion object{
        lateinit var batch: SpriteBatch
       
        var millImage = Texture("./res/Mill.png")
    
        var bgImage = Texture("./res/MenuBackground.png")
        
        var houseImage = Texture("./res/House.png")
        var windmillImage = Texture("./res/Windmill.png")
        var towerImage = Texture("./res/Tower.png")
        var woodCampImage = Texture("./res/WoodCamp.png")
        var quarryImage = Texture("./res/Quarry.png")
        var barracksImage = Texture("./res/Barracks.png")
        var barricadeImage = Texture("./res/Barricade.png")
        
        var gameBarImages = arrayListOf(
            houseImage,
            windmillImage,
            towerImage,
            woodCampImage,
            quarryImage,
            barracksImage,
            barricadeImage
        )
        
        
        var crossImage = Texture("./res/Cross.png")
    }

}