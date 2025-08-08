package framework.model;


import framework.lang.Mat4;
import framework.textures.Texture;

import java.util.Objects;

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
