package nc.itf.arap.gatheringbill.ext.msg;

import java.util.List;

import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;

/***
 * �տ������ŷ��͹��ܲ�ѯ����д����ӿ�
 * 
 * @author WYR
 * @Date 2017-10-21
 */
public interface ISendMessageGatheringbillService {
	/***
	 * ҵ�������ѯ
	 * 
	 * @param pk_org ��֯
	 * @param initCode ҵ���������
	 * @return
	 */
	public String getLimitPara(String pk_org,String initCode);
	/***
	 * ��д�ɹ����Ͷ�������
	 * 
	 * @param pk_bill �տ����
	 * @param num ���Ͷ��Ŵ���
	 * @throws BusinessException
	 */
	public void updateSendNum(String pk_bill,String num) throws BusinessException;
	
	
	/***
	 * ������־��¼
	 * 
	 * @param listMsglog
	 * @throws BusinessException
	 */
	public void sMSVOInfoDBServices(List<SMSVO> listMsglog) throws BusinessException;
	

	
}
