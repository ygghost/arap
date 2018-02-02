package nc.ui.arap.gatheringbill.ext.sendmsg.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.print.version55.print.template.cell.style.Border;
import nc.ui.pub.print.version55.util.DoublePoint;

/***
 * 发送短信弹出的短信发送内容窗口
 * 
 * @author WYR
 * @Date 2017-10-20
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class CustomerDialog  extends UIDialog  {
	
	private static final long serialVersionUID = 7892536955918700879L;
	
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private UIButton OkButton;
	private UIButton cancelButton;
	private UITextArea  content;
	private UIButton updateButton;
	private JPanel panel1;
	private JPanel panel2;
	public static FontMetrics fm = null;
	public static int size_width = 400;
	public static int size_height = 250;
	public static int bound_width = 286;
	public static int bound_height = 170;
	private String smsContent = null;
	/**
	 * 点击确定按钮返回多行文本框的内容，点击取消返回空字符(""),
	 * 可以根据返回值来确定点击的是确定按钮还是取消按钮
	 * @param content  设置默认值的短信
	 * @return  
	 */
	public static  Object  showInputConfirm(String content){
		final CustomerDialog  cdg = new CustomerDialog();
		cdg.getContent().setText(content);
		cdg.init();
		return cdg.smsContent;
	}
	
	public void init(){
		setLayout(new BorderLayout(10, 10));
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getBottomPanel(), BorderLayout.SOUTH);
		setSize(size_width, size_height);
		setDefaultCloseOperation(2);
		setTitle("短信内容");
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		setVisible(true);
	}

	public JPanel getCenterPanel() {
		if(centerPanel == null){
			centerPanel = new JPanel();
		}
		centerPanel.add(getContent());
		return centerPanel;
	}
	public JPanel getBottomPanel() {
		if(bottomPanel == null){
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new BorderLayout(10,10));
		}
		bottomPanel.add(getPanel1(),BorderLayout.WEST);
		bottomPanel.add(getPanel2(),BorderLayout.EAST);
		return bottomPanel;
	}
	
	public JPanel getPanel1() {
		if(panel1==null){
			panel1= new JPanel();
			
		}
		panel1.add(getUpdateButton());
		return panel1;
	}


	public JPanel getPanel2() {
		if(panel2==null){
			panel2= new JPanel();
		}
		panel2.add(getOkButton());
		panel2.add(getCancelButton());
		return panel2;
	}

	public void setPanel2(JPanel panel2) {
		this.panel2 = panel2;
	}

	public UIButton getOkButton() {
		if(OkButton==null){
			OkButton = new UIButton("确定");
			OkButton.setSize(50, 25);
			OkButton.setMargin(new Insets(10, 10, 10, 10));
		}
		OkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				smsContent = getContent().getText().trim();
				dispose();
			}
		});
		return OkButton;
	}
	public UIButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new UIButton("取消");
			cancelButton.setMargin(new Insets(10, 10, 10, 10));
		}
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				smsContent = "";
				dispose();
			}
		});
		return cancelButton;
	}
	public UITextArea getContent() {
		if(content==null){
			content = new UITextArea();
		}
		content.setRows(14);
		content.setColumns(30);
		content.setLineWrap(true);
		content.setWrapStyleWord(true);
		content.setEditable(false);
		return content;
	}

	
	public UIButton getUpdateButton() {
		if(updateButton==null){
			updateButton = new UIButton("修改");
			updateButton.setMargin(new Insets(10, 10, 10, 10));
		}
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getContent().setEditable(true);
			}
		});
		return updateButton;
	}

}
