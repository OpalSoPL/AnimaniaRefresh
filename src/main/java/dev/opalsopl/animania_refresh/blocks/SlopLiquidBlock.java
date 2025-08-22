package dev.opalsopl.animania_refresh.blocks;

import dev.opalsopl.animania_refresh.helper.ParticleHelper;

import dev.opalsopl.animania_refresh.sounds.AllSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.joml.Vector3f;

import java.util.Random;
import java.util.function.Supplier;

public class SlopLiquidBlock extends LiquidBlock {

    private final Random random;

    public SlopLiquidBlock(Supplier<? extends FlowingFluid> sup, Properties properties) {
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

        if (level.getBlockState(pos.above()).is(Blocks.AIR) && random.nextInt(15) == 0)
        {
            ParticleHelper.spawnParticle(ParticleTypes.EFFECT, new ParticleHelper.ParticleModifier(
                            new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f),
                            new Vector3f(0, 0, 0))
                    .setColor(125, 76, 16)
                    , new Vector3f(0.5f, 0.3f, 0.5f), 3);
        }
    }

//    @Override
//    public void entityInside(BlockState state, Level level, BlockPos pos, Entity cause) {}
}