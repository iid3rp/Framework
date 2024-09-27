import org.lwjgl.opengl.GL40.*;
import util.Key;
import util.Map;

public class Main
{
    public static void main(String[] args)
    {
        Map<String, Integer> stuff = new Map<>();
        stuff.put("a",1);
        stuff.put("b",3);
        stuff.put("c",14);
        stuff.put("d",34);
        stuff.put("e",16);
        stuff.put("f",90);
        stuff.put("g",0);
        stuff.put("h",-4);
        stuff.put("i",0);
        stuff.put("a", -100);


        for(Key<String, Integer> key : stuff)
        {
            System.out.println(key.getValue());
        }

        System.out.println(stuff.get("g"));
    }
}
