package dev.opalsopl.animania_refresh.helper;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.ByteBuffer;



class ParticleModifierTest
{
    private ParticleHelper.ParticleModifier modifierFull;
    private ParticleHelper.ParticleModifier modifierEmpty;

    @BeforeEach
    public void Setup()
    {
        modifierFull = new ParticleHelper.ParticleModifier(new Vector3f(10, 11, 12), new Vector3f(13, 14, 15))
                .setColor(1, 2, 3)
                .setLifetime(500)
                .setPower(8)
                .setScale(20);

        modifierEmpty = new ParticleHelper.ParticleModifier(new Vector3f(), new Vector3f());
    }

    @Test
    public void CONSTRUCTOR_PosVel_SetsPosVel_Match()
    {
        ParticleHelper.ParticleModifier modifier1 = new ParticleHelper.ParticleModifier(
                new Vector3f(1, 2,3),
                new Vector3f(4, 5, 6));

        Assertions.assertEquals(1, modifier1.getX(), 1e-6);
        Assertions.assertEquals(2, modifier1.getY(), 1e-6);
        Assertions.assertEquals(3, modifier1.getZ(), 1e-6);
        Assertions.assertEquals(4, modifier1.getXd(), 1e-6);
        Assertions.assertEquals(5, modifier1.getYd(), 1e-6);
        Assertions.assertEquals(6, modifier1.getZd(), 1e-6);
    }

    @ParameterizedTest
    @CsvSource({
            "FULL, 1,2,3, 4,5,6",        // override all
            "FULL, -1,-1,-1, 4,5,6",     // null pos
            "FULL, 1,2,3, -1,-1,-1",     // null vel
            "FULL, -1,-1,-1, -1,-1,-1,", // null pos+vel
            "EMPTY, 1,2,3, 4,5,6",       // empty base
            "EMPTY, -1,-1,-1, 4,5,6,"    // empty base + null pos
    })
    public void Constructor_WithModifier_Match(String type, float x, float y, float z, float xd, float yd, float zd)
    {
        Vector3f pos = x == -1 ? null : new Vector3f(x, y, z);
        Vector3f vel = xd == -1 ? null : new Vector3f(xd, yd, zd);

        ParticleHelper.ParticleModifier mod = type.equals("EMPTY") ?
                modifierEmpty : modifierFull;

        ParticleHelper.ParticleModifier modifier = new ParticleHelper.ParticleModifier(mod, pos, vel);

        Assertions.assertEquals(pos != null ? x : mod.getX(), modifier.getX());
        Assertions.assertEquals(pos != null ? y : mod.getY(), modifier.getY());
        Assertions.assertEquals(pos != null ? z : mod.getZ(), modifier.getZ());

        Assertions.assertEquals(vel != null ? xd : mod.getXd(), modifier.getXd());
        Assertions.assertEquals(vel != null ? yd : mod.getYd(), modifier.getYd());
        Assertions.assertEquals(vel != null ? zd : mod.getZd(), modifier.getZd());

        Assertions.assertEquals(mod.getColor().x, modifier.getColor().x);
        Assertions.assertEquals(mod.getColor().y, modifier.getColor().y);
        Assertions.assertEquals(mod.getColor().z, modifier.getColor().z);

        Assertions.assertEquals(mod.getLifetime(), modifier.getLifetime());
        Assertions.assertEquals(mod.getPower(), modifier.getPower());
        Assertions.assertEquals(mod.getScale(), modifier.getScale());
    }

