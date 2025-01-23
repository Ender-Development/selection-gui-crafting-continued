package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {

    public final SgcCategory category = new SgcCategory();
    public final SgcRecipe recipe = new SgcRecipe();

    public GSContainer() {
        addProperty(category);
        addProperty(recipe);
    }
}
