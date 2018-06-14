package connexions;

import ucip.GetAccountDetails;
import ucip.GetAccumulators;
import ucip.GetBalanceAndDate;
import ucip.GetFaFList;
import ucip.GetOffers;
import ucip.GetUsageThresholdsAndCounters;
import ucip.Refill;
import ucip.UpdateBalanceAndDate;
import ucip.UpdateCommunityList;
import ucip.UpdateFaFList;
import ucip.UpdateOffer;
import ucip.UpdateServiceClass;
import ucip.UpdateSubscriberSegmentation;
import ucip.UpdateUsageThresholdsAndCounters;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import acip.AddPeriodicAccountManagementData;
import acip.DeleteAccumulators;
import acip.DeleteDedicatedAccounts;
import acip.DeleteOffer;
import acip.DeletePeriodicAccountManagementData;
import acip.DeleteSubscriber;
import acip.DeleteUsageThresholds;
import acip.GetPromotionCounters;
import acip.GetPromotionPlans;
import acip.InstallSubscriber;
import acip.LinkSubordinateSubscriber;
import acip.RunPeriodicAccountManagement;
import acip.UpdateAccumulators;
import acip.UpdatePeriodicAccountManagementData;
import acip.UpdatePromotionPlan;
import util.AccountDetails;
import util.AccumulatorInformation;
import util.BalanceAndDate;
import util.DedicatedAccount;
import util.FafInformationList;
import util.OfferInformation;
import util.PamInformationList;
import util.PamUpdateInformationList;
import util.PromotionPlanInformation;
import util.ServiceOfferings;
import util.UsageCounterUsageThresholdInformation;
import util.UsageThreshold;

public class AIRRequest {

	private boolean successfully, waitingForResponse;
	private List<String> hosts;
	private int sleep, timeout, threshold;

	public AIRRequest(List<String> hosts, int sleep, int timeout, int threshold) {
		this.hosts = hosts;
		this.sleep = sleep;
		this.threshold = threshold;
		this.timeout = timeout;

		// init
		waitingForResponse = true;
	}

	public SocketConnection getConnection() {
		if(hosts != null) {
			SocketConnection connection = null;

			for(String host : hosts) {
				int separator = host.indexOf(":");

				connection = new SocketConnection((host.substring(0, separator)).trim(), Integer.parseInt((host.substring(separator+1)).trim()), sleep, timeout);
				if(connection.isOpen()) {
					if(threshold > 0) connection.setThreshold(threshold);
					break;
				}
			}

			if(connection != null) {
				connection.setSleep(isWaitingForResponse() ? sleep : 0);
			}

			return connection;
		}

		return null;
	}

	public boolean isWaitingForResponse() {
		return waitingForResponse;
	}

	public void setWaitingForResponse(boolean waitingForResponse) {
		this.waitingForResponse = waitingForResponse;
	}

	public boolean isSuccessfully() {
		return successfully;
	}

	public void setSuccessfully(boolean successfully) {
		this.successfully = successfully;
	}

	public BalanceAndDate getBalanceAndDate(String msisdn, int dedicatedAccountID) {
		SocketConnection air = getConnection();

		BalanceAndDate result = new GetBalanceAndDate().getData(air, msisdn,dedicatedAccountID);
		setSuccessfully(air.isAvailable());

		return result;
	}

