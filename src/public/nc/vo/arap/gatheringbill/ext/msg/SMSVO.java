package nc.vo.arap.gatheringbill.ext.msg;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * ���ŷ�����־VO 
 * 
 * @author WYR
 * @Date 2017-10-21
 *
 */
public class SMSVO extends SuperVO{
	
	private static final long serialVersionUID = -961236082170720227L;
	
    /***����****/
	private String pk_sms;
	 /***�ͻ�����****/
	private String pk_customer;
	 /***��������****/
	private String sms_content;
	/***��֯����****/
	private String pk_org;
	/***ҵ�񵥾ݺ�****/
	private String vbillcode;
	/***���ŷ��ͽ��****/
	private String sms_result;
	/***���Ͷ����û�����****/
	private String  sms_user;
	/***ҵ�񵥾�����****/
	private String pk_bill ;
	/***ҵ�񵥾�����****/
	private String billtype ;
	/***������־������(��һ�η�����)****/
	private String creator;
	/***������־����ʱ��(��һ�η���ʱ��)****/
	private UFDateTime creationtime;
	/***������־����޸���(���һ�η�����)****/
	private String modifier;
	/***������־����޸�ʱ��(���һ�η���ʱ��)****/
	private UFDateTime modifiedtime;
	/***ʱ���****/
	private UFDateTime ts;
	/***ɾ����־****/
	private Integer dr;
	
	
	public String getPk_sms() {
		return pk_sms;
	}

	public void setPk_sms(String pk_sms) {
		this.pk_sms = pk_sms;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getSms_content() {
		return sms_content;
	}

	public void setSms_content(String sms_content) {
		this.sms_content = sms_content;
	}
	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getSms_result() {
		return sms_result;
	}

	public void setSms_result(String sms_result) {
		this.sms_result = sms_result;
	}

	public String getSms_user() {
		return sms_user;
	}

	public void setSms_user(String sms_user) {
		this.sms_user = sms_user;
	}

	public String getPk_bill() {
		return pk_bill;
	}

	public void setPk_bill(String pk_bill) {
		this.pk_bill = pk_bill;
	}
	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public UFDateTime getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public UFDateTime getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}
	
	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	
	/**
	 * ȡ�ø�VO�����ֶ�
	 * 
	 * <b>����ʱ�� 2017-10-21 11:16:30</b>
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}
	/**
	 * ���ض��ŷ�����־������
	 * 
	 * <b>����ʱ�� 2017-10-21 11:17:15</b>
	 */
	@Override
	public String getTableName() {
		return "yh_sendmsg_log";
	}
	/**
	 * ȡ�ñ�����
	 * 
	 * <b>����ʱ�� 2017-10-21 11:16:57</b>>
	 */
	@Override
	public String getPKFieldName() {
		return "pk_sms";
	}

	public SMSVO() {
		super();
	}
	
	
}
