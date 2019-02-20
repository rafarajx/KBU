package core

data class Resources(var wood: Int, var stone: Int, var iron: Int, var food: Int) {

    constructor(value: Int) : this(value, value, value, value) {}

    fun pay(cost: Resources) {
        wood -= cost.wood
        stone -= cost.stone
        iron -= cost.iron
        food -= cost.food
    }

    fun pay(wood: Int, stone: Int, iron: Int, food: Int) {
        this.wood -= wood
        this.stone -= stone
        this.iron -= iron
        this.food -= food
    }

    fun gain(cost: Resources) {
        wood += cost.wood
        stone += cost.stone
        iron += cost.iron
        food += cost.food
    }

    fun gain(wood: Int, stone: Int, iron: Int, food: Int) {
        this.wood += wood
        this.stone += stone
        this.iron += iron
        this.food += food
    }

    fun enough(cost: Resources): Boolean {
        return wood >= cost.wood && stone >= cost.stone && iron >= cost.iron && food >= cost.food
    }

    fun enough(wood: Int, stone: Int, iron: Int, food: Int): Boolean {
        return this.wood >= wood && this.stone >= stone && this.iron >= iron && this.food >= food
    }
}
