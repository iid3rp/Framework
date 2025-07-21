package framework.entity;

import framework.lang.Vec3;

public class Light {
    private Vec3 position;
    private Vec3 color;
    private Vec3 attenuation;

    public Light(Vec3 position, Vec3 color) {
        this.position = position;
        this.color = color;
        this.attenuation = new Vec3(1, 0, 0);
    }

    public Light(Vec3 position, Vec3 color, Vec3 attenuation)
    {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getColor() {
        return color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public Vec3 getAttenuation()
    {
        return attenuation;
    }

    public void setAttenuation(Vec3 attenuation)
    {
        this.attenuation = attenuation;
    }
}
