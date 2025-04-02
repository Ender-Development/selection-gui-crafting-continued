package io.enderdev.selectionguicrafting.registry;

import io.enderdev.selectionguicrafting.registry.util.Validation;

public interface IRegisterObject {
    /**
     * Every object that implements this interface must have a ErrorCheck
     */
    Validation ErrorCheck = new Validation();

    /**
     * Register the object in the registry.
     */
    IRegisterObject register();

    /**
     * Check if the object is valid.
     */
    boolean validate();
}
