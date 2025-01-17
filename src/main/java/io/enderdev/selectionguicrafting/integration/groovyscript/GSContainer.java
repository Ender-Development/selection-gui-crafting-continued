package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {
    public final SelectionRecipe selectionRecipe = new SelectionRecipe();
    public final SelectionPair selectionPair = new SelectionPair();
    public final SelectionCategory selectionCategory = new SelectionCategory();

    public GSContainer() {
        addProperty(selectionRecipe);
        addProperty(selectionPair);
        addProperty(selectionCategory);
    }
}
