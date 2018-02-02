package nc.ui.arap.gatheringbill.ext.actionInteceptors;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.apache.commons.lang3.StringUtils;
import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.arap.gatheringbill.ext.sendmsg.SendMsg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
/***
 * 粤海集团NC项目-收款单管理审批按钮拦截器(卡片)
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class ApproveActionInteceptorr implements ActionInterceptor {
	
	/******卡片界面*******/
	private BillForm editor;
	/***单据管理模型***/
	private AbstractAppModel model;
	/**单据数据模型**/
	private IModelDataManager dataManager;
	
	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}
	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}
	
	

	@Override
	public boolean afterDoActionFailed(Action arg0, ActionEvent arg1,Throwable arg2) {
		return true;
	}
	
	/**
	 * 在审批通过后弹出短信内容框，让用户确认短信内容，点确定按钮后发送短信，发送短信后记录发送日志
	 */
	@Override
	public void afterDoActionSuccessed(Action arg0, ActionEvent arg1) {
		Object[] datas =((BillManageModel) this.getModel()).getSelectedOperaDatas();
		//批量签字将不发送短信,因为已经是签字成功后所以不存在datas为空的情况。
		if(datas.length>1){
			return;
		}
		AggGatheringBillVO aggVO= (AggGatheringBillVO)this.getModel().getSelectedData();
		String sendNum =aggVO.getHeadVO().getDef29();//已发送短信数
		if(StringUtils.isEmpty(sendNum)){
			sendNum="0";
		}
		if(Integer.parseInt(sendNum)>0){//已经发送就不再发送短信
			return;
		}
		SendMsg sm= new SendMsg();
		sm.sendSMS(aggVO,this.editor);
		try {
			//刷新数据
			RefreshBillData();
		} catch (Exception e) {
			Logger.error(e);
			e.printStackTrace();
		}

	}

	@Override
	public boolean beforeDoAction(Action arg0, ActionEvent arg1) {

		return true;
	}
	
	
	
	/**
	 * 刷新数据(分列表与卡片刷新)
	 * 
	 * @throws Exception
	 */
	private void RefreshBillData()throws Exception {
		//是否卡片
		if(this.editor.getBillCardPanel().isShowing()){
			//卡片刷新(单条数据)
			Object obj=model.getSelectedData();
			if(obj!=null){
				if(obj instanceof SuperVO){
					SuperVO oldVO=(SuperVO)obj;
					SuperVO newVO=getMDQueryService().queryBillOfVOByPK(oldVO.getClass(), oldVO.getPrimaryKey(), false);
					model.directlyUpdate(newVO);
				}else if(obj instanceof AggregatedValueObject){ 
					AggregatedValueObject billvo = (AggregatedValueObject) obj;
					CircularlyAccessibleValueObject parentvo = billvo.getParentVO();
					if(parentvo != null)
					{
						NCObject ncobj = getMDQueryService().queryBillOfNCObjectByPK(parentvo.getClass(), parentvo.getPrimaryKey());
						if (ncobj != null) {
							model.directlyUpdate(ncobj.getContainmentObject());
						}
						else {
							// 数据已经被删除
							throw new BusinessException("数据已经被删除，请返回列表界面！");
						}
					}
				}
				//刷新提示
				ShowStatusBarMsgUtil.showStatusBarMsg("刷新成功！",model.getContext());	
			}			
		}else{//列表
			//列表刷新(界面全部数据)
			this.getDataManager().refresh();
		}
	}
	
	/***
	 * 元数据查询接口服务
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}
	

}

