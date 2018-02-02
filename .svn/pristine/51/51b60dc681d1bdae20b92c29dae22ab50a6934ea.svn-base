package nc.imp.arap.gatheringbill.ext.msg;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.itf.arap.gatheringbill.ext.msg.ISendMessageGatheringbillService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
/***
 * 收款单管理短信发送功能查询及回写服务接口实现类
 * 
 * @author WYR
 * @Date 2017-10-21
 */
public class SendMessageGatheringbillServiceImpl implements ISendMessageGatheringbillService{		
	
	private BaseDAO dao;
	
	public BaseDAO getDao() {
		if(null==dao){
			dao=new BaseDAO();
		}
		return dao;
	}

	public void setDao(BaseDAO dao) {
		this.dao = dao;
	}

	/***
	 * 业务参数查询
	 * 
	 * @param pk_org 组织
	 * @param initCode 业务参数代码
	 * @return
	 */
	@Override
	public String getLimitPara(String pk_org, String initCode){
		String privilegeSql = getLimitParaSql(pk_org, initCode);
		try {
			Object value =getDao().executeQuery(privilegeSql, new ColumnProcessor());
			if(value==null){
				return null;
			}
			return value.toString();
		} catch (DAOException e) {
			Logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**拼接查询业务参数SQL
	 * 
	 * @param pk_org 组织
	 * @param initCode 业务参数代码
	 * @return
	 */
	public String getLimitParaSql(String pk_org,String initCode){
		SqlBuilder sql = new SqlBuilder();
		sql.append("select value from pub_sysinit where dr =0  and   ");
		sql.append("pk_org",pk_org);
		sql.append("  and  ");
		sql.append("initcode",initCode);
		return sql.toString();
	}
	
	/***
	 * 回写成功发送短信数量
	 * 
	 * @param pk_bill 收款单主键
	 * @param num 发送短信次数
	 * @throws BusinessException
	 */
	@Override
	public void updateSendNum(String pk_bill, String num)throws BusinessException {
		SqlBuilder sql = new SqlBuilder();
		sql.append("update ar_gatherbill  set ");
		sql.append("def29",num);
		sql.append("  where nvl(dr,0)=0 and ");
		sql.append("pk_gatherbill ",pk_bill);
		BaseDAO baseDao = new BaseDAO();
		baseDao.executeUpdate(sql.toString());
	}
	
	/***
	 * 短信日志记录
	 * 
	 * @param listMsglog
	 * @throws BusinessException
	 */
	public void sMSVOInfoDBServices(List<SMSVO> listMsglog) throws BusinessException{
		//当前服务器时间
		UFDateTime currentTime=AppContext.getInstance().getServerTime();
		//当前登录用户
		String pk_user=AppContext.getInstance().getPkUser();

		List<SMSVO> listInsert=new ArrayList<SMSVO>();
		List<SMSVO> listUpate=new ArrayList<SMSVO>();
		if(null==listMsglog||listMsglog.size()==0) return;
		for (SMSVO smsvo : listMsglog) {
			//判断改收款单是否已经发送过短信
			SMSVO oldsmsvo=QuerySMSVOInfo(smsvo.getPk_bill());
			if(null!=oldsmsvo){	
				SMSVO smsVO=(SMSVO) smsvo.clone();
				smsVO.setPk_sms(oldsmsvo.getPk_sms());
				smsVO.setCreator(oldsmsvo.getCreator());
				smsVO.setCreationtime(oldsmsvo.getCreationtime());
				smsVO.setModifier(pk_user);
				smsVO.setModifiedtime(currentTime);
				smsVO.setDr(0);
				smsVO.setTs(currentTime);
				listUpate.add(smsVO);
			}else{
				SMSVO smsVO=(SMSVO) smsvo.clone();
				smsVO.setPk_sms(smsvo.getPk_bill());
				smsVO.setCreator(pk_user);
				smsVO.setCreationtime(currentTime);
				smsVO.setDr(0);
				smsVO.setTs(currentTime);
				listInsert.add(smsVO);
			}
		}
		if(listInsert.size()==0&&listUpate.size()==0){
			String msg="保存短信日志出现错误!";
			ExceptionUtils.wrappBusinessException(msg);
		}
		if(listUpate.size()>0){
			getDao().updateVOList(listUpate);
		}
		if(listInsert.size()>0){
			getDao().insertVOList(listInsert);
		}
		
	}
	
	/***
	 * 查询短信记录(方便判断是更新或插入操作)
	 * 
	 * @param pk 短信记录日志表主键
	 * @return
	 * @throws BusinessException
	 */
	private SMSVO QuerySMSVOInfo(String pk)throws BusinessException{
		String sql="select * from YH_SENDMSG_LOG where nvl(dr,0)=0 and pk_bill='"+pk+"'";
		SMSVO smsVO=null;
		Object obj =getDao().executeQuery(sql, new BeanProcessor(SMSVO.class));
		if(null!=obj){
			smsVO=(SMSVO) obj;
		}
		return  smsVO;
	}



}
