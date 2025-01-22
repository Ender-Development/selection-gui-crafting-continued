import io.enderdev.selectionguicrafting.gui.Assets;

for (i in 0..20) {
    mods.selectionguicrafting.sgc_recipe.newRecipe()
            .category('dead')
            .input(item('minecraft:wheat_seeds') * 3)
            .output(item('minecraft:sand') * 2)
            .tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f)
            .tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f)
            .catalyst(item('minecraft:apple') * 2, 0.9f)
            .frame(Assets.FRAME_IRON.path())
            .time(40)
            .durability(1)
            .queueType(false)
            .outputType('DROP')
            .xp(1)
            .sound('minecraft:block.anvil.land', 1.0f, 1.0f)
            .register()
}

mods.selectionguicrafting.sgc_category.newCategory()
        .id('dead')
        .displayName('This is another dummy category to test')
        .background(Assets.BG_DEADLANDS.path())
        .decoration(Assets.DECOR_GOLD.path())
        .border(Assets.BG_WOOD.path())
        .backgroundType('SINGLE_STRETCH')
        .register()