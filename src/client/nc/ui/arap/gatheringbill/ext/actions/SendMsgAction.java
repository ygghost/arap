package nc.ui.arap.gatheringbill.ext.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import nc.md.data.access.NCObject;
import nc.ui.arap.gatheringbill.ext.sendmsg.SendMsg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.ic.m4c.entity.SaleOutVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;

/***
 * ��������NC��Ŀ-�տ�������ӡ����Ͷ��š���ť
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class SendMsgAction extends NCAction{

	private static final long serialVersionUID = -6117750199913767818L;
	
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
		model.addAppEventListener(this);
	}
	public IModelDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IModelDataManager dataManager) {
		this.dataManager = dataManager;
	}
	
	

	public SendMsgAction() {
		super();
		String btnName="���Ͷ���";
	    this.setBtnName(btnName);//��ť����
	    this.setCode("SendMsgAction");//��ť����
	    //��ť��ݼ�����
		putValue(NCAction.SHORT_DESCRIPTION, btnName+ "(ctrl+H)");//��ť����
		putValue(NCAction.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));//���ÿ�ݼ�ctrl+H
		
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
	    //��ȡѡ�е�����(����)
		Object obj=this.getModel().getSelectedData();
		if(null==obj)return;
		AggGatheringBillVO aggVO=(AggGatheringBillVO) obj;
		SendMsg sm= new SendMsg();
		sm.sendSMS(aggVO,this.editor);
		//ˢ������
		RefreshBillData();		
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
	@Override
	protected boolean isActionEnable() {
		//��ѡ���ݰ�ť������
//		return model.getUiState()==UIState.NOT_EDIT&&((BillManageModel) this.getModel()).getSelectedOperaDatas() != null
//						&& ((BillManageModel) this.getModel()).getSelectedOperaDatas() .length == 1;
		return false;//����ҵ��Ա��Ӧ��ʱ�������ɷ��Ͷ��Ű�ť�����������Ҫ����������ע�͵����д���ſ����ɣ����д���ע�͡�
	}
	
	/***
	 * Ԫ���ݲ�ѯ�ӿڷ���
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

}
