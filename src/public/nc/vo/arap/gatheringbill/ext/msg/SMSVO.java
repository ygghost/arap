package nc.vo.arap.gatheringbill.ext.msg;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * 短信发送日志VO 
 * 
 * @author WYR
 * @Date 2017-10-21
 *
 */
public class SMSVO extends SuperVO{
	
	private static final long serialVersionUID = -961236082170720227L;
	
    /***主键****/
	private String pk_sms;
	 /***客户主键****/
	private String pk_customer;
	 /***短信内容****/
	private String sms_content;
	/***组织主键****/
	private String pk_org;
	/***业务单据号****/
	private String vbillcode;
	/***短信发送结果****/
	private String sms_result;
	/***发送短信用户主键****/
	private String  sms_user;
	/***业务单据主键****/
	private String pk_bill ;
	/***业务单据类型****/
	private String billtype ;
	/***短信日志创建人(第一次发送人)****/
	private String creator;
	/***短信日志创建时间(第一次发送时间)****/
	private UFDateTime creationtime;
	/***短信日志最后修改人(最后一次发送人)****/
	private String modifier;
	/***短信日志最后修改时间(最后一次发送时间)****/
	private UFDateTime modifiedtime;
	/***时间戳****/
	private UFDateTime ts;
	/***删除标志****/
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
	 * 取得父VO主键字段
	 * 
	 * <b>创建时间 2017-10-21 11:16:30</b>
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}
	/**
	 * 返回短信发送日志表名称
	 * 
	 * <b>创建时间 2017-10-21 11:17:15</b>
	 */
	@Override
	public String getTableName() {
		return "yh_sendmsg_log";
	}
	/**
	 * 取得表主键
	 * 
	 * <b>创建时间 2017-10-21 11:16:57</b>>
	 */
	@Override
	public String getPKFieldName() {
		return "pk_sms";
	}

	public SMSVO() {
		super();
	}
	
	
}
