package dev.opalsopl.animania_refresh.network;

import java.util.function.Supplier;
import dev.opalsopl.animania_refresh.helper.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.joml.Vector3f;


public class ParticlePacket{
    final ParticleHelper.ParticleModifier modifier;
    final ParticleOptions particleOptions;
    final boolean extendedFlag; //has spread and amount data?
    Vector3f spreadRange;
    int amount;

    public ParticlePacket(ParticleHelper.ParticleModifier modifier, ParticleOptions particleOptions)
    {
        extendedFlag = false;
        this.modifier = modifier;
        this.particleOptions = particleOptions;
    }

    public ParticlePacket(ParticleHelper.ParticleModifier modifier, ParticleOptions particleOptions, int amount, Vector3f spreadRange)
    {
        extendedFlag = true;
        this.modifier = modifier;
        this.particleOptions = particleOptions;
        this.amount = amount;
        this.spreadRange = spreadRange;
    }

    public  ParticlePacket(FriendlyByteBuf buf)
    {
        extendedFlag = buf.readBoolean();
        modifier = new ParticleHelper.ParticleModifier(buf.readBytes((Float.BYTES * 11) + Integer.BYTES).nioBuffer());
        particleOptions = buf.readJsonWithCodec(ParticleTypes.CODEC);

        if (extendedFlag)
        {
            amount = buf.readInt();
            spreadRange = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    }

    public ParticleHelper.ParticleModifier getModifier()
    {
        return modifier;
    }

    public ParticleOptions getParticleOptions() {
        return particleOptions;
    }

    public Vector3f getSpreadRange() {
        return spreadRange;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isExtended()
    {
        return extendedFlag;
    }

    public void Encode (FriendlyByteBuf buf)
    {
        buf.writeBoolean(extendedFlag);
        buf.writeBytes(modifier.encode());
        buf.writeJsonWithCodec(ParticleTypes.CODEC, particleOptions);

        if (extendedFlag)
        {
            buf.writeInt(amount);

            buf.writeFloat(spreadRange.x);
            buf.writeFloat(spreadRange.y);
            buf.writeFloat(spreadRange.z);
        }
    }

    public static void handle(ParticlePacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!msg.extendedFlag)
            {
                ParticleHelper.spawnParticle(msg.particleOptions, msg.modifier);
            }
            else
            {
                ParticleHelper.spawnParticle(msg.particleOptions, msg.modifier, msg.spreadRange, msg.amount);
            }
            context.setPacketHandled(true);
        });
    }

    public static void register(SimpleChannel channel) {
        channel.registerMessage(2137, ParticlePacket.class,
                ParticlePacket::Encode,
                ParticlePacket::new,
                ParticlePacket::handle
        );
    }
}
