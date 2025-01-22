package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.cleanroommc.groovyscript.documentation.linkgenerator.LinkGeneratorHooks;

import io.enderdev.selectionguicrafting.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GSPlugin implements GroovyPlugin {
    @GroovyBlacklist
    public static GSContainer instance;

    @Override
    public @Nullable GroovyPropertyContainer createGroovyPropertyContainer() {
        GSPlugin.instance = new GSContainer();
        return GSPlugin.instance;
    }

    @Override
    public @NotNull String getModId() {
        return Tags.MOD_ID;
    }

    @Override
    public @NotNull String getContainerName() {
        return Tags.MOD_NAME;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        LinkGeneratorHooks.registerLinkGenerator(new LinkGenerator());
    }
}
