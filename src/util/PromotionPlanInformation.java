package util;

import java.util.Date;

public class PromotionPlanInformation {
	private String promotionPlanID;
    private Date promotionStartDate,promotionEndDate;
    
	public String getPromotionPlanID() {
		return promotionPlanID;
	}
	public void setPromotionPlanID(String promotionPlanID) {
		this.promotionPlanID = promotionPlanID;
	}
	public Date getPromotionStartDate() {
		return promotionStartDate;
	}
	public void setPromotionStartDate(Date promotionStartDate) {
		this.promotionStartDate = promotionStartDate;
	}
	public Date getPromotionEndDate() {
		return promotionEndDate;
	}
	public void setPromotionEndDate(Date promotionEndDate) {
		this.promotionEndDate = promotionEndDate;
	}
	public PromotionPlanInformation(String promotionPlanID,Date promotionStartDate,Date promotionEndDate){
		this.promotionPlanID=promotionPlanID;
		this.promotionStartDate=promotionStartDate;
		this.promotionEndDate=promotionEndDate;
	}
}
