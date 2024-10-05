package framework.entity;

import framework.event.FocusListener;
import framework.event.MouseEvent;
import framework.event.MouseListener;
import framework.lang.Vector3f;
import framework.lang.Vector4f;
import framework.model.TexturedModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotationX, rotationY, rotationZ;
    private float scale;
    private int textureAtlasIndex;
    private List<MouseListener> mouseListeners = new ArrayList<>();
    private List<FocusListener> focusListeners = new ArrayList<>();
    private Color mouseColor = new Color(0, 0, 0);
    private Vector4f highlightColor = new Vector4f(0f);

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
        this.mouseListeners = entity.mouseListeners;
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
        if(!mouseListeners.isEmpty()) {
            if(!mouseListeners.contains(listener)) {
                mouseListeners.add(listener);
            }
        }
        else
        {
            MouseEvent.addMouseListener(this);
            mouseListeners.add(listener);
        }

    }
    public void addFocusListener(FocusListener listener)
    {
        if(!focusListeners.isEmpty()) {
            if(!focusListeners.contains(listener)) {
                focusListeners.add(listener);
            }
        }
        else
        {
            focusListeners.add(listener);
        }

    }

    public List<MouseListener> getMouseListeners()
    {
        return mouseListeners;
    }

    public void setMouseColor(Color color)
    {
        this.mouseColor = color;
    }

    public Color getMouseColor()
    {
        return mouseColor;
    }

    public List<FocusListener> getFocusListeners()
    {
        return focusListeners;
    }

    public void setHighlightColor(Vector4f highlightColor)
    {
        this.highlightColor = highlightColor;
    }

    public Vector4f getHighlightColor()
    {
        return highlightColor;
    }
}