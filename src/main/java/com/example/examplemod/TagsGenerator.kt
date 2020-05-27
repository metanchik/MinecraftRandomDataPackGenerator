package com.example.examplemod

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.io.File
import java.util.*

object TagsGenerator {
    @JvmStatic
    fun main(args: Array<String>) {
        var folder = File("D:/Work/Forge/src/main/resources/items/")
        var itemsTags = mutableListOf<String>()
        folder.walk().forEach { ff ->
            if (ff.isFile) {
                var txt = ff.readText()
                val result = Klaxon()
                        .parse<ItemsTags>(txt)
//                println(txt)
                result?.values?.forEach {
                    if (!it.contains("#"))
                        itemsTags.add(it)
                }
            }
        }
        println(itemsTags.count())

        val tagsJsons = mutableListOf<String>()
        val checkSizeBuffer: StringBuffer = StringBuffer()

        println("loops: 003")
        while (checkSizeBuffer.length < 24000000) {

            val maxInt = itemsTags.count() - 1
            val items = mutableListOf<String>()
            while (items.count() < 50) {
                val i = Random().nextInt(maxInt)
                items.add(itemsTags[i])
            }
            val tagObj = ItemsTags(false, items)

            val tagJsonString = Klaxon().toJsonString(tagObj)
            val tagJsonObj = Parser.default().parse(StringBuilder(tagJsonString)) as JsonObject
            val tagJsonPrettyPrintString = tagJsonObj!!.toJsonString(true)
            tagsJsons.add(tagJsonPrettyPrintString)
            checkSizeBuffer.append(tagJsonPrettyPrintString)

        }

        println("Final size of buffer: " + checkSizeBuffer.length + "/2400000")
        var outFolderName = "D:/Work/Forge/src/main/resources/outTags/"
        tagsJsons.forEach { recipeJson ->
            var i = UUID.randomUUID().toString().replace("-", "_")
            var recipeFile = File(outFolderName + i + ".json")
            recipeFile.createNewFile()
            recipeFile.writeText(recipeJson)
        }
        println("Writed " + tagsJsons.count() + " files")
    }

}


