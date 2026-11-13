/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.registry;

import org.ender_development.selectionguicrafting.registry.util.Validation;

public interface IRegisterObject {
    /**
     * Every object that implements this interface must have a ErrorCheck
     */
    Validation ErrorCheck = new Validation();

    /**
     * @return get the error check of the object
     */
    Validation getErrorCheck();

    /**
     * Register the object in the registry.
     */
    IRegisterObject register();

    /**
     * @return true if the error check is valid
     */
    boolean validate();
}
