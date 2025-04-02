print("Category Test");

val test = mods.selectionguicrafting.category.categoryBuilder();
test.id("test");
test.trigger(<minecraft:apple>);
test.register();

print(test.toString());
