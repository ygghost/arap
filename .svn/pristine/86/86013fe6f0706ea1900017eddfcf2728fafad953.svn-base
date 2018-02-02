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
 * 粤海集团NC项目-收款单管理增加《发送短信》按钮
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class SendMsgAction extends NCAction{

	private static final long serialVersionUID = -6117750199913767818L;
	
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
		String btnName="发送短信";
	    this.setBtnName(btnName);//按钮名称
	    this.setCode("SendMsgAction");//按钮编码
	    //按钮快捷键设置
		putValue(NCAction.SHORT_DESCRIPTION, btnName+ "(ctrl+H)");//按钮描述
		putValue(NCAction.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));//设置快捷键ctrl+H
		
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
	    //获取选中的数据(单条)
		Object obj=this.getModel().getSelectedData();
		if(null==obj)return;
		AggGatheringBillVO aggVO=(AggGatheringBillVO) obj;
		SendMsg sm= new SendMsg();
		sm.sendSMS(aggVO,this.editor);
		//刷新数据
		RefreshBillData();		
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
	@Override
	protected boolean isActionEnable() {
		//多选单据按钮不可用
//		return model.getUiState()==UIState.NOT_EDIT&&((BillManageModel) this.getModel()).getSelectedOperaDatas() != null
//						&& ((BillManageModel) this.getModel()).getSelectedOperaDatas() .length == 1;
		return false;//你们业务员反应暂时屏蔽自由发送短信按钮，如果后期需要开启把上面注释的两行代码放开即可，本行代码注释。
	}
	
	/***
	 * 元数据查询接口服务
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

}
