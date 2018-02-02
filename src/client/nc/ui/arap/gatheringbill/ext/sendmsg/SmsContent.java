package nc.ui.arap.gatheringbill.ext.sendmsg;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class SmsContent extends DBserviceUtils {
	/**
	 * 拼接短信发送内容
	 * 
	 * ① ""表示没有维护短信内容格式业务参数
	 * ② N表示没有权限
	 * ③ null 未查询到客户信息
	 * @param hvo 收款单表头VO
	 * @param cv 客户基本信息VO
	 * @param smsVOs 短信日志VO
	 * @return
	 */
	public String msgContent(GatheringBillVO hvo, CustomerVO cv,List<SMSVO> smsVOs) {
		//获取收款单表头 客户PK
		String pk_customer = hvo.getCustomer();
		CustomerVO[] custVOs=null;
		//金额字符串
		String strMoney="0.00";
		try {
			custVOs = getCustservice().queryDataByPkSet(new String[]{pk_customer});
		} catch (BusinessException e) {
			Logger.error(e);
			e.printStackTrace();
			return null;//表示获取客户信息为空
		}
		if(null==custVOs||custVOs.length==0){
			return null;//表示获取客户信息为空
		}
		CustomerVO customerVO=custVOs[0];
		List<String> materialIds = new ArrayList<String>();
		// 判断客户基本信息是否启用了发送短信权限
		if ("Y".equals(customerVO.getDef30())) {	
			// 收款单表头总金额(原币金额)
			double monery=0;
			//考虑有退款的负数金额订单，这里判断金额不等于0即可
			if(null!=hvo.getMoney()&&hvo.getMoney().toDouble()!=0){
				monery=hvo.getMoney().toDouble();
			}
//			//取单据日期,年月日，例如：2017-10-20
			String yymmdd=hvo.getBilldate().toString().substring(0, 10);		
			String nyr[]=yymmdd.split("-");
			//年、月、日
			String  year=nyr[0];
			String  month=nyr[1];
			String  day=nyr[2];
			String customer=customerVO.getName();

//			StringBuffer smsMsg = new StringBuffer();
//			smsMsg.append("尊敬的");
//			smsMsg.append(customerVO.getName()+"客户");
//			smsMsg.append("，您好，");
//			smsMsg.append(year+"年");
//			smsMsg.append(month+"月");
//			smsMsg.append(day+"日，");
//			smsMsg.append("收到您的货款"+getFormatMoney(strMoney)+"元，");
//			smsMsg.append("感谢您的支持！(汇账金额以实际账单显示为准)");
			
			//获取业务参数"smscnt"配置的短信格式内容%1$s为参数类型及顺序的表达式，灵活可无序配置
			String smsMsg="";
			String pkOrg =hvo.getPk_org();
			String smscnt =getService().getLimitPara(pkOrg, "smscnt");
			if(StringUtils.isEmpty(smscnt)){
				return "";//表示未设置短信格式内容参数
			}
			//format(smscnt,customer,year,month,day,monery)参数不能改变顺序
			smscnt=String.format(smscnt,customer,year,month,day,monery);
			smsMsg=smscnt;
			cv.setTel1(customerVO.getTel1());
			cv.setPk_customer(customerVO.getPk_customer());
			SMSVO smsVO =  new SMSVO();
			smsVO.setVbillcode(hvo.getBillno());
			smsVO.setPk_org(hvo.getPk_org());
			smsVO.setBilltype("F2");//业务单据类型
			smsVO.setPk_customer(customerVO.getPk_customer());
			smsVO.setSms_content(smsMsg);
			smsVO.setPk_bill(hvo.getPk_gatherbill());
			smsVO.setSms_user(AppContext.getInstance().getPkUser());
			smsVOs.add(smsVO);

			return smsMsg;
		} else {
			return "N";//表示客户基本信息自定义项30未否启用“是否发送短信”
		}
	}	
		
	/**
	 * 格式化金额字段(带,分隔符保留2位小数)
	 *
	 * @param str 数值字符串
	 * @return
	 */
	public static String getFormatMoney(String str) {
	    if (str == null||"".equals(str)) str = "0";
	    BigDecimal bd = new BigDecimal(str);
	    double fAmount = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	    String sFormat = String.format("%1$,f", fAmount);
	    if (sFormat == null) sFormat = "0.000";
	    String formatAmount = sFormat.substring(0, sFormat.lastIndexOf(".") + 3);
	    return formatAmount;
	}
	
}
