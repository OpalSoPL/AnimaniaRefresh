package dev.opalsopl.animania_refresh.network;

import java.util.function.Supplier;
import dev.opalsopl.animania_refresh.helper.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


public class ParticlePacket{
    final ParticleHelper.ParticleModifier modifier;
    final ParticleOptions particleOptions;

    public ParticlePacket(ParticleHelper.ParticleModifier modifier, ParticleOptions particleOptions)
    {
        this.modifier = modifier;
        this.particleOptions = particleOptions;
    }

    public  ParticlePacket(FriendlyByteBuf buf)
    {
        this.modifier = new ParticleHelper.ParticleModifier(buf.readBytes((Float.BYTES * 11) + Integer.BYTES).nioBuffer());
        particleOptions = buf.readJsonWithCodec(ParticleTypes.CODEC);
    }

    public void Encode (FriendlyByteBuf buf)
    {
        buf.writeBytes(modifier.encode());
        buf.writeJsonWithCodec(ParticleTypes.CODEC, particleOptions);
    }

    public static void handle(ParticlePacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                MinecraftServer server = context.getSender().getServer();

                assert server != null;

                server.getPlayerList().getPlayers().stream()
                        .filter((player) -> ParticleHelper.inRenderRange(player.position(), msg.modifier))
                        .forEach((player) -> NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), msg));
            });
        }
        else {
            context.enqueueWork(() -> {
                ParticleHelper.spawnParticle(msg.particleOptions, msg.modifier);
                context.setPacketHandled(true);
            });
        }
    }

    public static void register(SimpleChannel channel) {
        channel.registerMessage(2137, ParticlePacket.class,
                ParticlePacket::Encode,
                ParticlePacket::new,
                ParticlePacket::handle
        );
    }
}
