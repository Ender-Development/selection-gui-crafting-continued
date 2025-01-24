val myRecipe = mods.selectionguicrafting.recipe.recipeBuilder();
myRecipe.category("test");
myRecipe.output(<minecraft:sand> * 2);
myRecipe.tool(<minecraft:stick> * 5);
myRecipe.input(<minecraft:snow> * 3);
myRecipe.register();

mods.selectionguicrafting.recipe.recipeBuilder().category("test").output(<minecraft:sand> * 5).tool(<minecraft:stick> * 10).input(<minecraft:snow> * 2).register();