package framework.model;


import framework.textures.Texture;

public class TexturedModel
{
    private final Model rawModel;
    private final Texture modelTexture;

    public TexturedModel(Model rawModel, Texture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public Model getModel() {
        return rawModel;
    }

    public Texture getTexture() {
        return modelTexture;
    }
}
