
// Auto generated groovyscript example file
// MODS_LOADED: selectionguicrafting

log.info 'mod \'selectionguicrafting\' detected, running script'

// groovyscript.wiki.selectionguicrafting.groovy_category.title:
// groovyscript.wiki.selectionguicrafting.groovy_category.description.

mods.selectionguicrafting.groovy_category.removeByName('dummy_category_1')
// mods.selectionguicrafting.groovy_category.removeAll()

mods.selectionguicrafting.groovy_category.categoryBuilder()
    .id('dummy_category')
    .trigger(item('minecraft:diamond'))
    .background('selectionguicrafting:textures/gui/background/wood.png')
    .register()

mods.selectionguicrafting.groovy_category.categoryBuilder()
    .id('blub')
    .trigger(item('minecraft:stone_shovel'))
    .background('selectionguicrafting:textures/gui/background/lake.png')
    .backgroundType('SINGLE_CUT')
    .register()

mods.selectionguicrafting.groovy_category.categoryBuilder()
    .id('dead')
    .trigger(block('minecraft:snow'))
    .background('selectionguicrafting:textures/gui/background/deadlands.png')
    .decoration('selectionguicrafting:textures/gui/decor/gold.png')
    .border('selectionguicrafting:textures/gui/background/wood.png')
    .backgroundType('SINGLE_CUT')
    .register()


