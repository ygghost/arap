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
 * ���ŷ�����
 * 
 * @author WYR
 * @Date 2017-10-20
 */
public class SendSMS {
	
//	private static long corpID=186930;      			//��ҵID
//	private static String loginName="ceshi";			//��¼�ʺ�
//	private static String password="325210";			//���룬MD5(CorpID+�ʺ�����+TimeStamp)
//	private static String timeStamp;					//ʱ�����MMDDHHMMSS������ʱ����),��0514094912
//	private static String addNum="";					//��չ�Ӻţ������ڶ˿ںź�
//	private static String timer = "";					//��ʱʱ�䣺yyyy-MM-dd HH:mm:ss ��:2010-05-14 10:30:00
//	private  long longSms = 1;					//�Ƿ��Գ����ŷ�ʽ����,0-��1-��
//	private static MobileListGroup[] mobileList;		//���պ����б���MobileListGroup��ɣ�Ϊ��ֹ��ʱ��ÿ���ύ���ţ����պ����������鲻Ҫ����50����
//	private static StringHolder errMsg;					//������Ϣ�����ڷ��غ������ý������������
//	private static ArrayOfSmsIDListHolder smsIDList;	//����ID�б����ڷ��ط��ͳɹ��Ķ��ż�¼ID���˶���ID������״̬����ƥ���ʶ��
//	private static LongHolder count ;					//���ú����ķ���ֵ�����Ͷ��ţ����ض���ID(SmsID)���ŷ��ͽ��������0��ʾ���ͳɹ���
//	private static String url="http://sms.mobset.com:8080/Api"; //��������ַ
	
	 
	/***
	 * ���ö��Žӿڷ��Ͷ���
	 * 
	 * 
	 * @param corpID ��ҵID
	 * @param loginName ���ŷ��ͽӿ�ϵͳ �û�
	 * @param password ���ŷ��ͽӿ�ϵͳ �û�����
	 * @param mymobiles ��Ҫ���Ͷ��ŵ��ֻ���
	 * @param mycontent ���ŷ���������Ϣ
	 * @param sendUrl ���ŷ��ͽӿ�ϵͳ��ַ
	 * @return
	 */
	public static Map<String,Object> sendMsg(long corpID, String loginName, String password,String mymobiles, String mycontent,String sendUrl) {
		Map<String,Object> obj = new HashMap<String,Object>();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");// ���Է�����޸����ڸ�ʽ
		String timeStamp = dateFormat.format(now);
		System.out.println(timeStamp);
		
		// ��ʼ������
		StringHolder errMsg = new StringHolder();
		ArrayOfSmsIDListHolder smsIDList = new ArrayOfSmsIDListHolder();
		LongHolder count = new LongHolder();

		// ���ֻ������ַ����ֽ⵽������
		String[] mobileArray = StringUtils.replace(mymobiles, "��", ";").split(";");
		MobileListGroup[] mobileList = new MobileListGroup[mobileArray.length];

		for (int i = 0; i < mobileList.length; i++) {
			mobileList[i] = new MobileListGroup();
			mobileList[i].setMobile(mobileArray[i]);
		}
		// MD5�������
		MD5 md5 = new MD5();
		password = md5.getMD5ofStr(corpID + password + timeStamp);

		try {
			URL myurl = new URL(sendUrl);
			MobsetApiSoap mymobset = DataObjectFactory.getMobsetApi(myurl);
			// ���÷��ͷ������ж����·�
			mymobset.sms_Send(corpID, loginName, password, timeStamp, "",
					"", 1, mobileList, mycontent, count, errMsg, smsIDList);
			System.out.println("���ͽ��:"+errMsg.value);
			obj.put("smsIDList", smsIDList);
			return obj;
		} catch (Exception e) {
			String errmsg="���ŷ���ʧ�ܣ�������Ϣ:"+e.getMessage();
			Logger.error(errmsg);
			e.printStackTrace();
			System.out.println(errmsg);
			//ExceptionUtils.wrappBusinessException(errmsg);
			MessageDialog.showHintDlg(null, "��ʾ", errmsg);
		}
		return null;
	}
}

