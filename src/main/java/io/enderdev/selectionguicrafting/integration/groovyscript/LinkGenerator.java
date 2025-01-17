package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;
import io.enderdev.selectionguicrafting.Tags;

public class LinkGenerator extends BasicLinkGenerator {
    @Override
    public String id() {
        return Tags.MOD_ID;
    }

    @Override
    protected String domain() {
        return "https://github.com/Ender-Development/selection-gui-crafting-continued/";
    }
}
