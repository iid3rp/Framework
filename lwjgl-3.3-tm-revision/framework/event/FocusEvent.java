package framework.event;

import framework.entity.Entity;

import java.util.List;

public class FocusEvent
{
    public static Entity currentFocus;
    public static Entity oldFocus;

    public static void setFocus(Entity focus)
    {
        FocusEvent.currentFocus = focus;

        if(currentFocus == oldFocus)
            return;

        if(currentFocus == null)
            return;

        List<FocusListener> listeners = currentFocus.getFocusListeners();
        for(FocusListener listener : listeners)
            listener.focusGained();

        if(oldFocus != null) {
            List<FocusListener> oldListeners = oldFocus.getFocusListeners();
            for(FocusListener listener : oldListeners)
                listener.focusLost();
        }

        oldFocus = focus;
    }

    public static boolean isNotFocused()
    {
        return currentFocus != oldFocus;
    }
}
