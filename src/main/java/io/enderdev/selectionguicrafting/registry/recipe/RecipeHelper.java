package io.enderdev.selectionguicrafting.registry.recipe;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.*;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeHelper {
    private final Recipe recipe;
    private final RecipeData recipeData;
    private final Category category;
    private final CategoryData categoryData;

    public RecipeHelper(Recipe recipe) {
        this.recipe = recipe;
        this.category = Register.getCategoryByID(recipe.getCategory());
        this.recipeData = recipe.getRecipeData();
        this.categoryData = category.getScreenData();
    }

    public ResourceLocation getFrame() {
        return recipeData.getFrame() != null ? recipeData.getFrame() : categoryData.getFrame();
    }

    public ResourceLocation getProgressBar() {
        return recipeData.getProgressBar() != null ? recipeData.getProgressBar() : categoryData.getProgressBar();
    }

    public OutputType getOutputType() {
        return recipeData.getOutputType() != null ? recipeData.getOutputType() : categoryData.getOutputType();
    }

    public QueueType getQueueable() {
        return recipeData.getQueueable() != null ? recipeData.getQueueable() : categoryData.getQueueable();
    }

    public SoundType getSoundType() {
        return recipeData.getSoundType() != null ? recipeData.getSoundType() : categoryData.getSoundType();
    }

    public ArrayList<Sound> getSounds() {
        if (!recipeData.getSounds().isEmpty()) {
            return recipeData.getSounds();
        } else if (!categoryData.getSounds().isEmpty()) {
            return categoryData.getSounds();
        }
        return new ArrayList<Sound>() {{
            add(new Sound(new ResourceLocation("minecraft", "block.anvil.use"), 0.1f, 1));
            add(new Sound(new ResourceLocation("minecraft", "block.anvil.break"), 0.1f, 1));
        }};
    }

    public ArrayList<Particle> getParticles() {
        if (!recipeData.getParticles().isEmpty()) {
            return recipeData.getParticles();
        } else if (!categoryData.getParticles().isEmpty()) {
            return categoryData.getParticles();
        }
        return new ArrayList<Particle>() {{
            add(new Particle(EnumParticleTypes.VILLAGER_HAPPY, 10, 0.5f));
        }};
    }

    public boolean canCraft(EntityPlayer player, double multiplier) {
        if (!checkAdvancement(player)) return false;
        if (!checkGamestage(player)) return false;
        if (!checkSkill(player)) return false;

        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();
        ArrayList<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);

        if (!checkMainHand(mainHand, multiplier)) return false;
        if (!checkOffHand(offHand, multiplier)) return false;

        // this will only work for a whole inventory slot having a good amount of items
        // if you'd want to also check for the items being spread out across multiple slots (like 3 slots each with 1 snow blocks and the recipe requiring 3 snow blocks)
        // you'd probably want to consume the recipe input instead of consuming item stacks, and at the end check if any inputs haven't been fully consumed

        // go through all recipeInputs, if all can be found in the simplified inventory, return true
        for (RecipeInput input : recipe.getInputs()) {
            boolean found = false;
            for (ItemStack stack : inventory)
                if (input.compare(stack, multiplier)) {
                    inventory.remove(stack);
                    found = true;
                    break;
                }
            if (!found)
                return false;
        }

        return true;
    }

    public int getAbsoluteXP(double multiplier) {
        return (int) (recipe.getXP() * multiplier);
    }

    public boolean hasMainHand() {
        return recipe.getMainHand() != null && recipe.getMainHand().getIngredient().getMatchingStacks().length != 0;
    }

    public boolean hasOffHand() {
        return recipe.getOffHand() != null && recipe.getOffHand().getIngredient().getMatchingStacks().length != 0;
    }

    public boolean checkMainHand(ItemStack hand, double multiplier) {
        return !hasMainHand() || (!hand.isEmpty() && recipe.getMainHand().compare(hand, multiplier));
    }

    public boolean checkOffHand(ItemStack hand, double multiplier) {
        return !hasOffHand() || (!hand.isEmpty() && recipe.getOffHand().compare(hand, multiplier));
    }

    public boolean checkAdvancement(EntityPlayer player) {
        if (recipe.getAdvancements().isEmpty()) return true;
        return recipe.getAdvancements().stream().allMatch(advancement -> hasAdvancement(player, advancement));
    }

    public boolean checkGamestage(EntityPlayer player) {
        if (!Loader.isModLoaded("gamestages") || recipe.getGamestages().isEmpty()) return true;
        return GameStageHelper.hasAllOf(player, recipe.getGamestages());
    }

    public boolean checkSkill(EntityPlayer player) {
        if (!Loader.isModLoaded("reskillable") || recipe.getSkills().isEmpty()) return true;
        Map<String, Integer> playerData = new HashMap<>();
        boolean check = true;
        PlayerDataHandler.get(player).getAllSkillInfo().forEach(playerSkillInfo -> playerData.put(playerSkillInfo.skill.getKey(), playerSkillInfo.getLevel()));
        for (Map.Entry<String, Integer> entry : recipe.getSkills().entrySet()) {
            String skill = entry.getKey();
            Integer level = entry.getValue();
            if (!playerData.containsKey(skill) || playerData.get(skill) < level) {
                check = false;
            }
        }
        return check;
    }

    /*
     * <https://github.com/Ender-Development/EndExpansion-TheLamentedIslands/blob/32b02b3ebc31ff632952b653989b5a9bf26a1813/src/main/java/com/example/structure/proxy/ClientProxy.java#L109>
     * <https://github.com/Ender-Development/EndExpansion-TheLamentedIslands/blob/32b02b3ebc31ff632952b653989b5a9bf26a1813/src/main/java/com/example/structure/proxy/CommonProxy.java#L107>
     */
    private boolean hasAdvancement(EntityPlayer player, ResourceLocation locAdvancement) {
        Advancement advancement;
        if(player instanceof EntityPlayerSP) {
            ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
            advancement = manager.getAdvancementList().getAdvancement(locAdvancement);
            if(advancement == null) {
                SelectionGuiCrafting.LOGGER.debug("Advancement is null: {}", locAdvancement);
                return false;
            }
            AdvancementProgress progress = manager.advancementToProgress.get(advancement);
            return progress != null && progress.isDone();
        }
        if (player instanceof EntityPlayerMP) {
            advancement = ((EntityPlayerMP) player).getServerWorld().getAdvancementManager().getAdvancement(locAdvancement);
            return advancement != null && ((EntityPlayerMP) player).getAdvancements().getProgress(advancement).isDone();
        }
        SelectionGuiCrafting.LOGGER.debug("Unable to locate Advancement: {}", locAdvancement);
        return false;
    }
}
