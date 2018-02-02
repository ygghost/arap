package nc.ui.arap.gatheringbill.ext.sendmsg;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.vo.bd.psn.PsndocVO;
import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;
import nc.bs.framework.common.NCLocator;
import nc.itf.bd.cust.baseinfo.ICustBaseInfoQueryService;
import nc.pubitf.arap.gathering.IArapGatheringBillPubQueryService;
import nc.itf.arap.gatheringbill.ext.msg.ISendMessageGatheringbillService;


/***
 * �տ������ŷ���ǰ��̨���ݲ����ӿڷ��񹤾���
 * 
 * @author WYR
 * @Date 2017-10-21
 *
 */
public class DBserviceUtils {
	
	
	/***********�ͻ�������Ϣ��ѯ�ӿ�***************/
	private  ICustBaseInfoQueryService  custservice;
	/***�տ������ŷ��͹��ܲ�ѯ����д����ӿ�***/
	private  ISendMessageGatheringbillService service;
	/*******�տ��ѯ�ӿ�*****/
	private IArapGatheringBillPubQueryService gatheQueryService;
	
	/***NCǰ̨��ѯ����ӿ�***/
	private  IUAPQueryBS iuapQueryservice;
	
	
	/**
	 * �ͻ�������Ϣ��ѯ����ӿ�
	 * 
	 * @return
	 */
	public ICustBaseInfoQueryService getCustservice() {
		
		if(null==custservice){
			custservice=NCLocator.getInstance().lookup(ICustBaseInfoQueryService.class);
		}
		return custservice;
	}
	public void setCustservice(ICustBaseInfoQueryService custservice) {
		this.custservice = custservice;
	}
	/***
	 * �տ������ŷ��͹��ܲ�ѯ����д����ӿ�
	 * 
	 * @return
	 */
	public ISendMessageGatheringbillService getService() {
		if(null==service){
			 service = NCLocator.getInstance().lookup(ISendMessageGatheringbillService.class);
		}
		return service;
	}
	public void setService(ISendMessageGatheringbillService service) {
		this.service = service;
	}
	/***
	 * NCǰ̨��ѯ�ӿ�
	 * 
	 * @return
	 */
	public IUAPQueryBS getIuapQueryservice() {
		if(null==iuapQueryservice){
			iuapQueryservice=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return iuapQueryservice;
	}
	public void setIuapQueryservice(IUAPQueryBS iuapQueryservice) {
		this.iuapQueryservice = iuapQueryservice;
	}
	
	/**
	 * �տ��ѯ�ӿ�
	 * 
	 * @return gatheQueryService
	 */
	public IArapGatheringBillPubQueryService getGatheQueryService() {
		if(gatheQueryService==null){
			gatheQueryService=NCLocator.getInstance().lookup(IArapGatheringBillPubQueryService.class);
		}
		return gatheQueryService;
	}

	/**
	 * @param gatheQueryService Ҫ���õ� gatheQueryService
	 */
	public void setGatheQueryService(IArapGatheringBillPubQueryService gatheQueryService) {
		this.gatheQueryService = gatheQueryService;
	}
	
	/***
	 * ��ѯ��Ա������Ϣ
	 * 
	 * @param pk_psndoc ��Ա������Ϣ����
	 * @return
	 * @throws BusinessException 
	 */
	public PsndocVO getPsndocVOInfo(String pk_psndoc) {
		try {
			PsndocVO psndocVO = (PsndocVO) getIuapQueryservice().retrieveByPK(PsndocVO.class, pk_psndoc);
			return psndocVO;
		} catch (BusinessException e) {
			Logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	

}
