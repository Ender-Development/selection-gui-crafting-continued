
// Auto generated groovyscript example file
// MODS_LOADED: selectionguicrafting

log.info 'mod \'selectionguicrafting\' detected, running script'

// groovyscript.wiki.selectionguicrafting.sgc_category.title:
// groovyscript.wiki.selectionguicrafting.sgc_category.description.

// mods.selectionguicrafting.sgc_category.removeByName('dummy_category')
// mods.selectionguicrafting.sgc_category.removeAll()

mods.selectionguicrafting.sgc_category.createCategory()
    .id('dummy_category')
    .displayName('Dummy Category')
    .background('selectionguicrafting:textures/gui/gui_wood.png')
    .register()


// groovyscript.wiki.selectionguicrafting.sgc_recipe.title:
// groovyscript.wiki.selectionguicrafting.sgc_recipe.description.

// mods.selectionguicrafting.sgc_recipe.removeByCategory('dummy_category')
mods.selectionguicrafting.sgc_recipe.removeByInput(item('minecraft:cobblestone'))
mods.selectionguicrafting.sgc_recipe.removeByOutput(item('minecraft:stone'))
mods.selectionguicrafting.sgc_recipe.removeByTool(item('minecraft:wool'))
// mods.selectionguicrafting.sgc_recipe.removeAll()

mods.selectionguicrafting.sgc_recipe.createRecipe()
    .category('dummy_category')
    .input(item('minecraft:stone') * 3)
    .output(item('minecraft:cobblestone') * 2, 0.5f)
    .tool(item('minecraft:wooden_pickaxe'), 1.0f)
    .time(200)
    .xp(1)
    .sound('minecraft:block.anvil.land')
    .register()


