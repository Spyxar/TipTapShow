plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "26.2"

stonecutter parameters {
    replacements.string(current.parsed < "26.1", "component_text") {
        replace("net.minecraft.network.chat.Component", "net.minecraft.text.Text")
        replace("Component", "Text")
    }
    replacements.string(current.parsed < "26.1", "minecraft_minecraftclient") {
        replace("net.minecraft.client.Minecraft", "net.minecraft.client.MinecraftClient")
        replace("Minecraft", "MinecraftClient")
    }
    replacements.string(current.parsed < "26.1", "identifier") {
        replace("net.minecraft.resources.Identifier", "net.minecraft.util.Identifier")
    }
}