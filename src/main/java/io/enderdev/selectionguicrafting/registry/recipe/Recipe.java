package io.enderdev.selectionguicrafting.registry.recipe;

import java.util.ArrayList;

public class Recipe {
    // required
    private ArrayList<RecipeInput> inputs = new ArrayList<>();
    private ArrayList<RecipeOutput> outputs = new ArrayList<>();
    private String category;

    // optional
    private RecipeInput mainHand;
    private RecipeInput offHand;
    private Integer time;
    private Integer xp;

    public Recipe() {
    }

    public Recipe category(String category) {
        this.category = category;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Recipe input(RecipeInput input) {
        this.inputs.add(input);
        return this;
    }
}
