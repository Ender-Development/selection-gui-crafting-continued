
// Auto generated groovyscript example file
// MODS_LOADED: selectionguicrafting

log.info 'mod \'selectionguicrafting\' detected, running script'

// groovyscript.wiki.selectionguicrafting.selection_category.title:
// groovyscript.wiki.selectionguicrafting.selection_category.description.

mods.selectionguicrafting.selection_category.removeByName('test_category')

mods.selectionguicrafting.selection_category.categoryBuilder()
    .name('test_category')
    .displayName('Test Category')
    .register()

mods.selectionguicrafting.selection_category.categoryBuilder()
    .name('another_category')
    .displayName('Another Category')
    .register()


// groovyscript.wiki.selectionguicrafting.selection_pair.title:
// groovyscript.wiki.selectionguicrafting.selection_pair.description.

mods.selectionguicrafting.selection_pair.removeByCategory('also_a_test_category')
mods.selectionguicrafting.selection_pair.removeByInput(item('minecraft:stone'))
mods.selectionguicrafting.selection_pair.removeByTool(item('minecraft:stone_pickaxe'))
// mods.selectionguicrafting.selection_pair.removeAll()

mods.selectionguicrafting.selection_pair.pairBuilder()
    .input(item('minecraft:dirt'),item('minecraft:cobblestone'))
    .addTool(item('minecraft:stone_pickaxe'), 1.0f, 1.0f)
    .category('test_category')
    .register()

mods.selectionguicrafting.selection_pair.pairBuilder()
    .input(item('minecraft:wool'),item('minecraft:diamond'))
    .addTool(item('minecraft:golden_pickaxe'), 1.0f, 1.0f)
    .addTool(item('minecraft:iron_pickaxe'), 1.0f, 1.0f)
    .category('another_category')
    .register()


// groovyscript.wiki.selectionguicrafting.selection_recipe.title:
// groovyscript.wiki.selectionguicrafting.selection_recipe.description.

mods.selectionguicrafting.selection_recipe.removeByOutput(item('minecraft:stone'))
// mods.selectionguicrafting.selection_recipe.removeAll()

mods.selectionguicrafting.selection_recipe.recipeBuilder()
    .category('test_category')
    .inputQuantity(1)
    .durabilityUsage(1)
    .time(0)
    .output(item('minecraft:apple'))
    .register()

mods.selectionguicrafting.selection_recipe.recipeBuilder()
    .category('another_category')
    .inputQuantity(1)
    .durabilityUsage(1)
    .time(0)
    .output(item('minecraft:clay'), item('minecraft:snowball'))
    .register()


