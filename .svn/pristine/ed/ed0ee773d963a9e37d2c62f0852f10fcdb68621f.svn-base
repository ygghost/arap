package nc.itf.arap.gatheringbill.ext.msg;

import java.util.List;

import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;

/***
 * 收款单管理短信发送功能查询及回写服务接口
 * 
 * @author WYR
 * @Date 2017-10-21
 */
public interface ISendMessageGatheringbillService {
	/***
	 * 业务参数查询
	 * 
	 * @param pk_org 组织
	 * @param initCode 业务参数代码
	 * @return
	 */
	public String getLimitPara(String pk_org,String initCode);
	/***
	 * 回写成功发送短信数量
	 * 
	 * @param pk_bill 收款单主键
	 * @param num 发送短信次数
	 * @throws BusinessException
	 */
	public void updateSendNum(String pk_bill,String num) throws BusinessException;
	
	
	/***
	 * 短信日志记录
	 * 
	 * @param listMsglog
	 * @throws BusinessException
	 */
	public void sMSVOInfoDBServices(List<SMSVO> listMsglog) throws BusinessException;
	

	
}
