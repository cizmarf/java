package reflection;


import java.lang.reflect.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String in = input.nextLine();
            try {
                Class c = Class.forName(in);
                String [] cS = c.toString().split(" ");
                TypeVariable[] cTv = c.getTypeParameters();

                // name
                System.out.println(cS[1]);

                // type
                if (c.isAnnotation())
                    System.out.println("annotation");
                else if (c.isEnum())
                    System.out.println("enum");
                else if (c.isInterface())
                    System.out.println("interface");
                else
                    System.out.println("class");

                // generic
                System.out.print("Generic: ");
                if (cTv.length == 0) {
                    System.out.println("no");
                }else {
                    System.out.print("yes, Variables:");
                    for (TypeVariable e : cTv) {
                        System.out.print(" " + e);
                    }
                    System.out.println();
                }

                // ancesor classes
                if (c.getSuperclass() == null)
                    System.out.println(c.getSuperclass());
                else
                    System.out.println(c.getSuperclass().toString().split(" ")[1]);

                // interfaces inherited
                Class[] cC = c.getInterfaces();
                System.out.println(cC.length);
                for (Class cI: cC) {
                    System.out.println(cI.toString().split(" ")[1]);
                }

                // private methods
                System.out.println(c.getMethods().length);

                // statit methods
                int publicStaticMethods = 0;
                for (Method method : c.getMethods()) {
                    if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                        ++publicStaticMethods;
                    }
                }
                System.out.println(publicStaticMethods);

                // nested classes
                Class[] cIc = c.getClasses();
                System.out.println(cIc.length);
                for (Class cIce: cIc) {
                    System.out.println(cIce.toString().split(" ")[1]);
                }

            }catch (ClassNotFoundException cnfe){
                System.out.println(in + " does not exist");
            }
            System.out.println();
        }
    }
}

//java.util.Map
//java.lang.String