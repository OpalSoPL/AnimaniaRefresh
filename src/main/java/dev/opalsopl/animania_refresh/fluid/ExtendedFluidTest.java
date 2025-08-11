package dev.opalsopl.animania_refresh.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Random;
import java.util.function.Supplier;

public class ExtendedFluidTest extends LiquidBlock {

    private final Random random;

    public ExtendedFluidTest(Supplier<? extends FlowingFluid> sup, Properties properties) {
        super(sup, properties);
        random = new Random();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (!getFluidState(state).isSource())
        {
            if (random.nextInt(30) == 0) //probability of playing sound
            {
                level.playLocalSound( //https://docs.minecraftforge.net/en/1.20.x/gameeffects/sounds/#level //3rd method
                        pos,
                        SoundEvents.HONEY_BLOCK_SLIDE, //todo replace with separate SoundEvent
                        SoundSource.BLOCKS,
                        0.5F, //volume
                        0.8F + random.nextFloat() * 0.4F, //pitch
                        false //distance delay
                );
            }
        }

        if (random.nextInt(15) == 0)
        {
            if (level.isClientSide())
            {// last float = size
                level.addParticle(ParticleTypes.EFFECT, pos.getX(), pos.getY(), pos.getZ(), 0d, 0d, 0d);
            }
        }
    }
}
