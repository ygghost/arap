package nc.ui.arap.gatheringbill.ext.sendmsg.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWindowLauncher;
import nc.ui.arap.gatheringbill.ext.sendmsg.linkData.SoLinkInitData;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.ui.pub.linkoperate.ILinkType;
import nc.vo.arap.gatheringbill.ext.msg.SMSVO;
import nc.vo.pub.SuperVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.tmpub.util.DialogTool;

/**
 * 查看短信记录对话框
 * 
 * @author WYR
 * @Date 2017-10-20
 *
 */
public class QuerySmsRecordDialog extends UIDialog {
	
	private static final long serialVersionUID = 2412207816673824481L;
	
	private String code="20060MSG";//短信查看记录单据模板编码
	private String pk = null;
	private FuncRegisterVO registerVO;
	private Component invoker;
	
	private SuperVO[] m_allData = null;
	private SoLinkInitData data = null;
	private  FuncletInitData initData;
	// 上级卡片
	private BillCardPanel parentCardPanel = null;
	private BillCardPanel billCardPanel = null;
	private String m_sLoginCorp = null;
	// 操作员ID
	private String m_sOperator = null;
	// 按钮面板
	private UIPanel m_panelSouth = null;
	// UIDialog的面板
	private JPanel m_dialogContentPane = null;
	// 填充面板
	private UIPanel m_pnlFillSpaceWest = null;
	private UIPanel m_pnlFillSpaceEast = null;
	private boolean m_closeMark = false;


	public QuerySmsRecordDialog() {
		super();
		initDialog();
	}
	
	public QuerySmsRecordDialog(Container paContainer,
			SuperVO[] m_allData, FuncRegisterVO registerVO,
			Component invoker, SoLinkInitData data, FuncletInitData initData) {
		initDialog();
		this.registerVO = registerVO;
		this.invoker = invoker;
		this.data = data;
		this.initData = initData;
		this.m_allData = m_allData;
		initData(m_allData);
	}

	private void initData(SuperVO[] m_allData) {
		// TODO Auto-generated method stub
		if (m_allData == null) {
			return;
		}
		List<SMSVO> m_allDatanew = new ArrayList<SMSVO>();
		for( int i = 0 ; i <  m_allData.length ; i++){
			SMSVO vo = (SMSVO) m_allData[i];
			m_allDatanew.add(vo);
		}
		
		getBillCardPanel().getBillModel().setBodyDataVO(m_allDatanew.toArray(new SMSVO[0]));
		getBillCardPanel().setEnabled(true);
		getBillCardPanel().getBillModel().loadLoadRelationItemValue();
		getBillCardPanel().getBillModel().execLoadFormula();
	}

	private void initDialog() {
		// TODO 自动生成的方法存根
		setName("ATPOrderDialog");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("查看短信记录");
		setBounds(450, 200, 1360, 300);
		setResizable(true);
		setReset(true);
		setContentPane(getM_dialogContentPane());

	}


	public UIPanel getM_panelSouth() {
		if (m_panelSouth == null) {
			m_panelSouth = new UIPanel();
			m_panelSouth.setName("PnlSouth");
		}
		return m_panelSouth;
	}

	public JPanel getM_dialogContentPane() {
		if (m_dialogContentPane == null) {
			m_dialogContentPane = new JPanel();
			m_dialogContentPane.setName("UIDialogContentPane");
			m_dialogContentPane.setLayout(new BorderLayout());
			// 按钮Panel
			m_dialogContentPane.add(getM_panelSouth(), "South");
			// 公司panel
			m_dialogContentPane.add(getBillCardPanel(), "Center");
			// 边界填充panle
			m_dialogContentPane.add(getM_pnlFillSpaceWest(), "West");
			m_dialogContentPane.add(getM_pnlFillSpaceEast(), "East");

		}
		return m_dialogContentPane;
	}

	public UIPanel getM_pnlFillSpaceEast() {
		if (m_pnlFillSpaceEast == null) {
			m_pnlFillSpaceEast = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceEast.setName("PnlFillSpaceEast");
			m_pnlFillSpaceEast.setPreferredSize(new Dimension(15, 10));
		}
		return m_pnlFillSpaceEast;
	}

	public BillCardPanel getBillCardPanel() {
		if (billCardPanel == null) {
			billCardPanel = new BillCardPanel();
			// 加载模板
			billCardPanel.loadTemplet(code, null, m_sOperator, m_sLoginCorp);
			// 加载数值 测试

			//
//			billCardPanel.addBodyEditListener2(this);
//			billCardPanel.addBodyMouseListener(this);//表体鼠标事件监听
//			billCardPanel.addMouseListener(this);// 添加鼠标监听
//			billCardPanel.addEditListener(this);
//			billCardPanel.addBillEditListenerHeadTail(this);
			billCardPanel.setBodyShowThMark(true);
			billCardPanel.setEnabled(true);
			billCardPanel.getBillTable().setRowSelectionAllowed(true);
		}
		return billCardPanel;
	}

	public UIPanel getM_pnlFillSpaceWest() {
		if (m_pnlFillSpaceWest == null) {
			m_pnlFillSpaceWest = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceWest.setName("PnlFillSpaceWest");
			m_pnlFillSpaceWest.setPreferredSize(new Dimension(15, 16));
		}
		return m_pnlFillSpaceWest;
	}

//	public UIButton getM_btnOK() {
//		if (m_btnOK == null) {
//			m_btnOK = new UIButton();
//			m_btnOK.setName("BtnOK");
//			HotkeyUtil.setHotkeyAndText(m_btnOK, 'O', "查看详情");
//			m_btnOK.addActionListener(this);
//		}
//		return m_btnOK;
//	}

//	public UIButton getM_btnCancel() {
//		if (m_btnCancel == null) {
//			m_btnCancel = new UIButton();
//			m_btnCancel.setName("BtnCancel");
//			HotkeyUtil.setHotkeyAndText(m_btnCancel, 'C', "取消");
//			m_btnCancel.addActionListener(this);
//		}
//		return m_btnCancel;
//	}


//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == this.getM_btnOK()) {
//
//			int row = getBillCardPanel().getBillTable().getSelectedRow();
//
//			this.setPk(getM_allData()[row].getPrimaryKey());
//			if (getPk() != null) {
//
//				getData().setPk_wr(getPk());
//				getInitData().setInitData(getData());
//				getInitData().setInitType(ILinkType.LINK_TYPE_QUERY);
//				openNode();
////				this.closeOK();
//
//			}else{
//				MessageDialog.showErrorDlg(null, "提示", "请选择一张单据");
//			}
//
//		}
//		if (e.getSource() == this.getM_btnCancel()) {
//			this.m_closeMark = false;
//			this.closeCancel();
//		}
//
//	}




	/**
	 * 打开节点
	 */
	public void openNode() {
		FuncletWindowLauncher
				.openFuncNodeForceModalDialog(getInvoker(), getRegisterVO(),
						getInitData(), null, true,
						DialogTool.getWindowOfScreenCenter());
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public SuperVO[] getM_allData() {
		return m_allData;
	}

	public void setM_allData(SuperVO[] m_allData) {
		this.m_allData = m_allData;
	}

	public FuncRegisterVO getRegisterVO() {
		return registerVO;
	}

	public void setRegisterVO(FuncRegisterVO registerVO) {
		this.registerVO = registerVO;
	}

	public Component getInvoker() {
		return invoker;
	}

	public void setInvoker(Component invoker) {
		this.invoker = invoker;
	}


	public FuncletInitData getInitData() {
		return initData;
	}

	public void setInitData(FuncletInitData initData) {
		this.initData = initData;
	}

}
