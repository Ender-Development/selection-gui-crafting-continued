print("Category Test");

val test = mods.selectionguicrafting.category.categoryBuilder();
test.id("test");
test.trigger(<minecraft:apple>);
test.trigger(<minecraft:diamond_pickaxe>, 10.0, 0.1, 10.0);
test.trigger(<minecraft:grass>.asBlock(), 2.0, 2.0, 2.0);
test.register();

print(test.toString());