package core

import math.vec3

lateinit var batch: SpriteBatch

var millImage = Texture("/Mill.png")

var bgImage = Texture("/MenuBackground.jpg")

var houseImage = Texture("/House.png")
var windmillImage = Texture("/Windmill.png")
var towerImage = Texture("/Tower.png")
var woodCampImage = Texture("/WoodCamp.png")
var quarryImage = Texture("/Quarry.png")
var barracksImage = Texture("/Barracks.png")
var barricadeImage = Texture("/Barricade.png")

var gameBarImages = arrayListOf(
    houseImage,
    windmillImage,
    towerImage,
    woodCampImage,
    quarryImage,
    barracksImage,
    barricadeImage
)


var crossImage = Texture("/Cross.png")