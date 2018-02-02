package nc.ui.arap.gatheringbill.ext.actionInteceptors;

import javax.swing.Action;

import org.apache.commons.lang3.StringUtils;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.pub.SuperVO;
import nc.ui.uif2.editor.BillForm;
import java.awt.event.ActionEvent;
import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.vo.pub.BusinessException;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.uif2.actions.ActionInterceptor;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.md.persist.framework.MDPersistenceService;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.arap.gatheringbill.ext.sendmsg.SendMsg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
/***
 * ��������NC��Ŀ-�տ����������ť������(�б�ѡ)
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class ApproveListActionInteceptor implements ActionInterceptor {
	
	/******��Ƭ����*******/
	private BillForm editor;
	/***���ݹ���ģ��***/
	private AbstractAppModel model;
	/**��������ģ��**/
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

	@Override
	public void afterDoActionSuccessed(Action arg0, ActionEvent arg1) {
		Object[] datas =((BillManageModel) this.getModel()).getSelectedOperaDatas();
		//���������������Ͷ���,��Ϊ�Ѿ���ǩ�ֳɹ������Բ�����datasΪ�յ������
		if(datas.length>1){
			return;
		}
		AggGatheringBillVO aggVO= (AggGatheringBillVO)this.getModel().getSelectedData();
		String sendNum =aggVO.getHeadVO().getDef29();//�ѷ��Ͷ�����
		if(StringUtils.isEmpty(sendNum)){
			sendNum="0";
		}
		if(Integer.parseInt(sendNum)>0){//�Ѿ����;Ͳ��ٷ��Ͷ���
			return;
		}
		SendMsg sm= new SendMsg();
		sm.sendSMS(aggVO,this.editor);
		try {
			//ˢ������
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
	 * ˢ������(���б��뿨Ƭˢ��)
	 * 
	 * @throws Exception
	 */
	private void RefreshBillData()throws Exception {
		//�Ƿ�Ƭ
		if(this.editor.getBillCardPanel().isShowing()){
			//��Ƭˢ��(��������)
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
							// �����Ѿ���ɾ��
							throw new BusinessException("�����Ѿ���ɾ�����뷵���б���棡");
						}
					}
				}
				//ˢ����ʾ
				ShowStatusBarMsgUtil.showStatusBarMsg("ˢ�³ɹ���",model.getContext());	
			}			
		}else{//�б�
			//�б�ˢ��(����ȫ������)
			this.getDataManager().refresh();
		}
	}
	
	/***
	 * Ԫ���ݲ�ѯ�ӿڷ���
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}
	

}