	public boolean updateBalanceAndDate(String msisdn, HashSet<BalanceAndDate> balancesAndDates, String transactionType, String transactionCode, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateBalanceAndDate().update(air, msisdn,balancesAndDates,transactionType,transactionCode,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateServiceClass(String msisdn, String serviceClassAction, int serviceClassNew, int serviceClassTemporaryNew, Date serviceClassTemporaryNewExpiryDate, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateServiceClass().update(air, msisdn, serviceClassAction, serviceClassNew, serviceClassTemporaryNew, serviceClassTemporaryNewExpiryDate, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public AccountDetails getAccountDetails(String msisdn) {
		SocketConnection air = getConnection();

		AccountDetails result = new GetAccountDetails().getData(air, msisdn);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public HashSet<AccumulatorInformation> getAccumulators(String msisdn, int[][]accumulatorSelection) {
		SocketConnection air = getConnection();

		HashSet<AccumulatorInformation> result = new GetAccumulators().getData(air, msisdn,accumulatorSelection);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public FafInformationList getFaFList (String msisdn, int requestedOwner) {
		SocketConnection air = getConnection();

		if(!isWaitingForResponse()) air.setSleep(0);
		FafInformationList result = new GetFaFList().getData(air, msisdn,requestedOwner);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateCommunityList(String msisdn,int[] communityOldIDs,int[] communityNewIDs, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateCommunityList().update(air, msisdn, communityOldIDs, communityNewIDs, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateSubscriberSegmentation(String msisdn, Integer accountGroupID, ServiceOfferings serviceOfferings, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateSubscriberSegmentation().update(air, msisdn, accountGroupID, serviceOfferings, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public HashSet<OfferInformation> getOffers(String msisdn,int[][] offerSelection,boolean requestInactiveOffersFlag,String offerRequestedTypeFlag,boolean requestDedicatedAccountDetailsFlag) {
		SocketConnection air = getConnection();

		HashSet<OfferInformation> result = new GetOffers().getData(air, msisdn, offerSelection,requestInactiveOffersFlag,offerRequestedTypeFlag, requestDedicatedAccountDetailsFlag);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateOffer(String msisdn,int offerID,Object startDate,Object expiryDate,Integer offerType,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result =  new UpdateOffer().update(air, msisdn, offerID,startDate,expiryDate,offerType,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deleteOffer(String msisdn, int offerID, String originOperatorID, boolean acceptOfferNotFound) {
		SocketConnection air = getConnection();

		boolean result = new DeleteOffer().delete(air, msisdn, offerID, originOperatorID, acceptOfferNotFound);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public HashSet<UsageCounterUsageThresholdInformation> getUsageThresholdsAndCounters(String msisdn,String originOperatorID) {
		SocketConnection air = getConnection();

		HashSet<UsageCounterUsageThresholdInformation> result = new GetUsageThresholdsAndCounters().getData(air, msisdn,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateUsageThresholdsAndCounters(String msisdn,HashSet<UsageCounterUsageThresholdInformation> usageCounterUpdateInformation,HashSet<UsageThreshold>usageThresholdUpdateInformation,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateUsageThresholdsAndCounters().update(air, msisdn,usageCounterUpdateInformation,usageThresholdUpdateInformation,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateFaFList(String msisdn, String fafAction, FafInformationList fafInformation, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateFaFList().update(air, msisdn, fafAction, fafInformation,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deleteAccumulators(String msisdn,Integer serviceClassCurrent ,HashSet<AccumulatorInformation> accumulatorIdentifier,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new DeleteAccumulators().delete(air, msisdn, serviceClassCurrent, accumulatorIdentifier,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deleteDedicatedAccounts(String msisdn,Integer serviceClassCurrent ,HashSet<DedicatedAccount> dedicatedAccountIdentifier,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new DeleteDedicatedAccounts().delete(air, msisdn, serviceClassCurrent, dedicatedAccountIdentifier,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deleteUsageThresholds(String msisdn,HashSet<UsageThreshold> usageThresholds,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new DeleteUsageThresholds().delete(air, msisdn, usageThresholds,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public void getPromotionCounters(String msisdn) {
		SocketConnection air = getConnection();

		new GetPromotionCounters().getData(air, msisdn);
		setSuccessfully(air.isAvailable());
	}

	public HashSet<PromotionPlanInformation> getPromotionPlans(String msisdn, String originOperatorID) {
		SocketConnection air = getConnection();

		HashSet<PromotionPlanInformation> result = new GetPromotionPlans().getData(air, msisdn, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updateAccumulators(String msisdn,HashSet<AccumulatorInformation> accumulatorUpdateInformation,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdateAccumulators().update(air, msisdn, accumulatorUpdateInformation,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updatePromotionPlan(String msisdn,String promotionPlanAction,PromotionPlanInformation promotionPlanNew,PromotionPlanInformation promotionPlanOld,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdatePromotionPlan().update(air, msisdn, promotionPlanAction, promotionPlanNew,promotionPlanOld,originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean installSubscriber (String msisdn, int serviceClassNew, boolean temporaryBlockedFlag, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new InstallSubscriber().install(air, msisdn, serviceClassNew, temporaryBlockedFlag, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean linkSubordinateSubscriber (String msisdn,String masterAccountNumber,String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new LinkSubordinateSubscriber().link(air, msisdn, masterAccountNumber, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean refill(String msisdn, String transactionAmount, String transactionCurrency, String refillProfileID, String voucherActivationCode, String transactionType, String transactionCode, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new Refill().update(air, msisdn, transactionAmount, transactionCurrency, refillProfileID, voucherActivationCode, originOperatorID, transactionType, transactionCode);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deleteSubscriber(String msisdn, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new DeleteSubscriber().delete(air, msisdn, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean addPeriodicAccountManagementData(String msisdn, PamInformationList pamInformationList, boolean acceptServiceIDAlreadyExist, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new AddPeriodicAccountManagementData().add(air, msisdn, pamInformationList, acceptServiceIDAlreadyExist, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean deletePeriodicAccountManagementData(String msisdn, PamInformationList pamInformationList, String originOperatorID, boolean acceptServiceIDNotExist) {
		SocketConnection air = getConnection();

		boolean result = new DeletePeriodicAccountManagementData().delete(air, msisdn, pamInformationList, originOperatorID, acceptServiceIDNotExist);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean runPeriodicAccountManagement(String msisdn, int pamServiceID, String originOperatorID ) {
		SocketConnection air = getConnection();

		boolean result = new RunPeriodicAccountManagement().run(air, msisdn, pamServiceID, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

	public boolean updatePeriodicAccountManagementData(String msisdn, PamUpdateInformationList pamUpdateInformationList, String originOperatorID) {
		SocketConnection air = getConnection();

		boolean result = new UpdatePeriodicAccountManagementData().update(air, msisdn, pamUpdateInformationList, originOperatorID);
		setSuccessfully(air.isAvailable());
		return result;
	}

}
