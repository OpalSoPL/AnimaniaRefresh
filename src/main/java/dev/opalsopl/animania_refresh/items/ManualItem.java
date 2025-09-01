package dev.opalsopl.animania_refresh.items;

import dev.opalsopl.animania_refresh.gui.ManualScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManualItem extends Item {
    public ManualItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide)
        {
            Minecraft.getInstance().setScreen(new ManualScreen(Component.literal("lol")));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
