package framework.resources;

public final class Resources
{
    private static Class<?> resourceField = Resources.class;

    public static Class<?> getResource()
    {
        return resourceField;
    }

    public void setResource(Class<?> resource)
    {
        this.resourceField = resource;
    }
}