    @ParameterizedTest
    @CsvSource({"Power, EMPTY, -1", "Power, FULL, 8",
            "Lifetime, EMPTY, -1", "Lifetime, FULL, 500",
            "Scale, EMPTY, -1", "Scale, FULL, 20"})
    public void getValues_Match(String typeValue, String type, float expected)
    {
        ParticleHelper.ParticleModifier mod = type.equals("EMPTY") ?
                modifierEmpty : modifierFull;

        float actual = switch (typeValue)
        {
            case "Power" -> mod.getPower();
            case "Lifetime" -> mod.getLifetime();
            case "Scale" -> mod.getScale();

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        Assertions.assertEquals(expected, actual, 1e-6);
    }

    @ParameterizedTest()
    @CsvSource({"Pos, EMPTY, 0, 0, 0", "Pos, FULL, 10, 11, 12",
                "Vel, EMPTY, 0, 0, 0", "Vel, FULL, 13, 14, 15",
                "Color, EMPTY, -1, -1, -1", "Color, FULL, 1, 2, 3"})
    public void getVectorValues_Match (String typeValue, String type, float expectedX, float expectedY, float expectedZ)
    {
        ParticleHelper.ParticleModifier mod = type.equals("EMPTY") ?
                modifierEmpty : modifierFull;

        Vector3f value = switch (typeValue)
        {
            case "Pos" -> mod.getPos();
            case "Vel" -> mod.getVel();
            case "Color" -> new Vector3f(mod.getColor());

            default -> throw new IllegalStateException("Unexpected value: " + typeValue);
        };

        Assertions.assertEquals(expectedX, value.x, 1e-6);
        Assertions.assertEquals(expectedY, value.y, 1e-6);
        Assertions.assertEquals(expectedZ, value.z, 1e-6);
    }

    @ParameterizedTest()
    @CsvSource({"EMPTY, -1, -1, -1", "FULL, 1, 2, 3"})
    public void getColorRange_Match(String type, float expectedX, float expectedY, float expectedZ)
    {
        ParticleHelper.ParticleModifier mod = type.equals("EMPTY") ?
                modifierEmpty : modifierFull;

        Vector3f value = mod.getColorRange();

        Assertions.assertEquals(expectedX/255f, value.x, 1e-6);
        Assertions.assertEquals(expectedY/255f, value.y, 1e-6);
        Assertions.assertEquals(expectedZ/255f, value.z, 1e-6);
    }

    @ParameterizedTest
    @CsvSource({"X, EMPTY, 0", "X, FULL, 10",
                "Y, EMPTY, 0", "Y, FULL, 11",
                "Z, EMPTY, 0", "Z, FULL, 12",
                "Xd, EMPTY, 0", "Xd, FULL, 13",
                "Yd, EMPTY, 0", "Yd, FULL, 14",
                "Zd, EMPTY, 0", "Zd, FULL, 15"})
    public void getVectorComponents_Match (String typeValue, String type, double expected)
    {
        ParticleHelper.ParticleModifier mod = type.equals("EMPTY") ?
                modifierEmpty : modifierFull;

        double value = switch (typeValue)
        {
            case "X" -> mod.getX();
            case "Y" -> mod.getY();
            case "Z" -> mod.getZ();
            case "Xd" -> mod.getXd();
            case "Yd" -> mod.getYd();
            case "Zd" -> mod.getZd();

            default -> throw new IllegalStateException("Unexpected value: " + typeValue);
        };

        Assertions.assertEquals(expected, value, 1e-6);
    }

    @ParameterizedTest
    @CsvSource({"Power, -1", "Power, 0", "Power, 50", "Power, 1000",
                "Lifetime, -1", "Lifetime, 0", "Lifetime, 50", "Lifetime, 1000",
                "Scale, -1", "Scale, 0", "Scale, 50", "Scale, 1000"})
    public void setValues_Match(String type, float expected)
    {
        ParticleHelper.ParticleModifier mod = modifierEmpty;

        switch (type)
        {
            case "Power" -> mod.setPower(expected);
            case "Lifetime" -> mod.setLifetime((int) expected);
            case "Scale" -> mod.setScale(expected);

            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

        float actual = switch (type)
        {
            case "Power" -> mod.getPower();
            case "Lifetime" -> mod.getLifetime();
            case "Scale" -> mod.getScale();

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        Assertions.assertEquals(expected, actual, 1e-6);
    }

    @ParameterizedTest
    @CsvSource({"Power, -7f", "Power, -1.1f", "Power, -0.1f", "Power, -0.9",
            "Lifetime, -7", "Lifetime, -30",
            "Scale, -7f", "Scale, -1.1f", "Scale, -0.1f", "Scale, -0.9f"})
    public void setValues_ThrowsException(String type, float expected)
    {
        ParticleHelper.ParticleModifier mod = modifierEmpty;

        Exception e = Assertions.assertThrows(IllegalArgumentException.class, () ->
            {
                switch (type)
                {
                    case "Power" -> mod.setPower(expected);
                    case "Lifetime" -> mod.setLifetime((int) expected);
                    case "Scale" -> mod.setScale(expected);

                    default -> throw new IllegalStateException("Unexpected value: " + type);
                }
            });

        Assertions.assertEquals(String.format("%s must be -1 or >= 0", type), e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"1, 80, 70", "255, 255, 255", "0, 0, 0", "-1, -1, -1"})
    public void setColor_Match(int r, int g, int b)
    {
        modifierEmpty.setColor(r, g, b);

        Vector3i color = modifierEmpty.getColor();

        Assertions.assertArrayEquals(new float[] {r, g, b}, new float[]{color.x, color.y, color.z});
    }

    @ParameterizedTest
    @CsvSource({"-1, -1, 4", "256, 1888, 21", "-30, -80, 5"})
    public void setColor_ThrowsException(int r, int g, int b)
    {
        Exception e = Assertions.assertThrows(IllegalArgumentException.class, () ->
            modifierEmpty.setColor(r,g,b));

        String message = e.getMessage();

        boolean isValidMessage = message.equals("RGB value must be -1 or between 0 and 255")
                                || message.equals("All values must be -1 to not change color");

        Assertions.assertTrue(isValidMessage, "Unexpected exception message: " +  message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"full", "empty"})
    public void encodeDecode_RoundTrip_Match(String type)
    {
        ParticleHelper.ParticleModifier mod = type.equals("empty") ?
                modifierEmpty : modifierFull;

        ByteBuffer buf = mod.encode();

        ParticleHelper.ParticleModifier decodedMod = new ParticleHelper.ParticleModifier(buf);

        Vector3i col = mod.getColor();
        Vector3f pos = mod.getPos();
        Vector3f vel = mod.getVel();

        Vector3i colDec = decodedMod.getColor();
        Vector3f posDec = decodedMod.getPos();
        Vector3f velDec = decodedMod.getVel();


        boolean isEqual = (col.x == colDec.x &&
                col.y == colDec.y &&
                col.z == colDec.z &&

                pos.x == posDec.x &&
                pos.y == posDec.y &&
                pos.z == posDec.z &&

                vel.x == velDec.x &&
                vel.y == velDec.y &&
                vel.z == velDec.z &&

                mod.getPower() == decodedMod.getPower() &&
                mod.getScale() == decodedMod.getScale() &&
                mod.getLifetime() == decodedMod.getLifetime());

        Assertions.assertTrue(isEqual);
    }

    //note: ModifyParticles cannot be tested without minecraft
}
