package framework.util;

import framework.lang.Vec3;

public class OctobinarySearchTree<T>
{
    Vec3 position;

    // top parts
    private SearchTree<T> xyz;
    private SearchTree<T> _xyz;
    private SearchTree<T> xy_z;
    private SearchTree<T> _xy_z;

    // bottom parts
    private SearchTree<T> x_yz;
    private SearchTree<T> _x_yz;
    private SearchTree<T> x_y_z;
    private SearchTree<T> _x_y_z;

    public OctobinarySearchTree(Vec3 vec3)
    {
        position = vec3;
        xyz = new SearchTree<>(position);
        _xyz = new SearchTree<>(position);
        xy_z = new SearchTree<>(position);
        _xy_z = new SearchTree<>(position);
        x_yz = new SearchTree<>(position);
        _x_yz = new SearchTree<>(position);
        x_y_z = new SearchTree<>(position);
        _x_y_z = new SearchTree<>(position);
    }

    public void add(T element, Vec3 position)
    {
        SearchTree<T> st = determineOctant(position);
        st.add(new Node<>(position, element, null, null));
    }

    public void remove(Vec3 position) {
        SearchTree<T> st = determineOctant(position);
        st.remove(position);
    }

    public void clear()
    {
        xyz.clear();
        _xyz.clear();
        xy_z.clear();
        _xy_z.clear();
        x_yz.clear();
        _x_yz.clear();
        x_y_z.clear();
    }

        private SearchTree<T> determineOctant(Vec3 position)
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

    private static class Node<T>
    {
        Vec3 location;
        T value;
        Node<T> right;
        Node<T> left;

        public Node(Vec3 location, T value, Node<T> right, Node<T> left)
        {
            this.location = location;
            this.value = value;
            this.right = right;
            this.left = left;
        }

        public Node(Vec3 location, T value)
        {
            this.location = location;
            this.value = value;
        }

        public Vec3 getLocation()
        {
            return location;
        }

        public void setLocation(Vec3 location)
        {
            this.location = location;
        }

        public void setValue(T value)
        {
            this.value = value;
        }

        public Node<T> getRight()
        {
            return right;
        }

        public void setRight(Node<T> more)
        {
            this.right = more;
        }

        public Node<T> getLeft()
        {
            return left;
        }

        public void setLeft(Node<T> less)
        {
            this.left = less;
        }

        public T get()
        {
            return value;
        }
    }

    public static class SearchTree<T>
    {
        private Node<T> root;

        public SearchTree(Vec3 location)
        {
            root = new Node<>(location, null);
        }

        private void add(Node<T> node)
        {
            if (root == null)
                root = node;
            else addRecursive(root, node);
        }



        private void addRecursive(Node<T> current, Node<T> newNode) {
            if (newNode.location.lengthFrom(root.location) < current.location.lengthFrom(root.location))
                if (current.left == null)
                    current.left = newNode;
                else addRecursive(current.left, newNode);
            else if (current.right == null)
                    current.right = newNode;
            else addRecursive(current.right, newNode);
        }

        public<E> LinkList<T> inorder()
        {
            return inorder(root, new LinkList<>());
        }

        public<E> LinkList<T> preorder()
        {
            return preorder(root, new LinkList<>());
        }

        public<E> LinkList<T> postorder()
        {
            return postorder(root, new LinkList<>());
        }

        private<E> LinkList<T> inorder(Node<T> root, LinkList<T> list)
        {
            if(root != null)
            {
                list = inorder(root.getLeft(), list);
                list.addAll(root.get());
                list = inorder(root.getRight(), list);
            }
            return list;
        }
        private<E> LinkList<T> preorder(Node<T> root, LinkList<T> list)
        {
            if(root != null)
            {
                list.addAll(root.get());
                list = preorder(root.getLeft(), list);
                list = preorder(root.getRight(), list);
            }
            return list;
        }
        private LinkList<T> postorder(Node<T> root, LinkList<T> list)
        {
            if(root != null)
            {
                list = inorder(root.getLeft(), list);
                list = inorder(root.getRight(), list);
                list.addAll(root.get());
            }
            return list;
        }

        public void remove(Vec3 location) {
            root = removeRecursive(root, location);
        }

        private Node<T> removeRecursive(Node<T> current, Vec3 location) {
            if (current == null) return null;

            if (location.equals(current.location))
            {
                // Node to remove found
                if (current.left == null && current.right == null)
                    return null;
                if (current.left == null)
                    return current.right;
                if (current.right == null)
                    return current.left;

                // Two children: Replace with the smallest value in the right subtree
                Node<T> smallestValue = findSmallest(current.right);
                current.location = smallestValue.location;
                current.value = smallestValue.value;
                current.right = removeRecursive(current.right, smallestValue.location);
                return current;
            }

            if (location.lengthFrom(root.location) < current.location.lengthFrom(root.location)) {
                current.left = removeRecursive(current.left, location);
                return current;
            }

            current.right = removeRecursive(current.right, location);
            return current;
        }

        private Node<T> findSmallest(Node<T> root)
        {
            return root.left == null ? root : findSmallest(root.left);
        }

        private Node<T> findBiggest(Node<T> root)
        {
            return root.right == null ? root : findBiggest(root.right);
        }

        public void clear() {
            root = null; // other things delegate to java's built-in garbage collection.
        }

        public LinkList<T> rangeQuery(Vec3 min, Vec3 max) {
            LinkList<T> results = new LinkList<>();
            rangeQueryRecursive(root, min, max, results);
            return results;
        }

        private void rangeQueryRecursive(Node<T> node, Vec3 min, Vec3 max, LinkList<T> results)
        {
            if (node == null)
                return;
            if (node.location.isWithin(min, max))
                results.addAll(node.value);
            if (node.left != null && node.location.x > min.x)
                rangeQueryRecursive(node.left, min, max, results);
            if (node.right != null && node.location.x < max.x)
                rangeQueryRecursive(node.right, min, max, results);
        }
    }
}