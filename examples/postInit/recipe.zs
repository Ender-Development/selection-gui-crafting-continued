val myRecipe = mods.selectionguicrafting.sgc_recipe.newRecipe();
myRecipe.category("test");
myRecipe.output(<minecraft:sand> * 2);
myRecipe.tool(<minecraft:stick> * 5);
myRecipe.input(<minecraft:snow> * 3);
myRecipe.register();