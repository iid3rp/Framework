package framework.util;

import framework.lang.Vec3;

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

    public OctList()
    {
        xyz = new LinkList<>();
        _xyz = new LinkList<>();
        xy_z = new LinkList<>();
        _xy_z = new LinkList<>();
        x_yz = new LinkList<>();
        _x_yz = new LinkList<>();
        x_y_z = new LinkList<>();
        _x_y_z = new LinkList<>();
    }

    public void setPosition(Vec3 position)
    {
        this.position = position;
    }

    public void add(T element, Vec3 position)
    {
        if(this.position == null)
            throw new IllegalArgumentException("Must set the position first before adding!");
        LinkList<T> list = determineList(position);
        list.addAll(element);
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

    public LinkList<T> getQuery(float yaw, float pitch, float fov)
    {
        LinkList<T> resultList = new LinkList<>();
        yaw = (360 - yaw) % 360;
        pitch = (360 - pitch) % 360;

        float halfFov = fov / 2;
        float minYaw = yaw - halfFov;
        float maxYaw = yaw + halfFov;
        float minPitch = pitch - halfFov;
        float maxPitch = pitch + halfFov;

        // Check each octant and add the entire list if the rotation is within bounds
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 45, 45))
//            resultList.addAll(xyz);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 315, 45))
//            resultList.addAll(_xyz);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 135, 45))
//            resultList.addAll(xy_z);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 225, 45))
//            resultList.addAll(_xy_z);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 45, 315))
//            resultList.addAll(x_yz);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 315, 315))
//            resultList.addAll(_x_yz);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 135, 315))
//            resultList.addAll(x_y_z);
//        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 225, 315))
//            resultList.addAll(_x_y_z);

        // Check each octant and add the entire list if the rotation is within bounds
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 225, 315))
            resultList.addAll(xyz);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 135, 315))
            resultList.addAll(_xyz);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 315, 315))
            resultList.addAll(xy_z);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 45, 315))
            resultList.addAll(_xy_z);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 225, 45))
            resultList.addAll(x_yz);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 135, 45))
            resultList.addAll(_x_yz);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 315, 45))
            resultList.addAll(x_y_z);
        if (isWithinOctant(minYaw, maxYaw, minPitch, maxPitch, 45, 45))
            resultList.addAll(_x_y_z);

        return resultList;
    }

    private boolean isWithinOctant(float minYaw, float maxYaw, float minPitch, float maxPitch, float octYaw, float octPitch) {
        // Check if octant's center falls within yaw and pitch bounds
        return (octYaw >= minYaw && octYaw <= maxYaw) &&
                (octPitch >= minPitch && octPitch <= maxPitch);
    }

    public void clear()
    {
        position = null;
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
