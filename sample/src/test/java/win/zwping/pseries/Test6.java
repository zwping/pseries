package win.zwping.pseries;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import win.zwping.code.constant.Direction;

public class Test6 {

    private int a = -1;

    public void setT(@Direction int b) {
        this.a = b;
        System.out.println(this.a);
    }

    @Test
    public void test() {
        try {
//            Class clazz = Test6.class;
//            Object obj = clazz.newInstance();
//            Field field = clazz.getDeclaredField("a");
//            field.setAccessible(true);
//            System.out.println(field.get(obj));
            Method[] methods = Direction.class.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println(method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
