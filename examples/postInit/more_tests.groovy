mods.selectionguicrafting.sgc_recipe.createRecipe()
        .category('dummy_category')
        .input(item('minecraft:stone') * 32)
        .output(item('minecraft:diamond') * 50, 0.5f)
        .output(item('minecraft:clay') * 2, 0.1f)
        .tool(item('minecraft:wooden_pickaxe'), 1.0f)
        .tool(item('minecraft:diamond_pickaxe'), 10.0f, 10.0f)
        .durability(10)
        .time(200)
        .xp(1)
        .sound('minecraft:block.anvil.land')
        .register()

mods.selectionguicrafting.sgc_category.createCategory()
        .id('blub')
        .displayName('Pick your recipe')
        .background('selectionguicrafting:textures/gui/gui_full.png')
        .setBackgroundType('SINGLE_STRETCH')
        .register()

mods.selectionguicrafting.sgc_recipe.createRecipe()
        .category('blub')
        .input(item('minecraft:diamond'))
        .output(item('minecraft:wheat_seeds') * 5, 0.5f)
        .tool(item('minecraft:grass') * 5, 1.0f)
        .register()

for (i in 0..15) {
mods.selectionguicrafting.sgc_recipe.createRecipe()
        .category('blub')
        .input(item('minecraft:diamond'))
        .output(item('minecraft:dye', i) * 5, 0.5f)
        .tool(item('minecraft:stone_pickaxe'), 1.0f)
        .register()

    mods.selectionguicrafting.sgc_recipe.createRecipe()
        .category('blub')
        .input(item('minecraft:apple'))
        .output(item('minecraft:dye', i) * 5, 0.5f)
        .tool(item('minecraft:stone_pickaxe'), 1.0f)
        .register()
}

for (i in 0..15) {
    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:wool', i))
            .output(item('minecraft:dye', i), 0.5f)
            .tool(item('minecraft:wooden_pickaxe'), 1.0f)
            .register()

    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:dye', i))
            .output(item('minecraft:stained_glass', i), 0.5f)
            .tool(item('minecraft:wooden_pickaxe'), 1.0f)
            .register()

    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:stained_glass', i))
            .output(item('minecraft:stained_hardened_clay', i), 0.5f)
            .tool(item('minecraft:wooden_pickaxe'), 1.0f)
            .register()


    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:stained_hardened_clay', i))
            .output(item('minecraft:concrete', i), 0.5f)
            .tool(item('minecraft:stone_pickaxe'), 1.0f)
            .register()
    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:concrete', i))
            .output(item('minecraft:concrete_powder', i), 0.5f)
            .tool(item('minecraft:stone_pickaxe'), 1.0f)
            .register()

    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:concrete_powder', i))
            .output(item('minecraft:carpet', i), 0.5f)
            .tool(item('minecraft:stone_pickaxe'), 1.0f)
            .register()

    mods.selectionguicrafting.sgc_recipe.createRecipe()
            .category('dummy_category')
            .input(item('minecraft:carpet', i))
            .output(item('minecraft:stained_glass_pane', i), 0.5f)
            .tool(item('minecraft:stone_pickaxe'), 1.0f)
            .register()


}
