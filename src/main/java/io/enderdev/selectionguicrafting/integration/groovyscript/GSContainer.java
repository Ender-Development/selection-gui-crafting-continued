package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {

    public final Category category = new Category();
    public final Recipe recipe = new Recipe();

    public GSContainer() {
        addProperty(category);
        addProperty(recipe);
    }
}
