package dev.opalsopl.animania_refresh.helper;

import dev.opalsopl.animania_refresh.network.NetworkHandler;
import dev.opalsopl.animania_refresh.network.ParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.joml.*;

import java.nio.ByteBuffer;
import java.util.Random;

public class ParticleHelper {
    public static class ParticleModifier
    {
        private final Vector3f NOTHING = new Vector3f(-1, -1, -1);

        private final Vector3f position;
        private final Vector3f velocity;
        private Vector3f color = NOTHING;
        private float power = -1;
        private float scale = -1;
        private int lifetime = -1;

        public ParticleModifier (Vector3f pos, Vector3f vel)
        {
            position = new Vector3f(pos);
            velocity = new Vector3f(vel);
        }

        public ParticleModifier (ParticleModifier modifier, Vector3f pos, Vector3f vel)
        {
            position = new Vector3f(pos != null ? pos : modifier.getPos());
            velocity = new Vector3f(vel != null ? vel : modifier.getVel());

            color = modifier.getColor();
            power = modifier.power;
            scale = modifier.scale;
            lifetime = modifier.lifetime;
        }

        public ParticleModifier (ByteBuffer buffer)
        {
            Vector3f pos = new Vector3f();
            Vector3f vel = new Vector3f();

            decode(buffer, pos, vel);

            position = pos;
            velocity = vel;
        }

        public ParticleModifier setColor (float r, float g, float b)
        {
            if ((0 > r && -1 != r) || (0 > g && -1 != g) || (0 > b && -1 != b))
                throw new IllegalArgumentException("RGB value must be -1 or >= 0");

            color = new Vector3f(r, g, b);

            return this;
        }

        public ParticleModifier setPower (float power)
        {
            if (0 > power && -1 != power)
                throw new IllegalArgumentException("Power must be -1 or >= 0");

            this.power = power;

            return this;
        }

        public ParticleModifier setScale (float scale)
        {
            if (0 > scale && -1 != scale)
                throw new IllegalArgumentException("Scale must be -1 or >= 0");

            this.scale = scale;

            return this;
        }

        public ParticleModifier setLifetime (int lifetime)
        {
            if (0 > lifetime && -1 != lifetime)
                throw new IllegalArgumentException("Lifetime must be -1 or >= 0");

            this.lifetime = lifetime;

            return this;
        }

        public Vector3f getColor ()
        {
            return new Vector3f(color);
        }

        public float getPower () {
            return power;
        }

        public float getScale () {
            return scale;
        }

        public int getLifetime ()
        {
             return lifetime;
        }

        public Vector3f getPos ()
        {
            return new Vector3f(position);
        }

        public double getX ()
        {
            return position.x;
        }

        public double getY ()
        {
            return position.y;
        }

        public double getZ ()
        {
            return position.z;
        }

        public Vector3f getVel ()
        {
            return new Vector3f(velocity);
        }

        public double getXd ()
        {
            return velocity.x;
        }

        public double getYd ()
        {
            return velocity.y;
        }

        public double getZd ()
        {
            return velocity.z;
        }

        public Particle modifyParticle(Particle particle)
        {
            if (color != NOTHING) {
                particle.setColor(color.x, color.y, color.z);
            }

            if (power != -1) {
                particle.setPower(power);
            }

            if (scale != -1) {
                particle.scale(scale);
            }

            if (lifetime != -1) {
                particle.setLifetime(lifetime);
            }

            return particle;
        }

        public ByteBuffer encode ()
        {
            ByteBuffer buffer = ByteBuffer.allocate((Float.BYTES * 11) + Integer.BYTES);

            buffer.putFloat(position.x);
            buffer.putFloat(position.y);
            buffer.putFloat(position.z);

            buffer.putFloat(velocity.x);
            buffer.putFloat(velocity.y);
            buffer.putFloat(velocity.z);

            buffer.putFloat(color.x);
            buffer.putFloat(color.y);
            buffer.putFloat(color.z);

            buffer.putFloat(power);
            buffer.putFloat(scale);
            buffer.putInt(lifetime);

            return buffer;
        }

        private void decode (ByteBuffer buffer, Vector3f pos, Vector3f vel)
        {
            float x = buffer.getFloat();
            float y = buffer.getFloat();
            float z = buffer.getFloat();

            float xd = buffer.getFloat();
            float yd = buffer.getFloat();
            float zd = buffer.getFloat();

            float r = buffer.getFloat();
            float g = buffer.getFloat();
            float b = buffer.getFloat();

            power = buffer.getFloat();
            scale = buffer.getFloat();
            lifetime = buffer.getInt();


            pos.set(x, y, z);
            vel.set(xd, yd, zd);
            color = new Vector3f(r, g, b);
        }
    }

    public static void spawnParticle (ParticleOptions options, ParticleModifier modifier, Vector3f spreadRange, int amount)
    {
        Random rand = new Random();

        float modX = (float) modifier.getX();
        float modY = (float) modifier.getY();
        float modZ = (float) modifier.getZ();

        float maxX = modX + spreadRange.x;
        float minX = modX - spreadRange.x;
        float maxY = modY + spreadRange.y;
        float minY = modY - spreadRange.y;
        float maxZ = modZ + spreadRange.z;
        float minZ = modZ - spreadRange.z;

        for (int i = 1; i <= amount; i++)
        {
            float x = rand.nextFloat(minX, maxX);
            float y = rand.nextFloat(minY, maxY);
            float z = rand.nextFloat(minZ, maxZ);

            spawnParticle(options, new ParticleModifier(modifier, new Vector3f(x,y,z), null));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnParticle (ParticleOptions options, ParticleModifier modifier)
    {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        Particle p = engine.createParticle(options, modifier.getX(), modifier.getY(), modifier.getZ(),
                modifier.getXd(), modifier.getYd(), modifier.getZd());

        p = modifier.modifyParticle(p);
        engine.add(p);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public  static void sendParticle (ParticleOptions options, ParticleModifier modifier, Vector3f spreadRange, int amount)
    {
        sendParticle(new ParticlePacket(modifier, options, amount, spreadRange));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public  static void sendParticle (ParticleOptions options, ParticleModifier modifier)
    {
        sendParticle(new ParticlePacket(modifier, options));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    private static void sendParticle (ParticlePacket packet)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        server.getPlayerList().getPlayers().stream()
                .filter((player) -> ParticleHelper.inRenderRange(player.position(), packet.getModifier()))
                .forEach((player) -> NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet));
    }

    public static boolean inRenderRange (Vec3 pos, ParticleModifier modifier)
    {
        double maxX = modifier.getX() + 18;
        double minX = modifier.getX() - 18;
        double maxY = modifier.getY() + 18;
        double minY = modifier.getY() - 18;
        double maxZ = modifier.getZ() + 18;
        double minZ = modifier.getZ() - 18;
        AABB boundingBox = new AABB(maxX, maxY, maxZ, minX, minY, minZ);

        return boundingBox.contains(pos);
    }
}
