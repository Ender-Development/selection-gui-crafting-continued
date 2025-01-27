
// Auto generated groovyscript example file
// MODS_LOADED: selectionguicrafting

log.info 'mod \'selectionguicrafting\' detected, running script'

// Crafting Category:
// All recipes in the mod are divided into categories. Each category has its own set of recipes. Each category can have its
// own texture for the background, border, frame, decoration, and progress bar. The category can also have its own sounds,
// particles, can specify how the sounds are played, if recipes in the category can be added to the crafting queue, and how
// the output items are handed to the player.

mods.selectionguicrafting.category.removeByName('dummy_category_1')
// mods.selectionguicrafting.category.removeAll()

mods.selectionguicrafting.category.categoryBuilder()
    .id('dummy_category')
    .displayName('Your first Category')
    .background('selectionguicrafting:textures/gui/background/wood.png')
    .register()

mods.selectionguicrafting.category.categoryBuilder()
    .id('blub')
    .displayName('Pick your recipe')
    .background('selectionguicrafting:textures/gui/background/lake.png')
    .backgroundType('SINGLE_CUT')
    .register()

mods.selectionguicrafting.category.categoryBuilder()
    .id('dead')
    .displayName('This is another dummy category to test')
    .background('selectionguicrafting:textures/gui/background/deadlands.png')
    .decoration('selectionguicrafting:textures/gui/decor/gold.png')
    .border('selectionguicrafting:textures/gui/background/wood.png')
    .backgroundType('SINGLE_CUT')
    .register()


// Crafting Recipe:
// Creates a recipe that is shown in the specified category. Each recipe requires at least an input (placed in the
// offhand), tool (placed in the mainhand), and output. There can also be an optional catalyst (placed in the inventory).
// The recipe can have its own frame, sounds, particles, progress bar, can specify how the sounds are played, if the recipe
// can be added to the crafting queue, how the output items are handed to the player, how much durability is consumed if
// the tool is a damageable item, the crafting time, and how much XP is rewarded. Most of these have a fallback to the
// category settings.

// mods.selectionguicrafting.recipe.removeByCategory('dummy_category')
mods.selectionguicrafting.recipe.removeByInput(item('minecraft:cobblestone'))
mods.selectionguicrafting.recipe.removeByOutput(item('minecraft:stone'))
mods.selectionguicrafting.recipe.removeByTool(item('minecraft:wool'))
// mods.selectionguicrafting.recipe.removeAll()

mods.selectionguicrafting.recipe.recipeBuilder()
    .category('dummy_category')
    .input(item('minecraft:stone') * 3)
    .output(item('minecraft:cobblestone') * 2, 0.5f)
    .tool(item('minecraft:wooden_pickaxe'), 1.0f)
    .time(200)
    .xp(1)
    .sound('minecraft:block.anvil.land', 1.0f, 1.0f)
    .register()

mods.selectionguicrafting.recipe.recipeBuilder()
    .category('blub')
    .input(item('minecraft:diamond'))
    .output(item('minecraft:wheat_seeds') * 5, 0.5f)
    .tool(item('minecraft:grass') * 5, 1.0f)
    .register()

mods.selectionguicrafting.recipe.recipeBuilder()
    .category('dummy_category')
    .input(item('minecraft:stone') * 32)
    .output(item('minecraft:diamond') * 50, 0.5f)
    .output(item('minecraft:clay') * 2, 0.1f)
    .tool(item('minecraft:wooden_pickaxe'), 1.0f)
    .tool(item('minecraft:diamond_pickaxe'), 10.0f, 10.0f)
    .durability(10)
    .time(200)
    .xp(1)
    .sound('minecraft:block.anvil.land', 1.0f, 1.0f)
    .register()

mods.selectionguicrafting.recipe.recipeBuilder()
    .category('dead')
    .input(item('minecraft:wheat_seeds') * 3)
    .output(item('minecraft:sand') * 2)
    .tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f)
    .tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f)
    .catalyst(item('minecraft:apple') * 2, 0.9f)
    .time(40)
    .durability(1)
    .queueType(false)
    .outputType('INVENTORY')
    .xp(1)
    .register()

mods.selectionguicrafting.recipe.recipeBuilder()
    .category('dead')
    .input(item('minecraft:stick') * 3)
    .output(item('minecraft:sand') * 2)
    .tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f)
    .tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f)
    .catalyst(item('minecraft:apple') * 2, 0.9f)
    .frame('selectionguicrafting:textures/gui/frame/iron.png')
    .time(40)
    .durability(1)
    .queueType(false)
    .register()


