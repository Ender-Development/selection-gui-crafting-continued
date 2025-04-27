package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public class GSContainer extends GroovyPropertyContainer {

    public final GroovyCategory category = new GroovyCategory();

    public GSContainer() {
        addProperty(category);
    }
}
