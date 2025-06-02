package io.enderdev.selectionguicrafting.registry.recipe;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.Category;
import io.enderdev.selectionguicrafting.registry.category.OutputType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeHandler implements ICommandSender {
    private final EntityPlayer player;
    private final Category category;
    private final Recipe recipe;
    private final RecipeHelper recipeHelper;
    private final double durabilityMultiplier;
    private final int xp;

    public RecipeHandler(EntityPlayer player, Category category, int recipeIndex, double durabilityMultiplier, int xp) {
        this.player = player;
        this.category = category;
        this.recipe = Register.getRecipesByCategory(category).get(recipeIndex);
        this.recipeHelper = new RecipeHelper(recipe);
        this.durabilityMultiplier = durabilityMultiplier;
        this.xp = xp;
    }

    public boolean validate() {
        return player != null && category != null && recipe != null && recipeHelper.canCraft(player, durabilityMultiplier);
    }

    public void craft() {
        if (!validate())
            return;

        // If we got here, the recipe is valid and can be processed
        recipe.getOutputs().forEach(output -> {
            ItemStack stack = output.getItemStack().copy();
            if (player.world.rand.nextDouble() < output.getChance())
                if (recipeHelper.getOutputType() == OutputType.DROP || !player.inventory.addItemStackToInventory(stack))
                    player.dropItem(stack, false, true);
        });

        if (recipeHelper.hasMainHand())
            recipe.getMainHand().consume(player.getHeldItemMainhand(), durabilityMultiplier);

        if (recipeHelper.hasOffHand())
            recipe.getOffHand().consume(player.getHeldItemOffhand(), durabilityMultiplier);

        if(!recipe.getInputs().isEmpty()) {
            for(RecipeInput input : recipe.getInputs()) {
                if(!input.beConsumed())
                    continue;
                for(ItemStack stack : player.inventory.mainInventory)
                    if(input.compare(stack, durabilityMultiplier)) {
                        input.consume(stack, durabilityMultiplier);
                        break;
                    }
            }
        }

        player.addExperience(xp);

        if (!recipe.getCommands().isEmpty() && getServer() != null) {
            recipe.getCommands().forEach(command -> getServer().getCommandManager().executeCommand(this, command));
        }
    }

    @Override
    public @NotNull String getName() {
        return Tags.MOD_NAME;
    }

    @Override
    public boolean canUseCommand(int permLevel, @NotNull String commandName) {
        return permLevel <= 2;
    }

    @Override
    public @NotNull World getEntityWorld() {
        return player.getEntityWorld();
    }

    @Override
    public @Nullable MinecraftServer getServer() {
        return player.getServer();
    }
}
