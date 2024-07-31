package framework.entity;

import framework.event.MouseEvent;
import framework.event.MouseListener;
import framework.model.TexturedModel;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotationX, rotationY, rotationZ;
    private float scale;
    private int textureAtlasIndex;
    private List<MouseListener> listeners = new ArrayList<>();
    private Color mouseColor = new Color(0, 0, 0);

    public Entity(TexturedModel texturedModel, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;

    }

    public Entity(TexturedModel texturedModel, int textureAtlasIndex, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
        this.textureAtlasIndex = textureAtlasIndex;
    }

    public Entity(Entity entity)
    {
        this.texturedModel = new TexturedModel(entity.getTexturedModel().getModel(), entity.texturedModel.getTexture());
        this.position = new Vector3f(entity.getPosition());
        this.rotationX = entity.getRotationX();
        this.rotationY = entity.getRotationY();
        this.rotationZ = entity.getRotationZ();
        this.scale = entity.getScale();
        this.textureAtlasIndex = entity.textureAtlasIndex;
        this.listeners = entity.listeners;
        this.mouseColor = entity.mouseColor;
    }

    public void transformPosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void transformRotation(float dx, float dy, float dz) {
        this.rotationX += dx;
        this.rotationY += dy;
        this.rotationZ += dz;
    }

    public float getTextureXOffset() {
        int column = textureAtlasIndex % texturedModel.getTexture().getNumberOfRows();
        return (float) column / (float) texturedModel.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureAtlasIndex / texturedModel.getTexture().getNumberOfRows();
        return (float) row / (float) texturedModel.getTexture().getNumberOfRows();
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void transformPosition(Vector3f vec3)
    {
        transformPosition(vec3.x, vec3.y, vec3.z);
    }

    public void addMouseListener(MouseListener listener)
    {
        if(!listeners.isEmpty()) {
            if(!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
        else
        {
            MouseEvent.addMouseListener(this);
            listeners.add(listener);
        }

    }

    public List<MouseListener> getMouseListeners()
    {
        return listeners;
    }

    public void setMouseColor(Color color)
    {
        this.mouseColor = color;
    }

    public Color getMouseColor()
    {
        return mouseColor;
    }
}