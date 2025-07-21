package framework.model;


import framework.textures.Texture;

public class TexturedModel
{
    private final Model model;
    private final Texture texture;


    public TexturedModel(Model model, Texture texture)
    {
        this.model = model;
        this.texture = texture;
    }

    public Model getModel()
    {
        return model;
    }

    public Texture getTexture()
    {
        return texture;
    }
}
