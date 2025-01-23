mods.selectionguicrafting.sgc_category.newCategory()
        .id('hi')
        .displayName('SelectionGUI Crafting')
        .register()

for (i in 0..15) {
    mods.selectionguicrafting.sgc_recipe.newRecipe()
            .category('hi')
            .input(item('minecraft:wool', i) * 3)
            .output(item('minecraft:dye', i) * 2)
            .tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f)
            .tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f)
            .tool(item('minecraft:diamond_pickaxe'), 0.1f, 5.0f)
            .catalyst(item('minecraft:stained_glass', i) * 2, 0.9f)
            .time(40)
            .durability(10)
            .register()
}
