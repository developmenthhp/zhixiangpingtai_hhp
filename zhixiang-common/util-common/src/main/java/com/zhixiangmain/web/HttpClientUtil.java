package com.zhixiangmain.web;


import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangyun-admin
 * @Package com.zhixiangyun.controller
 * @Description: 跨域请求第三方接口
 * @author: hhp
 * @date: 2018-12-04 10:43
 * @Copyright: 2018 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HttpClientUtil {

    /**
     * 最大线程池
     */
    //public static final int THREAD_POOL_SIZE = 5;


    public static final int cache = 10 * 1024;


    /**
     * 根据url下载文件，文件名从response header头中获取
     * @param url
     * @return
     */
	/*public static String download(String url) {
		return download2(url, null);
	}*/

    public static JSONObject sendSms(String uri,JSONObject jobj,String[][] headers,Boolean isSingle){

        HttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost = new HttpPost(uri);

        JSONObject sobj = new JSONObject();

        try {

            for(int i=0;i<headers.length;i++){

                httppost.addHeader(headers[i][0], headers[i][1]);

            }

            httppost.setEntity(new StringEntity(getStringFromJson(jobj,isSingle)));

            HttpResponse response = httpclient.execute(httppost);

            System.out.println(response);
            System.out.println(getStringFromJson(jobj,isSingle));
            if (response.getStatusLine().getStatusCode() == 200) {
                //读返回数据
                String conResult = EntityUtils.toString(response
                        .getEntity());

                sobj = sobj.fromObject(conResult);


            } else {
                String err = response.getStatusLine().getStatusCode()+"";

                sobj.put("msg","发送失败:"+err);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sobj;
    }

    /**
     * 根据url下载文件，保存到filepath中
     * @param url
     * @param filepath
     * @return
     */
    public static void download2(String url, String filepath,JSONObject jobj,String[][] headers,Boolean isSingle) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);

            for(int i=0;i<headers.length;i++){

                httppost.addHeader(headers[i][0], headers[i][1]);

            }

            httppost.setEntity(new StringEntity(getStringFromJson(jobj,isSingle)));

            HttpResponse response = client.execute(httppost);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
			/*if (filepath == null)
				filepath = getFilePath(response);*/
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer=new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 获取response要下载的文件的默认路径
     * @param response
     * @return
     */
	/*public static String getFilePath(HttpResponse response) {
		String filepath = root + splash;
		String filename = getFileName(response);

		if (filename != null) {
			filepath += filename;
		} else {
			filepath += getRandomFileName();
		}
		return filepath;
	}*/
    /**
     * 获取response header中Content-Disposition中的filename值
     * @param response
     * @return
     */
	/*public static String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						//filename = new String(param.getValue().toString().getBytes(), "utf-8");
						//filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}*/
    /**
     * 获取随机文件名
     * @return
     */
	/*public static String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}*/
    /**
     * 获取response header
     * @param response
     */
	/*public static void outHeaders(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			System.out.println(headers[i]);
		}
	}*/

    public static void main(String[] args) {

		/*String url=OpslabConfig.get("SALERECORD_SHEET");

		String filepath = "D:\\test\\a.xls";
		HttpClientUtil.download2(url, filepath,);*/
    }





    private static String getStringFromJson(JSONObject adata,Boolean isSingle) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        //防止引号问题  不是数组型复杂参数
        if(isSingle){

            for(Object key:adata.keySet()){
                sb.append("\""+key+"\":\""+adata.get(key)+"\",");
                //sb.append("\""+key+"\":"+adata.get(key)+",");
            }

			/*int count = 0;

			for(Object key:adata.keySet()){

				System.out.println(count);
				if(count == 2){
					sb.append("\""+key+"\":"+adata.get(key)+",");
				}else{
					sb.append("\""+key+"\":\""+adata.get(key)+"\",");
				}
				System.out.println(sb);
				count++;
			}*/

        }else{

            for(Object key:adata.keySet()){
                //sb.append("\""+key+"\":\""+adata.get(key)+"\",");
                sb.append("\""+key+"\":"+adata.get(key)+",");
            }

        }


        String rtn = sb.toString().substring(0, sb.toString().length()-1)+"}";
        return rtn;
    }

}
