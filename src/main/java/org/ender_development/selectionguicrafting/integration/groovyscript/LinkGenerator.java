/*
 * Copyright (c) 2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.documentation.linkgenerator.BasicLinkGenerator;

import org.ender_development.selectionguicrafting.Tags;

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
