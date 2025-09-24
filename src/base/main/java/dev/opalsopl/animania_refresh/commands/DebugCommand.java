package dev.opalsopl.animania_refresh.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.opalsopl.animania_refresh.blocks.entities.NestBlockEntity;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;

public class DebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
    {
        dispatcher.register(Commands.literal("animania_debug")
            .then(Commands.literal("nest")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .then(Commands.literal("set")
                        .then(Commands.argument("count", IntegerArgumentType.integer(-4, 4))
                            .then(Commands.argument("item", ItemArgument.item(context))
                                .executes(DebugCommand::setExec))))
                    .then(Commands.literal("get")
                        .executes(DebugCommand::getExec)))));
    }

    private static int getExec(CommandContext<CommandSourceStack> context)
    {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        BlockEntity entity = context.getSource().getLevel().getBlockEntity(pos);

        if (!(entity instanceof NestBlockEntity nest))
        {
            context.getSource().sendFailure(Component.literal("This isn't a nest_be"));
            return -1;
        }

        ItemStack[] stacks = new ItemStack[4];

        for (int i = 0; i < nest.nestContents.getSlots(); i++)
        {
            stacks[i] = nest.nestContents.getStackInSlot(i);
        }

        context.getSource().sendSystemMessage(Component.literal(Arrays.toString(stacks)));

        return 0;
    }

    private static int setExec(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        int count = IntegerArgumentType.getInteger(context, "count");
        ItemStack items  = ItemArgument.getItem(context, "item").createItemStack(1, false);

        BlockEntity entity = context.getSource().getLevel().getBlockEntity(pos);

        if (!(entity instanceof NestBlockEntity nest))
        {
            context.getSource().sendFailure(Component.literal("This isn't a nest_be"));
            return -1;
        }

        if (count < 0)
        {
            for (int a = count; a < 0; a++)
            {
                int slot = nest.nestContents.getFirstSlotWithItem(items.getItem());

                if (slot == -1) return 0;

                nest.nestContents.extractItem(slot, 1, false);
            }
            return 0;
        }

        for (int i = 0; i < count; i++)
        {
            int slot = nest.nestContents.getFirstEmptySlot();

            if (slot == -1) return 0;

            nest.nestContents.animalInsert(slot, items.getItem(), false);
        }
        return 0;
    }
}
