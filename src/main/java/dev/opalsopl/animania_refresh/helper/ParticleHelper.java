package dev.opalsopl.animania_refresh.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.NotImplementedException;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ParticleHelper {
    public static class ParticleModifier
    {
        private final Vector3d position;
        private final Vector3d velocity;
        private Vector3f color;
        private float power = -1;
        private float scale = -1;
        private int lifetime = -1;

        public ParticleModifier(double x, double y, double z, double xd, double yd, double zd)
        {
            position = new Vector3d(x, y, z);
            velocity = new Vector3d(xd, yd, zd);
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

        public Vector3d getPos ()
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

        public Vector3d getVel ()
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
        throw new NotImplementedException();
    }
}
