print("Category Test");

val test = mods.selectionguicrafting.category.categoryBuilder();
test.id("test");
test.displayName("Test");
test.register();

print(test.toString());
