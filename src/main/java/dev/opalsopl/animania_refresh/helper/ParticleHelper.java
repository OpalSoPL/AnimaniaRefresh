package dev.opalsopl.animania_refresh.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import java.nio.ByteBuffer;

public class ParticleHelper {
    public static class ParticleModifier
    {
        private final Vector3f position;
        private final Vector3f velocity;
        private Vector3f color;
        private float power = -1;
        private float scale = -1;
        private int lifetime = -1;

        public ParticleModifier(float x, float y, float z, float xd, float yd, float zd)
        {
            position = new Vector3f(x, y, z);
            velocity = new Vector3f(xd, yd, zd);
        }

        public ParticleModifier(ByteBuffer buffer)
        {
            Vector3f pos = new Vector3f();
            Vector3f vel = new Vector3f();

            decode(buffer, pos, vel);

            position = pos;
            velocity = vel;
        }

        public ParticleModifier setColor (float r, float g, float b)
        {
            color = new Vector3f(r, g, b);

            return this;
        }

        public ParticleModifier setPower (float power)
        {
            this.power = power;

            return this;
        }

        public ParticleModifier setScale (float scale)
        {
            this.scale = scale;

            return this;
        }

        public ParticleModifier setLifetime (int lifetime)
        {
            this.lifetime = lifetime;

            return this;
        }

        public Vector3f getColor ()
        {
            return color;
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
            return position;
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
            return velocity;
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
            if (color != null) {
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
            pos.set(r, g, b);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnParticle (ParticleOptions Options, ParticleModifier modifier)
    {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        Particle p = engine.createParticle(Options, modifier.getX(), modifier.getY(), modifier.getZ(),
                modifier.getXd(), modifier.getYd(), modifier.getZd());

        p = modifier.modifyParticle(p);
        engine.add(p);
    }

    public  static void sendParticle (ParticleOptions Options, ParticleModifier modifier)
    {

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
