package dev.opalsopl.animania_refresh.events;

import dev.opalsopl.animania_refresh.AnimaniaRefresh;
import dev.opalsopl.animania_refresh.blocks.AllBlocks;
import dev.opalsopl.animania_refresh.blocks.SeedCarpetBlock;
import dev.opalsopl.animania_refresh.types.ESeedCarpetType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AnimaniaRefresh.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractHandler {

    @SubscribeEvent
    public static void onBlockRightClickEvent(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        Direction direction = event.getHitVec().getDirection();
        Player player = event.getEntity();

        if (stack.isEmpty() ||
            !(stack.is(Items.WHEAT_SEEDS) || stack.is(Items.BEETROOT_SEEDS) ||
            stack.is(Items.MELON_SEEDS) || stack.is(Items.PUMPKIN_SEEDS)))
            return;

        BlockState targetBlock = level.getBlockState(pos);
        BlockState aboveTargetBlock = level.getBlockState(pos.above());

        BlockPos targetPos;

        if (!targetBlock.canBeReplaced() && direction != Direction.UP) return;

        if (targetBlock.canBeReplaced())
        {
            targetPos = pos;
            if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP))
                return;
        }
        else if (aboveTargetBlock.canBeReplaced())
        {
            targetPos = pos.above();

            if (!targetBlock.isFaceSturdy(level, pos, direction))
                return;
        }
        else return;

        level.setBlockAndUpdate(targetPos, AllBlocks.SEED_CARPET_BLOCK.get().defaultBlockState().setValue(SeedCarpetBlock.TYPE, ESeedCarpetType.convert(stack)));
        level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1f ,1f);

        if (!player.isCreative())
        {
            stack.shrink(1);
        }
    }
}
