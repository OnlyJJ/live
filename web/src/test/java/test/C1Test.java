package test;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lm.live.common.constant.MCPrefix;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.MemcachedUtil;


public class C1Test {
	@Test
	public void test() {
		 String url=  "";
		 url=  "http://127.0.0.1:8080/car/42/";
		 JSONObject  json = new JSONObject();
		 JSONObject  kind = new JSONObject();
		 JSONObject  page = new JSONObject();
		 kind.put("a", 5003);
		 kind.put("b", 1);
		 page.put("b", 1);
		 page.put("c", 36);
		 json.put("kind", kind);
		 json.put("page", page);
		  
		  System.out.println("#####str:"+json.toString());
		 try {
			String strRes = HttpUtils.post(url, json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		  System.out.println("#####strRes:"+strRes);
	}
}
