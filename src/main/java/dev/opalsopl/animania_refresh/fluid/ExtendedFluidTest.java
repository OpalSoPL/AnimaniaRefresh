package dev.opalsopl.animania_refresh.fluid;

import dev.opalsopl.animania_refresh.helper.ParticleHelper;

import dev.opalsopl.animania_refresh.sounds.AllSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.joml.Vector3f;

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
                        AllSounds.SLOP_FLOW.get(),
                        SoundSource.BLOCKS,
                        0.5F, //volume
                        0.8F + random.nextFloat() * 0.4F, //pitch
                        false //distance delay
                );
            }
        }

        if (random.nextInt(15) == 0)
        {
            ParticleHelper.spawnParticle(ParticleTypes.EFFECT, new ParticleHelper.ParticleModifier(pos.getX(), pos.getY(), pos.getZ(), 0f, 5f, 0f)
                    .setColor(1, 0, 0)
                    , new Vector3f(1, 1, 1), 10);
        }
    }
}