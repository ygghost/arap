package nc.ui.arap.gatheringbill.ext.sendmsg;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.holders.LongHolder;
import javax.xml.rpc.holders.StringHolder;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.tempuri.MobileListGroup;
import org.tempuri.MobsetApiSoap;
import org.tempuri.holders.ArrayOfSmsIDListHolder;
import com.mobset.factory.DataObjectFactory;
import com.util.MD5;
import com.util.StringUtils;

/**
 * 短信发送类
 * 
 * @author WYR
 * @Date 2017-10-20
 */
public class SendSMS {
	
//	private static long corpID=186930;      			//企业ID
//	private static String loginName="ceshi";			//登录帐号
//	private static String password="325210";			//密码，MD5(CorpID+帐号密码+TimeStamp)
//	private static String timeStamp;					//时间戳，MMDDHHMMSS（月日时分秒),如0514094912
//	private static String addNum="";					//扩展子号，附加于端口号后。
//	private static String timer = "";					//定时时间：yyyy-MM-dd HH:mm:ss 如:2010-05-14 10:30:00
//	private  long longSms = 1;					//是否以长短信方式发送,0-否；1-是
//	private static MobileListGroup[] mobileList;		//接收号码列表，由MobileListGroup组成，为防止超时，每次提交短信，接收号码数量建议不要超过50个。
//	private static StringHolder errMsg;					//错误信息，用于返回函数调用结果的文字描述
//	private static ArrayOfSmsIDListHolder smsIDList;	//短信ID列表，用于返回发送成功的短信记录ID，此短信ID可用于状态报告匹配的识别。
//	private static LongHolder count ;					//调用函数的返回值：发送短信，返回短信ID(SmsID)短信发送结果，大于0表示发送成功过
//	private static String url="http://sms.mobset.com:8080/Api"; //服务器地址
	
	 
	/***
	 * 调用短信接口发送短信
	 * 
	 * 
	 * @param corpID 企业ID
	 * @param loginName 短信发送接口系统 用户
	 * @param password 短信发送接口系统 用户密码
	 * @param mymobiles 需要发送短信的手机号
	 * @param mycontent 短信发送内容信息
	 * @param sendUrl 短信发送接口系统地址
	 * @return
	 */
	public static Map<String,Object> sendMsg(long corpID, String loginName, String password,String mymobiles, String mycontent,String sendUrl) {
		Map<String,Object> obj = new HashMap<String,Object>();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");// 可以方便地修改日期格式
		String timeStamp = dateFormat.format(now);
		System.out.println(timeStamp);
		
		// 初始化参数
		StringHolder errMsg = new StringHolder();
		ArrayOfSmsIDListHolder smsIDList = new ArrayOfSmsIDListHolder();
		LongHolder count = new LongHolder();

		// 将手机号码字符串分解到数组中
		String[] mobileArray = StringUtils.replace(mymobiles, "；", ";").split(";");
		MobileListGroup[] mobileList = new MobileListGroup[mobileArray.length];

		for (int i = 0; i < mobileList.length; i++) {
			mobileList[i] = new MobileListGroup();
			mobileList[i].setMobile(mobileArray[i]);
		}
		// MD5密码加密
		MD5 md5 = new MD5();
		password = md5.getMD5ofStr(corpID + password + timeStamp);

		try {
			URL myurl = new URL(sendUrl);
			MobsetApiSoap mymobset = DataObjectFactory.getMobsetApi(myurl);
			// 调用发送方法进行短信下发
			mymobset.sms_Send(corpID, loginName, password, timeStamp, "",
					"", 1, mobileList, mycontent, count, errMsg, smsIDList);
			System.out.println("发送结果:"+errMsg.value);
			obj.put("smsIDList", smsIDList);
			return obj;
		} catch (Exception e) {
			String errmsg="短信发送失败，错误信息:"+e.getMessage();
			Logger.error(errmsg);
			e.printStackTrace();
			System.out.println(errmsg);
			//ExceptionUtils.wrappBusinessException(errmsg);
			MessageDialog.showHintDlg(null, "提示", errmsg);
		}
		return null;
	}
}

