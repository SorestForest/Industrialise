package ru.restudios.industrialise.other;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class REUtils {


    public static <T> T castOrNull(Class<T> clazz, Object cast){
        if (clazz.isAssignableFrom(cast.getClass())){
            return clazz.cast(cast);
        }
        return null;
    }

    public static int keepInRange(int value, int minimal, int maximal){
        if (value <= minimal) { return minimal; }
        else if (value >= maximal) { return maximal;}
        return value;
    }

    public static int[] getFrom(int from,int to){
        if (from > to) { throw new IllegalArgumentException("From is bigger than to. From: "+from+", To: "+to); }
        int[] array = new int[to];
        for (int i = from; i < array.length; i++) {
            array[i] = i;
        }
        return array;
    }


    public static TextComponent string(Object string){
        return new StringTextComponent(string.toString());
    }

    public static TextComponent localised(String key){
        return new TranslationTextComponent(key);
    }
}
