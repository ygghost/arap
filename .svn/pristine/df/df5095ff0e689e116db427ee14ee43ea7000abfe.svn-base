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
 * 收款单管理短信发送前后台数据操作接口服务工具类
 * 
 * @author WYR
 * @Date 2017-10-21
 *
 */
public class DBserviceUtils {
	
	
	/***********客户基本信息查询接口***************/
	private  ICustBaseInfoQueryService  custservice;
	/***收款单管理短信发送功能查询及回写服务接口***/
	private  ISendMessageGatheringbillService service;
	/*******收款单查询接口*****/
	private IArapGatheringBillPubQueryService gatheQueryService;
	
	/***NC前台查询服务接口***/
	private  IUAPQueryBS iuapQueryservice;
	
	
	/**
	 * 客户基本信息查询服务接口
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
	 * 收款单管理短信发送功能查询及回写服务接口
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
	 * NC前台查询接口
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
	 * 收款单查询接口
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
	 * @param gatheQueryService 要设置的 gatheQueryService
	 */
	public void setGatheQueryService(IArapGatheringBillPubQueryService gatheQueryService) {
		this.gatheQueryService = gatheQueryService;
	}
	
	/***
	 * 查询人员基本信息
	 * 
	 * @param pk_psndoc 人员基本信息主键
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
