val myRecipe = mods.selectionguicrafting.recipe.recipeBuilder();
myRecipe.category("test");
myRecipe.output(<minecraft:sand> * 2);
myRecipe.input(<minecraft:snow> * 3);
myRecipe.input(<minecraft:diamond_pickaxe>, 10);
myRecipe.register();

mods.selectionguicrafting.recipe.recipeBuilder().category("test").output(<minecraft:dirt> * 5).input(<minecraft:stone> * 2).input(<minecraft:cobblestone> * 2).register();

mods.selectionguicrafting.recipe.recipeBuilder().category("test").output(<minecraft:diamond> * 2).input(<minecraft:gold_ingot> * 2).mainHand(<minecraft:diamond_pickaxe>, 5).register();