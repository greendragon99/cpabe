import abe.LocationKey;
import abe.MyPairUtils;
import abe.PairingManager;
import abe.SecretKey;
import abe.TimeKey;
import abe.serialize.SerializeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import ui.panel;

import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;

import java.io.FileOutputStream;

public class Test {
    public static Element rt = null;
    public static void main(String[] args) {
        SecretKey SK = new SecretKey();
        //D & D'
        Element r = MyPairUtils.getZrGenerator();
        byte[] b = SerializeUtils.convertToByteArray(SK);
        JSONObject json = new JSONObject();
        json.put("SK", b);
        String s = JSON.toJSONString(json);
        JSONObject json1 = JSON.parseObject(s);
        byte[] b1 = json1.getBytes("SK");
        SecretKey SK1 = SerializeUtils.constructFromByteArray(SecretKey.class, b1);
    }

    public static String dateToStamp(String y,String M, String d, String h, String m) throws ParseException {
        String res;
        if (y.length() != 4) {
            res = "时间格式错误";
        }
        if (M.length() != 2) {
            M = '0' + M;
        }
        if (d.length() != 2) {
            d = '0' + d;
        }
        if (h.length() != 2) {
            h = '0' + h;
        }
        if (m.length() != 2) {
            m = '0' + m;
        }
        if (Integer.valueOf(M) > 12) {
            res = "时间格式错误";
        }
        int[] day_count = {31,28,31,30,31,30,31,31,30,31,30,31};
        if (Integer.valueOf(d) > day_count[Integer.valueOf(M)-1]) {
            res = "时间格式错误";
        }
        if (Integer.valueOf(h) > 24) {
            res = "时间格式错误";
        }
        if (Integer.valueOf(m) > 60) {
            res = "时间格式错误";
        }
        String s = y + '-' + M + '-' + d + ' ' + h + ':' + m;
        System.out.println("dateToStamp s:" + s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}
