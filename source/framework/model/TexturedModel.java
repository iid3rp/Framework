package framework.model;

import framework.textures.ModelTexture;

public class TexturedModel
{
    private final Model rawModel;
    private final ModelTexture modelTexture;

    public TexturedModel(Model rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public Model getModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
