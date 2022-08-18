package abe;

import it.unisa.dia.gas.jpbc.Element;

public class Debug {
    public static Element rt = null;
    public static Element ta = null;
    public static Element gh = null;
    public static Element SK_time_D0 = null;
    public static Element SK_time_D1 = null;
    public static Element SK_time_D2 = null;
    public static Element qn = null;
    public static Element qx1 = null;
    public static Element D = null;
    public static Element _D = null;
    public static Element C = null;
    public static Element _C = null;

    public static void debug(Element e1, Element e2) {
        Element e3 = e1.powZn(rt.duplicate());
        Element e4 = e2.powZn(ta.duplicate());
        Element e5 = e1.powZn(ta.duplicate());
        Element e6 = e2.powZn(rt.duplicate());
        System.out.println("v{l_c, l_c} same: " + (e3.equals(e4) || e5.equals(e6)));
    }
}
