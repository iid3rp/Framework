package framework.entity;

public class Light
{
    public float posX, posY, posZ;
    public float constant, linear, quadratic;
    public float r, g, b; // color
    private float distance; // < 0 means infinite distance
    private float intensity;

    public Light()
    {
        distance = -1f;
        r = g = b = 255f;
        posX = posY = posZ = constant = linear = quadratic = 0f;
    }

    public void setPosition(float x, float y, float z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    public float getPosX()
    {
        return posX;
    }

    public void setPosX(float posX)
    {
        this.posX = posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosY(float posY)
    {
        this.posY = posY;
    }

    public float getPosZ()
    {
        return posZ;
    }

    public void setPosZ(float posZ)
    {
        this.posZ = posZ;
    }

    public float getConstant()
    {
        return constant;
    }

    public void setConstant(float constant)
    {
        this.constant = constant;
    }

    public float getLinear()
    {
        return linear;
    }

    public void setLinear(float linear)
    {
        this.linear = linear;
    }

    public float getQuadratic()
    {
        return quadratic;
    }

    public void setQuadratic(float quadratic)
    {
        this.quadratic = quadratic;
    }

    public void setColor(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getDistance()
    {
        return distance;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }
}
