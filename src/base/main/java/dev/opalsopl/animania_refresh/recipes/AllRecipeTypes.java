package dev.opalsopl.animania_refresh.recipes;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AnimaniaRefresh.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, AnimaniaRefresh.MODID);

    public static final RegistryObject<RecipeType<NoBucketRecipe>> NO_BUCKET_TYPE =
            RECIPE_TYPES.register("no_bucket", () -> NoBucketRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<NoBucketRecipe>> NO_BUCKET_SERIALIZER =
            RECIPE_SERIALIZERS.register("no_bucket", () -> NoBucketRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus)
    {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
