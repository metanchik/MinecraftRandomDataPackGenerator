package com.example.examplemod

class Recipe {
    var type: String = "minecraft:crafting_shaped"
    var pattern: MutableList<String> = mutableListOf("abc", "def", "ghi")
    var key: ItemKey
    var result: ItemResult

    constructor(items: ItemKey, res: String) {
        key = items
        result = ItemResult(res)
    }

    public fun clone(): Recipe {
        val clone = Recipe(key, result.item)
        clone.type = type
        clone.pattern = pattern
        clone.result.count = result.count
        return clone
    }
}