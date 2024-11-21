package framework.util;

import framework.lang.Vec3;

import java.util.List;

public class OctList<T>
{
    Vec3 position;

    // top parts
    private LinkList<T> xyz;
    private LinkList<T> _xyz;
    private LinkList<T> xy_z;
    private LinkList<T> _xy_z;

    // bottom parts
    private LinkList<T> x_yz;
    private LinkList<T> _x_yz;
    private LinkList<T> x_y_z;
    private LinkList<T> _x_y_z;

    public OctList(Vec3 position)
    {
        this.position = position;
        xyz = new LinkList<>();
        _xyz = new LinkList<>();
        xy_z = new LinkList<>();
        _xy_z = new LinkList<>();
        x_yz = new LinkList<>();
        _x_yz = new LinkList<>();
        x_y_z = new LinkList<>();
        _x_y_z = new LinkList<>();
    }

    public void add(T element, Vec3 position)
    {
        LinkList<T> list = determineList(position);
        list.add(element);
    }

    private LinkList<T> determineList(Vec3 position)
    {
        boolean posX = position.x >= this.position.x;
        boolean posY = position.y >= this.position.y;
        boolean posZ = position.z >= this.position.z;

        // Top parts
        if (posY)
            if (posX)
                return posZ ? xyz : xy_z;
            else
                return posZ ? _xyz : _xy_z;

            // Bottom parts
        else if (posX)
            return posZ ? x_yz : x_y_z;
        else
            return posZ ? _x_yz : _x_y_z;
    }

    public void setLocation()
    {
        LinkList<T> list = new LinkList<>();
        list.add(xyz);
        list.add(xy_z);
        list.add(_xyz);
        list.add(_xy_z);
        list.add(x_yz);
        list.add(x_y_z);
        list.add(_x_yz);
        list.add(_x_y_z);

        clear();

        for(T t : list)
        {
            LinkList<T> det = determineList(position);
            det.add(t);
        }
    }

    public LinkList<T> getQuery(Vec3 rotation, float fov)
    {
        LinkList<T> list = new LinkList<>();

        float half = fov / 2;
        float minRotation = rotation.y - half;
        float maxRotation = rotation.y + half;
        float minPitch = rotation.x - half;
        float maxPitch = rotation.x + half;

        return null;
    }

    public void clear()
    {
        xyz.clear();
        xy_z.clear();
        _xyz.clear();
        _xy_z.clear();
        x_yz.clear();
        x_y_z.clear();
        _x_yz.clear();
        _x_y_z.clear();
    }

}
