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
 * 粤海集团NC项目-收款单管理增加《查看短信记录》按钮
 * 
 * @author WYR 
 * @Date 2017-10-20
 *
 */
public class SmsRecordAction extends NCAction{
	private static final long serialVersionUID = -4559812441178803910L;

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

   
	
	public SmsRecordAction() {
		super();
		String btnName="查看短信记录";
	    this.setBtnName(btnName);//按钮名称
	    this.setCode("SmsRecordAction");//按钮编码
	    //按钮快捷键设置
		putValue(NCAction.SHORT_DESCRIPTION, btnName+ "(ctrl+j)");//按钮描述
		putValue(NCAction.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_MASK));//设置快捷键ctrl+J
		
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
	    //获取选中的数据(单条)
		Object obj=this.getModel().getSelectedData();
		if(null==obj)return;
		AggGatheringBillVO aggVO=(AggGatheringBillVO) obj;
		//已发送短信数量
		String sendNum=aggVO.getHeadVO().getDef29();
		if(StringUtils.isEmpty(sendNum)||"0".equals(sendNum.trim())){
			ShowStatusBarMsgUtil.showStatusBarMsg("该单据暂时没有已发送的短信！", 
					this.getModel().getContext());
			return;
		}
		//收款单主键
		String pk =aggVO.getHeadVO().getPk_gatherbill();
		List<SMSVO> sms = getSMSByPk(pk);
		if(null==sms||sms.size()==0){
			ShowStatusBarMsgUtil.showErrorMsg("查询该单据已发送短信记录出现异常！","提示:", this.getModel().getContext());
			return;
		}
		String funcode="20060GBM";//收款单管理功能编码
		SoLinkInitData linkdata = new SoLinkInitData();
		
		FuncRegisterVO registerVO = FuncRegisterCacheAccessor.getInstance()
				.getFuncRegisterVOByFunCode(funcode);
		FuncletInitData initData = null;
		initData = new FuncletInitData();
		
		QuerySmsRecordDialog dialog = new QuerySmsRecordDialog(this.getEditor(), sms.toArray(new SMSVO[0]), registerVO,null, linkdata, initData);
		dialog.showModal();
		
		//刷新数据
		RefreshBillData();
		
	}
	
	/**
	 * 查询收款单已经发送的短信记录
	 * 
	 * @param pk收款单主键
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
		return model.getUiState()==UIState.NOT_EDIT&&((BillManageModel) this.getModel()).getSelectedOperaDatas() != null
						&& ((BillManageModel) this.getModel()).getSelectedOperaDatas() .length == 1;
	}
	
	/***
	 * 元数据查询接口服务
	 * @return
	 */
	private IMDPersistenceQueryService getMDQueryService() {
		return MDPersistenceService.lookupPersistenceQueryService();
	}

}
