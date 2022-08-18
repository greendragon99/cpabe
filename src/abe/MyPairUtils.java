package abe;

import abe.serialize.SerializeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import ui.panel;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Random;

public class MyPairUtils {
    public static final int NUM_PRIMES = 4;
    public static final int BITS = 64;
    public static Pairing pairing = PairingFactory.getPairing("a1.properties");
    public static PairingParameters parameters = PairingFactory.getPairingParameters("a1.properties");
    public static final Element PHI = ElementUtils.getGenerator(pairing,
            pairing.getG1().newRandomElement().getImmutable(), parameters, 0, NUM_PRIMES).getImmutable();

    private static void projectPairingInit() {
        //用来生成a1.properties,作为运行时的双线性参数使用
        TypeA1CurveGenerator generator = new TypeA1CurveGenerator(NUM_PRIMES, BITS);
        PairingParameters parameters = generator.generate();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("a1.properties");
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append(parameters.toString());
            writer.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUp() {
    }

    public static Element getG1Generator() {
        return pairing.getG1().newRandomElement().getImmutable();
    }

    public static Element getG1SubgroupGenerator(int index) {
        return ElementUtils.getGenerator(pairing, pairing.getG1().newRandomElement().getImmutable(),
                parameters, index, NUM_PRIMES);
    }

    public static Element getG2Generator() {
        return pairing.getG2().newRandomElement().getImmutable();
    }

    public static Element getG2SubgroupGenerator(int index) {
        return ElementUtils.getGenerator(pairing, pairing.getG2().newRandomElement().getImmutable(),
                parameters, index, NUM_PRIMES);
    }

    public static Element getGTGenerator() {
        return pairing.getGT().newRandomElement().getImmutable();
    }

    public static Element getZrGenerator() {
        return pairing.getZr().newRandomElement().getImmutable();
    }

    public static Element getG1FromByte(byte[] bytes) {
        return pairing.getG1().newElementFromBytes(bytes);
    }

    public static Element getG2FromByte(byte[] bytes) {
        return pairing.getG2().newElementFromBytes(bytes);
    }

    public static Element getZrFromBytes(byte[] bytes) {
        return pairing.getZr().newElementFromBytes(bytes);
    }

    public static Element pairing(Element element1, Element element2) {
        return pairing.pairing(element1, element2);
    }

    public static Element getZrFromInteger(int i) {
        return pairing.getZr().newElement().set(i);
    }

    public static Element MRDFFunction(Element exp, Element lambda, Element mu,
                                       String max, String start, String end) {
        Element lambda_exp = lambda.duplicate().pow(new BigInteger(start));
        Element mu_exp = mu.duplicate().pow(new BigInteger(max).subtract(new BigInteger(end)));
        Element ans = PHI.duplicate().powZn(exp);
        ans = ans.duplicate().powZn(lambda_exp.duplicate().mul(mu_exp));
        return ans;
    }

    public static Element MRDFNarrow(Element ori, Element lambda, Element mu,
                                     String begin, String end, String point) {
        Element lambda_exp = lambda.duplicate().pow(new BigInteger(point).subtract(new BigInteger(begin)));
        Element mu_exp = mu.duplicate().pow(new BigInteger(end).subtract(new BigInteger(point)));
        Element ans = ori.duplicate().powZn(lambda_exp.duplicate().mul(mu_exp));
        return ans;
    }

    public static void main(String[] args) {
        String id = "123";
        SecretKey SK = new SecretKey();
        PublicKey PK = new PublicKey();
        MasterKey MK = new MasterKey();
        String time = "1660614944000";
        String begin_time = "1643738520000";
        String end_time = "1662685740000";
        Policy p = new Policy();
        String attr = "time";

        Element alpha  = MyPairUtils.getZrGenerator();
        MK.alpha = alpha;
        PK.g = MyPairUtils.getG1SubgroupGenerator(1).getImmutable();
        PK.y = MyPairUtils.getG2Generator().getImmutable();
        MK.beta = MyPairUtils.getZrGenerator();
        MK.g_alpha = PK.g.duplicate().powZn(alpha);
        PK.h = PK.y.duplicate().powZn(MK.beta);
        PK.egy_alpha = MyPairUtils.pairing(MK.g_alpha.duplicate(), PK.y);
        PK.lambda = MyPairUtils.getZrGenerator();
        PK.mu = MyPairUtils.getZrGenerator();

        Element r = MyPairUtils.getZrGenerator();
        Element g_r = PK.g.duplicate().powZn(r);
        Element g_alpha = PK.g.duplicate().powZn(MK.alpha);
        SK.D = g_alpha.duplicate().mul(g_r);
        Element idHash = MyPairUtils.getG1FromByte(id.getBytes());
        idHash = idHash.duplicate().powZn(r);
        Element beta_inv = MK.beta.duplicate().invert();
        SK.D = SK.D.duplicate().powZn(beta_inv);
        idHash = idHash.duplicate().powZn(beta_inv);
        SK.D = SK.D.duplicate().mul(idHash);
        SK._D = PK.y.duplicate().powZn(r);
        Element gh = MyPairUtils.getG1FromByte(id.getBytes());
        gh = gh.duplicate().mul(PK.g);
        gh = gh.duplicate().powZn(r);

        Element m = MyPairUtils.getGTGenerator();
        Element s = MyPairUtils.getZrGenerator();
        Ciphertext ciphertext = new Ciphertext();
        ciphertext.p = p;
        //此处m.duplicate()是为了后面AES加密中还需要用到m
        ciphertext.C = m.duplicate().mul(PK.egy_alpha.duplicate().powZn(s));
        ciphertext._C = PK.h.duplicate().powZn(s);
        Element u = ciphertext.C.duplicate().mul(pairing(gh.duplicate(), PK.y.duplicate().powZn(s)));
        Element d = pairing(SK.D, ciphertext._C);
        d.invert();
        Element ans = u.duplicate().mul(d);
        System.out.println(ans.isEqual(m));
    }
}
