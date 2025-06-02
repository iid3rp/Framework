package Imodel;

import framework.lang.Vec3;
import framework.model.Model;

public interface TexturedModel
{
    Model getModel();

    Texture getTexture();

    void setTextureId(int textureId);

    Vec3 getPosition();

    void setPosition(Vec3 position);

    Vec3 getScale();

    void setScale(Vec3 scale);

    Vec3 getRotation();

    void setRotation(Vec3 rotation);

}
