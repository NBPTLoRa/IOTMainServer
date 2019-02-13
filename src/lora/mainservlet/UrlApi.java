package lora.mainservlet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public class UrlApi {
	public static String urltoDist(String url1, Map<String,String> data)throws Exception 
	{
		//�Ѳ���ƴ�ӵ�URL����
		url1+="?";
			for(Entry<String, String> vo : data.entrySet()){
			  	url1+=vo.getKey()+"="+vo.getValue()+"&";
			}
			  
		
		//����URL����
		URL url = new URL(url1);
		
		URLConnection connection=url.openConnection();
		
		InputStream in = connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

		//���շ�����Ӧ��Ϣ
		String response = new String();
		try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        response=new String(b,"utf-8");

		return response.toString();

	}

}
