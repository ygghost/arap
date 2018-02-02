package nc.ui.arap.gatheringbill.ext.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;
import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.FuncletInitData;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.MDPersistenceService;
import nc.ui.arap.gatheringbill.ext.sendmsg.dialog.QuerySmsRecordDialog;
import nc.ui.arap.gatheringbill.ext.sendmsg.linkData.SoLinkInitData;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.query2.model.IModelDataManager;
import nc.ui.sm.power.FuncRegisterCacheAccessor;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.sm.funcreg.FuncRegisterVO;
/***
 * ��������NC��Ŀ-�տ�������ӡ��鿴���ż�¼����ť
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class SmsRecordAction extends NCAction{
	private static final long serialVersionUID = -4559812441178803910L;

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

   
	
	public SmsRecordAction() {
		super();
		String btnName="�鿴���ż�¼";
	    this.setBtnName(btnName);//��ť����
	    this.setCode("SmsRecordAction");//��ť����
	    //��ť��ݼ�����
		putValue(NCAction.SHORT_DESCRIPTION, btnName+ "(ctrl+j)");//��ť����
		putValue(NCAction.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK));//���ÿ�ݼ�ctrl+J
		
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
	    //��ȡѡ�е�����(����)
		Object obj=this.getModel().getSelectedData();
		if(null==obj)return;
		AggGatheringBillVO aggVO=(AggGatheringBillVO) obj;
		//�ѷ��Ͷ�������
		String sendNum=aggVO.getHeadVO().getDef29();
		if(StringUtils.isEmpty(sendNum)||"0".equals(sendNum.trim())){
			ShowStatusBarMsgUtil.showStatusBarMsg("�õ�����ʱû���ѷ��͵Ķ��ţ�", 
					this.getModel().getContext());
			return;
		}
		//�տ����
		String pk =aggVO.getHeadVO().getPk_gatherbill();
		List<SMSVO> sms = getSMSByPk(pk);
		if(null==sms||sms.size()==0){
			ShowStatusBarMsgUtil.showErrorMsg("��ѯ�õ����ѷ��Ͷ��ż�¼�����쳣��","��ʾ:", this.getModel().getContext());
			return;
		}
		String funcode="20060GBM";//�տ�����ܱ���
		SoLinkInitData linkdata = new SoLinkInitData();
		
		FuncRegisterVO registerVO = FuncRegisterCacheAccessor.getInstance()
				.getFuncRegisterVOByFunCode(funcode);
		FuncletInitData initData = null;
		initData = new FuncletInitData();
		
		QuerySmsRecordDialog dialog = new QuerySmsRecordDialog(this.getEditor(), sms.toArray(new SMSVO[0]), registerVO,null, linkdata, initData);
		dialog.showModal();
		
		//ˢ������
		RefreshBillData();
		
	}
	
	/**
	 * ��ѯ�տ�Ѿ����͵Ķ��ż�¼
	 * 
	 * @param pk�տ����
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<SMSVO> getSMSByPk(String pk) throws BusinessException{
		SqlBuilder sql = new SqlBuilder();
		sql.append("select * from  V_YH_SENDMSG_LOG  where ");
		sql.append("pk_bill",pk);
		IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<SMSVO> results= (List<SMSVO>) query.executeQuery(sql.toString(), new BeanListProcessor(SMSVO.class));
		return results;
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
		return model.getUiState()==UIState.NOT_EDIT&&((BillManageModel) this.getModel()).getSelectedOperaDatas() != null
						&& ((BillManageModel) this.getModel()).getSelectedOperaDatas() .length == 1;
	}
	
	/***
	 * Ԫ���ݲ�ѯ�ӿڷ���
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

}
