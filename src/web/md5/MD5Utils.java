package web.md5;
 
import java.security.MessageDigest;
import java.text.DecimalFormat;

import org.apache.commons.codec.binary.Hex;
 
public class MD5Utils {
	/**
	 * MD5˫�ؽ���
	 * <p>
	 * 
	 * @Title : getconvertMD5
	 *        </p>
	 *        <p>
	 * @Description : TODO
	 *              </p>
	 *              <p>
	 * @Author : HuaZai
	 *         </p>
	 *         <p>
	 * @Date : 2017��12��26�� ����3:34:17
	 *       </p>
	 */
	public static String getconvertMD5(String inStr) {
		char[] charArray = inStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (charArray[i] ^ 't');
		}
		String str = String.valueOf(charArray);
		return str;
	}
 
	/**
	 * ʹ��Apache��Hex��ʵ��Hex(16�����ַ�����)���ֽ�����Ļ�ת
	 * <p>
	 * 
	 * @Title : md5Hex
	 *        </p>
	 *        <p>
	 * @Description : TODO
	 *              </p>
	 *              <p>
	 * @Author : HuaZai
	 *         </p>
	 *         <p>
	 * @Date : 2017��12��27�� ����11:28:25
	 *       </p>
	 */
	@SuppressWarnings("unused")
	private static String md5Hex(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new String(new Hex().encode(digest));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return "";
		}
	}
 
	/**
	 * ����MD5����
	 * <p>
	 * 
	 * @Title : getSaltMD5
	 *        </p>
	 *        <p>
	 * @Description : TODO
	 *              </p>
	 *              <p>
	 * @Author : HuaZai
	 *         </p>
	 *         <p>
	 * @Date : 2017��12��27�� ����11:21:00
	 *       </p>
	 */
	public static String getSaltMD5ofDevice(String password) {

		// �������յļ�����
		String Salt = "NiNgBo888ChUaNzHi";
		password = md5Hex(password + Salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = Salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}
	
	public static String getSaltMD5ofGate(String password) {

		// �������յļ�����
		String Salt = "NiNgBo777ChUaNzHi";
		password = md5Hex(password + Salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = Salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}
	public static void main(String[] args)
	{
		DecimalFormat nf = new DecimalFormat("00");
		for(int i=1;i<20;i++)
		{
			String aString="00000000000000"+nf.format(i);
			System.out.println("DeviceID:"+aString+" "+getSaltMD5ofDevice(aString));
		}
		for(int i=1;i<20;i++)
		{
			String aString="00000000000000"+nf.format(i);
			System.out.println("GateWayID:"+aString+" "+getSaltMD5ofGate(aString));
		}
	}
}
