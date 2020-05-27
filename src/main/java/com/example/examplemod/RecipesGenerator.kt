package com.example.examplemod

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.io.File
import java.util.*

object RecipesGenerator {
    @JvmStatic
    fun main(args: Array<String>) {
        var folder = File("D:/Work/Forge/src/main/resources/items/")
        var tags = mutableListOf<String>()
        folder.walk().forEach { ff ->
            if (ff.isFile) {
                var txt = ff.readText()
                val result = Klaxon()
                        .parse<ItemsTags>(txt)
//                println(txt)
                result?.values?.forEach {
                    if (!it.contains("#"))
                        tags.add(it)
                }
            }
        }
        println(tags.count())

        val recipesJsons = mutableListOf<String>()
        val checkSizeBuffer: StringBuffer = StringBuffer()

        println("loops: 2-2")
        for (idx in 1..22) {
            tags.forEach { itemTag ->
                if (checkSizeBuffer.length >= 24000000) {
                    return@forEach
                }

                val result = itemTag
                val resIdx = tags.indexOf(result)
                val recipeItemsIdxs = mutableListOf<Int>()
                val maxInt = tags.count() - 1
                while (recipeItemsIdxs.count() < 9) {
                    val i = Random().nextInt(maxInt)
                    if (i != resIdx) {
                        recipeItemsIdxs.add(i)
                    }
                }
//            println(recipeItemsIdxs)
                val itemKey = ItemKey(tags[recipeItemsIdxs[0]], tags[recipeItemsIdxs[1]], tags[recipeItemsIdxs[2]], tags[recipeItemsIdxs[3]],
                        tags[recipeItemsIdxs[4]], tags[recipeItemsIdxs[5]], tags[recipeItemsIdxs[6]], tags[recipeItemsIdxs[7]], tags[recipeItemsIdxs[8]])

                val mainRecipe = Recipe(itemKey, itemTag)
                val currentRecipes = createOtherRecipesFromCurrent(mainRecipe)
                currentRecipes.add(mainRecipe)
//            recipes.addAll(currentRecipes)

                currentRecipes.forEach { recipe ->
                    val recipeJsonString = Klaxon().toJsonString(recipe)
                    val recipeJsonObj = Parser.default().parse(StringBuilder(recipeJsonString)) as JsonObject
                    val recipeJsonPrettyPrintString = recipeJsonObj!!.toJsonString(true)
                    recipesJsons.add(recipeJsonPrettyPrintString)
                    checkSizeBuffer.append(recipeJsonPrettyPrintString)
                }
            }
        }
        println("Final size of buffer: " + checkSizeBuffer.length + "/2400000")
        var outFolderName = "D:/Work/Forge/src/main/resources/outRecipes/"
        var i = 0
        recipesJsons.forEach { recipeJson ->
            var recipeFile = File(outFolderName + i + ".json")
            recipeFile.createNewFile()
            recipeFile.writeText(recipeJson)
            i++
        }
        println("Writed " + i + " files")
    }

    fun createOtherRecipesFromCurrent(recipe: Recipe): MutableList<Recipe> {
        val patterns = mutableListOf<MutableList<String>>(
//                mutableListOf("abc", "def", "ghi"),
                mutableListOf("abc", "ghi", "def"),
                mutableListOf("def", "abc", "ghi"),
                mutableListOf("def", "ghi", "abc"),
                mutableListOf("ghi", "def", "abc"),
                mutableListOf("ghi", "abc", "def"),
        )
        val res = mutableListOf<Recipe>()

        patterns.forEach { pt ->
            var newRecipe = recipe.clone()
            newRecipe.pattern = pt
            res.add(newRecipe)
        }

        return res
    }
}


