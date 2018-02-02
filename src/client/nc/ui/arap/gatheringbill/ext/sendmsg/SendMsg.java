package nc.ui.arap.gatheringbill.ext.sendmsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.arap.gatheringbill.ext.sendmsg.dialog.CustomerDialog;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uif2.LoginContext;

import org.apache.commons.lang3.StringUtils;
import org.tempuri.SmsIDGroup;
import org.tempuri.holders.ArrayOfSmsIDListHolder;
/**
 * 短信发送工具类(校验接口参数及发送短信，启用收款单表头自定义项29记录已发送短信数)
 * 
 * 
 * @author WYR
 * @Date 2017-10-21
 *
 */
public class SendMsg extends DBserviceUtils {
	public void sendSMS(AggGatheringBillVO aggVO,BillForm editor){
		
		String strMsg="";
		String detailMsg="提示";
		//是否修改过短信内容
		UFBoolean isModified=UFBoolean.FALSE;
		//修改后的短信内容
		String sms_content2="";
		//收款单表头VO
		GatheringBillVO hvo =null;
		//由于在前台拼接短信内容发送，需要获取最新的收款单VO(防止别人已经修改该单据)，这里再查询一次最新的
		AggGatheringBillVO[] gathergetVOs=null;
		try {
			gathergetVOs = getGatheQueryService().findBillByPrimaryKey(new String[]{aggVO.getHeadVO().getPk_gatherbill()});
		} catch (BusinessException e1) {
			Logger.error(e1);
			e1.printStackTrace();
			//如果未查询到最新的就直接用界面获取的收款单表头VO
			hvo = aggVO.getHeadVO();
		}
		if(gathergetVOs==null||gathergetVOs.length==0){
			//如果未查询到最新的就直接用界面获取的收款单表头VO
			hvo = aggVO.getHeadVO();
		}else{
			//否则使用最新的收款单表头VO
			GatheringBillItemVO[] bodyVOs=gathergetVOs[0].getBodyVOs();
			hvo = gathergetVOs[0].getHeadVO();
			hvo.setCustomer(bodyVOs[0].getCustomer());
			hvo.setSo_psndoc(bodyVOs[0].getSo_psndoc());
		}
		if(null!=hvo.getMoney()&&hvo.getMoney().toDouble()<0){
//			strMsg="当前收款单金额是负数不发送短信！";
//			detailMsg="提示";
//			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
			return;
		}
		String pkOrg =hvo.getPk_org();
		String isAccess = "isAccess";
		String access =getService().getLimitPara(pkOrg, isAccess);
		//校验当前组织是否有权限发送短信
		if(StringUtils.isEmpty(access)||"N".equals(access)){
//			strMsg="发送失败，当前收款单组织无发送短信权限，请检查【业务参数-组织】库存管理参数代码【isAccess】是否为是！";
//			detailMsg="提示";
//			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
			return;
		}
		String isSend=hvo.getDef30();//收款单管理表头自定义30为是否发送短信
		if(!"Y".equals(isSend)){
			strMsg="当前收款单表头【是否发送短信】未勾选！";
			detailMsg="提示";
			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
			return;
		}
		CustomerVO customerVO = new CustomerVO();
		List<SMSVO> smsVOs = new ArrayList<SMSVO>();
		//获取短信内容
		String content = new SmsContent().msgContent(hvo,customerVO,smsVOs);
		if("".equals(content)){
			strMsg="发送失败，【业务参数-组织】供应链>销售管理下参数代码【smscnt】短信内容格式未设置值！";
			detailMsg="提示";
			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
		}else if(null==content){
			strMsg="发送失败，根据收款单客户主键未查询到客户基本信息!";
			detailMsg="提示";
			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
			return;
		}else if("N".equals(content)){
			strMsg="发送失败，客户没有没有启用发送短信权限，请检查当前收款单客户【是否发送短信】字段是否已勾选！";
			detailMsg="提示";
			MessageDialog.showHintDlg(editor, detailMsg, strMsg);
			return;
		}else{
			Object obj = CustomerDialog.showInputConfirm(content);
			if(null!=obj&&!"".equals(obj.toString())){
				System.out.println("短信内容:"+obj.toString());
				//弹出发送短信面板时判断用户是否修改过短信内容
				if(!content.equals(obj.toString())){
					isModified=UFBoolean.TRUE;
					sms_content2=obj.toString();
				}
				//获取收款单管理表头业务员PK
				String pk_psndoc= hvo.getSo_psndoc(); 
				PsndocVO psndocVO =getPsndocVOInfo(pk_psndoc);
				if(null==psndocVO){
					strMsg="发送失败，未查询到业务员信息！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				String pk_org = hvo.getPk_org();
				String userName ="username";
				String pwd ="pwd";
				String smsUrl ="smsUrl";
				String smsID="corpID";
				String loginName =getService().getLimitPara(pk_org, userName);
				if(StringUtils.isEmpty(loginName)){
					strMsg="发送失败，没有维护短信平台的用户名，请联系管理员维护短信平台的用户名！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				String id = getService().getLimitPara(pk_org, smsID);
				if(StringUtils.isEmpty(id)){
					strMsg="发送失败，没有维护短信平台的企业id，请联系管理员维护短信平台的企业id！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				long corpID = Long.parseLong(id);
				String sendUrl =getService().getLimitPara(pk_org, smsUrl);
				System.out.println("url:"+sendUrl);
				if(StringUtils.isEmpty(sendUrl)){
					strMsg="发送失败，没有维护短信平台的WebService地址，请联系管理员维护短信平台的WebService地址！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				String password = getService().getLimitPara(pk_org, pwd);
				if(StringUtils.isEmpty(password)){
					strMsg="发送失败，没有维护短信平台的密码，请联系管理员维护短信平台的密码！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				//发送短信的手机号码
				String costomerTel = customerVO.getTel1();
				String mobile = psndocVO.getMobile();
//				String costomerTel ="18319980895";//文锐
//				String costomerTel ="13822569201";//国治
//				String mobile ="15975980617";//黄毓慧
//				String costomerTel ="13928054574";//
//				String costomerTel ="18825640126";//自己副号
//				String mobile ="13709681104";
				if(StringUtils.isEmpty(costomerTel)&&StringUtils.isEmpty(mobile)){
					strMsg="发送失败，客户和业务员的手机号都为空，不能发送短信！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				if(StringUtils.isEmpty(costomerTel)){
					strMsg="发送失败，客户的手机号为空，不能发送短信！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				if(StringUtils.isEmpty(mobile)){
					strMsg="发送失败，业务员的手机号空，不能发送短信！";
					detailMsg="提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
					return;
				}
				String mobiles =costomerTel+"；"+mobile;
				//开始调用接口发送短信
				//发送短信，当手机号码相同时，只发送一条短信
				Map<String, Object> sendMsg = SendSMS.sendMsg(corpID, loginName, password, mobiles, obj.toString(), sendUrl);
				ArrayOfSmsIDListHolder smsIDList  =(ArrayOfSmsIDListHolder) sendMsg.get("smsIDList");
				SmsIDGroup[] value = smsIDList.value;
				StringBuilder resultMsg = new StringBuilder();
				String def29 =hvo.getDef29();
				if(StringUtils.isEmpty(def29)){
					def29="0";
				}
				int sendNum=Integer.parseInt(def29);
				for (int i = 0,length =value.length; i<length;i++) {
					SmsIDGroup result = value[i];
					String msg= result.getSmsID()>0?"成功":"失败";
					if("成功".equals(msg)){
						sendNum+=1;
					}
					String sendMobile = result.getMobile();
					resultMsg.append(sendMobile+msg+"\n");
				}
				MessageDialog.showHintDlg(editor, "提示", "短信发送结果如下\n"+resultMsg.toString());
				
				for (SMSVO smsvo : smsVOs) {
					smsvo.setSms_result(resultMsg.toString());
					//如果收款单管理弹出短信发送内容窗口界面，修改了发送的短信内容，
					//则这里需要覆盖更新到日志表短信内容字段
					if(UFBoolean.TRUE==isModified){
						smsvo.setSms_content(sms_content2);
					}
				}
				try {
					//记录发送的短信：包括短信内容、客户、组织、单号、发送短信的用户
					getService().sMSVOInfoDBServices(smsVOs);
				} catch (Exception e) {
					String errmsg="记录已成功发送短信信息到日志表发生异常，错误信息:"+e.getMessage();
					Logger.error(errmsg);
					e.printStackTrace();
					System.out.println(errmsg);
					strMsg="发送失败，"+errmsg;
					detailMsg="错误提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
				}
				try {
					//回写收款单表头已发送短信数量字段
					getService().updateSendNum(hvo.getPk_gatherbill(), sendNum+"");
				} catch (BusinessException e) {
					String errmsg="更新收款单表头已发送短信数发生错误，错误信息:"+e.getMessage();
					Logger.error(errmsg);
					e.printStackTrace();
					System.out.println(errmsg);
					strMsg="发送失败，"+errmsg;
					detailMsg="错误提示";
					MessageDialog.showHintDlg(editor, detailMsg, strMsg);
				}
			}
		}
	}
}
