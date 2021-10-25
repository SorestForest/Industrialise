package ru.restudios.industrialise.other;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class REUtils {

    private REUtils() {}

    public static <T> T castOrNull(Class<T> clazz, Object cast){
        if (clazz == null || cast == null){ return null; }
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
        int[] array = new int[to-from];
        for (int i = 1; i < array.length+1; i++) {
            array[i-1] = to-i;
        }

        return reverse_array(array);
    }

    public static int[] reverse_array(int[] a)
    {
        int n = a.length;
        int[] dest_array = new int[n];
        int j = n;
        for (int value : a) {
            dest_array[j - 1] = value;
            j = j - 1;
        }

        return dest_array;
    }


    public static <T> T[] collapse(T[] a, T[] b){
        ArrayList<T> output = new ArrayList<>();
        output.addAll(Lists.asList(null,a));
        output.addAll(Lists.asList(null,b));
        output.remove(0);
        output.remove(a.length);
        return output.toArray(a);
    }

    public static boolean consideredTheSameItem(ItemStack p_195929_0_, ItemStack p_195929_1_) {
        if (p_195929_0_.getItem() != p_195929_1_.getItem()){ return false;}
        return ItemStack.matches(p_195929_0_, p_195929_1_);
    }
}
