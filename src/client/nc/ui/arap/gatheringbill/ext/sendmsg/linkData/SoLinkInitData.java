package nc.ui.arap.gatheringbill.ext.sendmsg.linkData;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.pub.linkoperate.ILinkQueryData;

@SuppressWarnings("restriction")
public class SoLinkInitData extends FuncletInitData implements ILinkQueryData{
	
	private static final long serialVersionUID = 2900082892532432378L;
	
	private String pk_sms;

	public String getPk_sms() {
		return pk_sms;
	}

	public void setPk_sms(String pk_sms) {
		this.pk_sms = pk_sms;
	}

	@Override
	public String getBillID() {
		return this.getPk_sms();
	}

	@Override
	public String getBillType() {
		return null;
	}

	@Override
	public String getPkOrg() {
		return null;
	}

	@Override
	public Object getUserObject() {
		return null;
	}

}
