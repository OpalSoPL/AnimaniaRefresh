package dev.opalsopl.animania_refresh.recipes;

import com.google.gson.JsonObject;
import dev.opalsopl.animania_refresh.helper.ResourceHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

public class NoBucketRecipe extends ShapelessRecipe {
    public NoBucketRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, category, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingInv) {
        return NonNullList.withSize(craftingInv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<NoBucketRecipe>
    {
        public static final Type INSTANCE = new Type();
        public static final String ID = "no_bucket";
    }

    public static class Serializer implements RecipeSerializer<NoBucketRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceHelper.getModResourceLocation("no_bucket");


        @Override
        public NoBucketRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS_RECIPE.fromJson(id, json);
            return new NoBucketRecipe(
                    recipe.getId(),
                    recipe.getGroup(),
                    recipe.category(),
                    recipe.getResultItem(RegistryAccess.EMPTY),
                    recipe.getIngredients()
            );
        }

        @Override
        public @Nullable NoBucketRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe recipe = ShapelessRecipe.Serializer.SHAPELESS_RECIPE.fromNetwork(id, buf);

            return new NoBucketRecipe(
                    recipe.getId(),
                    recipe.getGroup(),
                    recipe.category(),
                    recipe.getResultItem(RegistryAccess.EMPTY),
                    recipe.getIngredients()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, NoBucketRecipe recipe) {
            ShapelessRecipe.Serializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
        }
    }
 }
